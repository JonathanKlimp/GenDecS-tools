#!/usr/bin/env bash
  set -e
  set -u
  set -o pipefail

filtered_file=$(java -jar ClinVar-Filter-1.0.0.jar /Users/jonathan/Documents/GitHub/GenDecS-tools/data/clinvar_20220205.vcf)
echo "$filtered_file"

matched_file=$(java -jar Variant-Matcher-1.0.0.jar "$filtered_file" /Users/jonathan/Documents/GitHub/GenDecS-tools/data/vcfdata.vcf)
echo "$matched_file"

annotated_file=$(java -jar Variant-HPO-Annotator-1.1.0.jar "$matched_file" /Users/jonathan/Documents/GitHub/GenDecS-tools/data/genes_to_phenotype.txt)
echo "$annotated_file"