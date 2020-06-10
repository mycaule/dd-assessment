<p align="center">
  June 2020 DD Assessment
</p>

<p align="center">
  <a href="https://github.com/mycaule/dd-assessment/actions"><img src="https://github.com/mycaule/dd-assessment/workflows/CI/badge.svg?branch=master" alt="Build Status"></a>
  <br>
  <br>
</p>

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
python3 stats.py show
python3 stats.py run --group country
jupyter-notebook stats.ipynb
```
See also [GitHub Actions logs](https://github.com/mycaule/dz-assessment/actions)

### References

- [Netflix - Open-Sourcing Metaflow, a Human-Centric Framework for Data Science](https://netflixtechblog.com/open-sourcing-metaflow-a-human-centric-framework-for-data-science-fa72e04a5d9)
  - [`Netflix/metaflow/tutorials/02-statistics`](https://github.com/Netflix/metaflow/tree/master/metaflow/tutorials/02-statistics)
