<p align="center">
  June 2020 DD Assessment
</p>

<p align="center">
  <a href="https://github.com/mycaule/dd-assessment/actions"><img src="https://github.com/mycaule/dd-assessment/workflows/CI/badge.svg?branch=master" alt="Build Status"></a>
  <br>
  <br>
</p>

### Observations

Some GNU utils that can solve the problem [with the command line](basic_script.sh).

```bash
# downloading a file
wget https://dumps.wikimedia.org/other/pageviews/2020/2020-06/pageviews-20200601-020000.gz -P data
# about 1500 different domains

# list of unique domains
zcat pageviews-20200601-020000.gz | awk '{print $1}' | uniq

# top 25 results for fr
zcat pageviews-20200601-020000.gz | grep -E '^fr ' | grep -vF -f blacklist_domains_and_pages | sort -nrk3,3 | head -25 | awk '{print $2" "$3}'
```

Using the fact that we can read the stream of gzipped pageviews already sorted by domains, this should be the fastest way and can be optimized running between multiple core using GNU Parallel.

We make further investigations using Pandas in a [Jupyter Notebook](notebook.ipynb) and finally choose a simple derived implementation using Metaflow in Python.

This approach is more pragmatic that Spark, which would be required to compute analytics other long period of time. Wikimedia [says they compute their analytics using Hadoop](https://wikitech.wikimedia.org/wiki/Analytics/AQS/Pageviews) and provide similar functionality to this exercise in a REST API.

Metaflow presents multiple advantages in Data Science workflows and also gives interesting abstractions to go from the desktop computer to AWS using S3, Batch and Step Functions.

### Pre-requisites

Create a virtual environment with all the python packages required.

```bash
python3 -m venv venv
source venv/bin/activate
pip3 install -r requirements.txt
```

### Exercise

**To run the batch script**

```bash
# Command line help on the arguments
python3 stats.py run --help

# Shows the DAG
python3 stats.py show

# Running on the hourly file 1 day ago
python3 stats.py run --domains '["it", "ca"]'
```
See also [GitHub Actions logs](https://github.com/mycaule/dz-assessment/actions)

### References

- [Netflix - Open-Sourcing Metaflow, a Human-Centric Framework for Data Science](https://netflixtechblog.com/open-sourcing-metaflow-a-human-centric-framework-for-data-science-fa72e04a5d9)
  - [`Netflix/metaflow/tutorials/02-statistics`](https://github.com/Netflix/metaflow/tree/master/metaflow/tutorials/02-statistics)
