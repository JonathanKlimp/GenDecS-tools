#!/usr/bin/env bash
  set -e

usage="$(basename "${0}") usage: [-h for help] [-f vcf data file] | [-o output directory] | [-c clinvar file] | [-g genes_to_phenotype.txt]"

if [ $# -lt 4 ]; then
    echo "$usage"
    exit 1
fi

while getopts f:o:c:g:h: flag
do
    case "${flag}" in
        f) vcf_file=${OPTARG};;
        o) output_dir=${OPTARG};;
        c) clinvar_file=${OPTARG};;
        g) genes_to_phenotype=${OPTARG};;
        h) echo "$usage"
           exit
           ;;
        :) printf "missing argument for -%s\n" "$OPTARG" >&2
           echo "$usage" >&2
           exit 1
           ;;
        *) echo "$usage"
           exit 1
           ;;
    esac
done

java -jar ClinVar-Filter-2.0.3.jar --output "$output_dir" --clinvar "$clinvar_file"

# extract the file name from the given directory
# and add the output name
clinvar_name="${clinvar_file##*/}"
clinvar_file="${clinvar_name%.*}"
filtered_clinvar="$output_dir""$clinvar_file""_filtered_ZEROSTAR.vcf"

java -jar Variant-Matcher-2.1.4.jar --output "$output_dir" --clinvar "$filtered_clinvar" --data "$vcf_file"

# extract the file name from the given directory
# and add the output name
file_name="${vcf_file##*/}"
file="${file_name%.*}"
filtered_vcf="$output_dir""$file""_clinvar_matched.vcf"

java -jar Variant-HPO-Annotator-2.0.4.jar --output "$output_dir" --data "$filtered_vcf" --genetopheno "$genes_to_phenotype"

annotated_vcf="$output_dir""$file""_clinvar_matched_annotated_hpo.vcf"

curl -v -F upload=@"$annotated_vcf" http://localhost:8080/gendecs/api/vcf