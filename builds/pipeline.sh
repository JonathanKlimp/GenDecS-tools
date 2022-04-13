#!/usr/bin/env bash
  set -e

java -jar ClinVar-Filter-2.0.0.jar --output /Users/jonathan/Documents/GitHub/GenDecS-tools/data/ /Users/jonathan/Documents/GitHub/GenDecS-tools/data/clinvar_20220205.vcf

java -jar Variant-Matcher-2.0.0.jar --output /Users/jonathan/Documents/GitHub/GenDecS-tools/data/ /Users/jonathan/Documents/GitHub/GenDecS-tools/data/clinvar_20220205_filtered_ZEROSTAR.vcf /Users/jonathan/Documents/GitHub/GenDecS-tools/data/vcfdata.vcf

java -jar Variant-HPO-Annotator-2.0.0.jar --output /Users/jonathan/Documents/GitHub/GenDecS-tools/data/ /Users/jonathan/Documents/GitHub/GenDecS-tools/data/vcfdata_clinvar_matched.vcf /Users/jonathan/Documents/GitHub/GenDecS-tools/data/genes_to_phenotype.txt
