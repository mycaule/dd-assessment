#!/bin/bash

# user can provide date and time or defaults to 1 day ago
year=$(date '+%Y')
month=$(date '+%m')
day=$(date '+%d' --date="1 day ago")
hour=$(date '+%H')

set_date() {
  year=$1
  month=$2
  day=$3
  hour=$4
}

if [ $# -eq 4 ]; then
  echo "Using user input for date..."
  set_date "$1" "$2" "$3" "$4"
fi

# input and output files
mkdir -p data
filename="pageviews-${year}${month}${day}-${hour}0000.gz"
blacklist="blacklist_domains_and_pages"
output="domain_top25_${year}${month}${day}-${hour}0000"
touch "data/$output"

# downloads input files with resuming
wget -c "https://dumps.wikimedia.org/other/pageviews/$year/$year-$month/$filename" -P data
wget -c "https://s3.amazonaws.com/dd-interview-data/data_engineer/wikipedia/$blacklist" -P data

# all unique domains and already processed domains
domains=$(zcat "data/$filename" | awk '{print $1}' | sort -u)
processed=$(awk '{print $1}' "data/$output" | sort -u)

# processes the files with resuming
echo "Running for $year $month $day $hour..."

for domain in $domains
do
  if echo "$processed" | grep -q "$domain"; then
    printf "%s\r" "skipping $domain                    "
  else
    printf "%s\r" "+ $domain                           "
  
    # top 25 results for one single domain, sorting on third field (views)
    zgrep -E "^$domain " "data/$filename" \
      | grep -vF -f "data/$blacklist" \
      | sort -nrk3,3 | head -25 \
      | awk '{print $1" "$2" "$3}' >> "data/$output"
  fi
done

echo "Finished!"

