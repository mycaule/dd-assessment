<p align="center">
  June 2020 DD Assessment
</p>

<p align="center">
  <a href="https://github.com/mycaule/dd-assessment/actions"><img src="https://github.com/mycaule/dd-assessment/workflows/CI/badge.svg?branch=master" alt="Build Status"></a>
  <br>
  <br>
</p>

### Observations

In the Linux world, it is frequent users and admins have to process relatively large text files. In particular for the problem of [sorting and filtering](https://en.wikipedia.org/wiki/External_sorting) with limited amount of computing resources, there have been very efficient tools from the GNU utils.

Considering the pageviews hourly file is only about 50MB compressed and the blacklist file 3MB, we try a first pragmatic approach using only basic CLI tools from Debian based distributions.

Hence, using `wget`, `cat`, `gzip`, `grep`, `awk`, `sort`, `head`, we can pretty much solve the exercise in a few lines of codes using these one-liners.

```bash
# downloading a file
wget https://dumps.wikimedia.org/other/pageviews/2020/2020-06/pageviews-20200601-020000.gz -P data
# about 1500 different domains

# list of unique domains
zcat pageviews-20200601-020000.gz | awk '{print $1}' | uniq

# top 25 results for fr
zcat pageviews-20200601-020000.gz | grep -E '^fr ' | grep -vF -f blacklist_domains_and_pages | sort -nrk3,3 | head -25 | awk '{print $2" "$3}'
```

Using the fact that we can read the files into streams and pipe them through different tools, we come up with a nice solution that can run on a computer without much RAM and CPU.
It is in fact my case on my personal computer. I believe cluster and cloud computing shouldn't always be used as a hammer to solve problem.

The solution we are studying are similar to the merge sort algorithm, we show the ideas behind three implementations in Bash (GNU utils), Python (Pandas) and Scala (Spark)

### Bash solution

We keep reading the gzip file sequentially into subproblems by the domain and keep appending the result file.

```bash
cd bash

./run.sh
# or
./run.sh 2020 06 01 00
```

Running tests with [`bats`](https://github.com/sstephenson/bats).
```bash
bats tests.bats
```

- runs in about half an hour
- can be optimized by splitting the file and using [GNU parallel](https://www.gnu.org/software/parallel/) to take full advantage of multi-core processing.

### Python solution

We make further investigations using Pandas in a [Colab notebook](https://colab.research.google.com/drive/1VJk8rqx0pWe4KkqmjQ63ILQdjoLqrZ3M?usp=sharing) and finally choose a simple derived implementation using Metaflow in Python.

Metaflow is a open source tool from Netflix for ML pipelines which presents multiple advantages from rapid prototyping to interesting abstractions between the local host and the cloud on AWS using S3, Batch and Step Functions.

You can write simple dags using their Python library and have out of the box CLI interface boilerplate code (documentation, logging, etc.).

We choose to let users be able to choose the number of domains in which to compute in parallel using multiple process.

To do the job completely would require to choose the subjobs in a smart manner over the distribution of domains, and then to join the results at the end.

*Pre-requisites*

Create a virtual environment with all the python packages required.

```bash
python3 -m venv venv
source venv/bin/activate
pip3 install -r requirements.txt
```

**To run the workflow**

```bash
cd python

# Command line help on the arguments
python3 stats.py run --help

# Shows the DAG
python3 stats.py show

# Running on the hourly file 1 day ago
python3 stats.py run --domains '["zu", "zu.d", "zu.m"]'
```

Running tests
```bash
python3 tests.py
```

See also [GitHub Actions logs](https://github.com/mycaule/dz-assessment/actions)

- runs in about an hour in the default Google Colab instance.

**References**

- [Netflix - Open-Sourcing Metaflow, a Human-Centric Framework for Data Science](https://netflixtechblog.com/open-sourcing-metaflow-a-human-centric-framework-for-data-science-fa72e04a5d9)
  - [`Netflix/metaflow/tutorials/02-statistics`](https://github.com/Netflix/metaflow/tree/master/metaflow/tutorials/02-statistics)

This second approach is more pragmatic that Spark, which would be required to compute analytics other long period of time. This exercise was only about computing the analytics for periods of one hour, eventually looping or scheduling cron jobs running every few hours to automate.

Wikimedia [says they compute their analytics using Hadoop](https://wikitech.wikimedia.org/wiki/Analytics/AQS/Pageviews) and provide similar functionality to this exercise in a REST API.

### Scala solution

Lastly we use Spark which provides more safety dealing with the datasets over time, but requires running the code on a cluster of machines with more sophisticated software installed.

I first did my investigations using a [Databricks notebook](https://databricks-prod-cloudfront.cloud.databricks.com/public/4027ec902e239c93eaaa8714f173bcfc/3675239947483677/1559863225658641/3923561340607708/latest.html) and then wrote a small scala project.

```bash
cd scala
sbt run
# or
sbt run 2020 06 01 00
```

Running the tests
```bash
sbt test
```

Going further in the production would require doing more work in the packaging for the archive, and configuring EMR or Dataproc to submit the job. AWS Step Functions and GCP Cloud Composer are also solutions to schedule the job in production.

- runs in about a minute on the default Databricks cluster
