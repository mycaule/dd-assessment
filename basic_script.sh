#!/bin/bash

mkdir -p data

wget -c https://dumps.wikimedia.org/other/pageviews/2020/2020-06/pageviews-20200601-020000.gz -P data

# list of unique domains
for domain in $(zcat data/pageviews-20200601-020000.gz | awk '{print $1}' | uniq)
do
  printf "%s" "$domain-"
done

# top 25 results for fr
zcat data/pageviews-20200601-020000.gz | grep -E '^en ' \
  | grep -vF -f data/blacklist_domains_and_pages \
  | sort -nrk3,3 | head -25 \
  | awk '{print $2" "$3}'
