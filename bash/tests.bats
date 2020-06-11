#!/usr/bin/env bats

@test "zcat is installed" {
  [ "$(zcat data/pageviews-test.gz | wc -l)" -eq 1000 ]
}

@test "wget is installed" {
  wget -c "https://www.google.fr/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png" \
       -O "data/googlelogo.png"

  md5sum data/googlelogo.png | grep -q "8f9327"
}

@test "awk is installed" {
  result=$(echo "h e y you" | awk '{print $1" "$2" "$3}')
  [ "$result" == "h e y" ]
}

@test "zgrep" {
  [ "$(zgrep -E "^zu " data/pageviews-test.gz | wc -l)" -eq 41 ]
}

@test "unique lines" {
  [ "$(cat data/blacklist-test | cut -d' ' -f1 | sort -u | wc -l)" -eq 4 ]
}

@test "sort on a field" {
  [ "$(cat data/blacklist-test | sort -nrk2,2 | head -1)" == "af 2009" ]
}
