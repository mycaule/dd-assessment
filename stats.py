from metaflow import FlowSpec, step, Parameter, JSONType
import datetime
import re
import pandas
import filereader


def read_log(filename, folder, domains):
    """Failsafe way to read CSV"""
    df = pandas.read_csv(filereader.script_path(filename, folder),
                         compression='gzip', sep=' ', usecols=[0, 1, 2],
                         names=['domain', 'title', 'views'])
    filter = df.domain.isin(domains)
    df = df.loc[filter]
    return df.dropna(thresh=3)


class PageViewsStatsFlow(FlowSpec):
    """
    A flow to generate statistics about the page views

    The flow performs the following steps:
    1) Ingests a CSV into a Pandas dataframe
    2) Fan-out over group using Metaflow foreach
    3) Compute top K elements for each group
    4) Save a dictionary of group specific statistics
    """

    domains = Parameter("domains", help="List of domains to compute",
                        type=JSONType, default='["fr", "en"]')
    k = Parameter("k", help="Maximum number of top elements", default=25)
    date = Parameter("date", help="Date to fetch the data from",
                     default="20200601")
    hour = Parameter("hour", help="Hour of the day", default="00")
    folder = Parameter("folder", help="Folder with the hourly dumps",
                       default="data")

    @step
    def start(self):
        """
        The start step:
        1) Loads the listening metadata into pandas dataframe
        2) Finds all the unique domains
        3) Launches parallel statistics computation for each domain
        """
        td = datetime.datetime(2020, 6, 1, 0, 0)  # datetime.datetime.today()

        # TESTME
        last_week = ["pageviews-%s-020000.gz" %
                     (td - datetime.timedelta(i)).strftime('%Y%m%d')
                     for i in range(7)]
        # FIXME past 24 hours of last day

        df = pandas.DataFrame(columns=['domain', 'title', 'views'])
        for f in filereader.list_dir(self.folder):
            d_search = re.search('pageviews-\\d{4}\\d{2}\\d{2}-020000.gz', f)
            if d_search:
                d_found = d_search.group(0)
                if d_found in last_week:
                    df = df.append(read_log(d_found, self.folder,
                                            self.domains), sort=False)

        self.dataframe = df
        # Compute statistics for each group in parallel (fan-out)
        print(len(self.dataframe.index))

        self.next(self.compute_statistics, foreach='domains')

    @step
    def compute_statistics(self):
        "Computes statistics for a single group element"
        self.element = self.input
        print("Computing statistics for %s" % self.element)

        # Find all the rows related to this domain
        filter = self.dataframe.domain == self.element
        self.dataframe = self.dataframe.loc[filter]

        self.dataframe = self.dataframe.groupby(
            ['title'])['views'].sum().reset_index()

        self.top_k = self.dataframe.nlargest(self.k, ['views'])
        self.next(self.join)

    @step
    def join(self, inputs):
        "Joins parallel branches and merges results into a dictionary"
        self.element_stats = {
            inp.element: {'top_k': inp.top_k} for inp in inputs
        }
        self.next(self.write_top_k)

    @step
    def write_top_k(self):
        "Writes the top K to a text file"
        data = [(element,
                 data['top_k'][['title', 'views']].to_dict('split')['data'])
                for element, data in self.element_stats.items()]

        pandas.DataFrame([[element, d[0], d[1]] for element, data2 in data
                          for d in data2]).to_csv(
            filereader.script_path('%s_top_%s_%s%s0000.csv' %
                                   ('domain', self.k, self.date, self.hour),
                                   self.folder),
            sep=' ', header=False, index=False)

        self.next(self.end)

    @step
    def end(self):
        "Ends the flow"
        pass


if __name__ == '__main__':
    PageViewsStatsFlow()
