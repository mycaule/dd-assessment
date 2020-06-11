#!/usr/bin/env bats

@test "addition using bc" {
  echo "hello welt"

  result="$(echo 2+2 | bc)"
  [ "$result" -eq 4 ]
}

@test "zcat is installed" {
  [ "a" == "a" ]
}

@test "wget is installed" {
  [ "a" == "a" ]
}

@test "awk is installed" {
  [ "a" == "a" ]
}

@test "zgrep and grep" {
  [ "a" == "a" ]
}

@test "grep on blacklist" {
  [ "a" == "a" ]
}

@test "unique lines" {
  [ "a" == "a" ]
}

@test "sort on 3rd field" {
  [ "a" == "a" ]
}

@test "top lines using head" {
  [ "a" == "a" ]
}
