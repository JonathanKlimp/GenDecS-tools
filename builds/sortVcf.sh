#!/usr/bin/env bash

usage="$(basename "${0}") usage: [-f vcf file to sort] [-h help]"

while getopts f::h: flag
do
    case "${flag}" in
        f) vcf_file=${OPTARG};;
        h) echo "$usage"
           exit
           ;;
        *) echo "$usage"
           exit 1
           ;;
    esac
done

directoryFile="${vcf_file##*/}"

echo "$directoryFile"
echo "$vcf_file"

cat "$vcf_file" | awk '$1 ~ /^#/ {print $0;next} {print $0 | "sort -k1,1V -k2,2n"}' > temp.vcf && mv temp.vcf "$vcf_file"