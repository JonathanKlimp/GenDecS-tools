# GenDecS-tools

This repository contains the tools that are used to process and annotate VCF data.
These tools are:

## ClinVar filter
a ClinVar filter which filters ClinVar on high quality variants and pathogenic/
likely pathogenic/ uncertain significance.  
The output is a string with the location of the new filtered ClinVar file.


### Requirements
* ClinVar vcf file which can be downloaded from: [Clinvar](https://ftp.ncbi.nlm.nih.gov/pub/clinvar/vcf_GRCh37/)

### Usage 
```
java -jar ClinVar-Filter-[version].jar [options] [--clinvar location of clinvar]
```
--help for options  
Output: filtered ClinVar file.

## VariantMatcher

A tool that filters variants from an input vcf data file on if they are present in ClinVar. A
new file with the matched variants will be written.

### Requirements
* A vcf file with variant data 
* ClinVar data file.    

For the vcf file [V.I.P](https://github.com/molgenis/vip) output is expected.  
Example data can be found in the gendecsTestData folder.  
For the ClinVar file the output of the ClinVar is expected but a ClinVar file 
downloaded from [Clinvar](https://ftp.ncbi.nlm.nih.gov/pub/clinvar/vcf_GRCh37/)
will also work.

### Usage
```
java -jar Variant-Matcher-[version].jar [options] [--clinvar location_of_filtered_clinvar] [--data location_of_vcf_data]
```
--help for options
Output: vcf file that is matched with ClinVar

## VCF HPO annotator
An annotator tool which annotates HPO terms which are associated with the gene of a variant.  
A vcf input file is parsed and for each variant present the associated HPO terms are added
to the variant line.

### Requirements
* [genes_to_phenotype.txt](https://hpo.jax.org/app/download/annotation)
* vcf file

The output of the VariantMatcher tool is expected as vcf file.

### Usage

```
java -jar Variant-HPO-Annotator-[version].jar [options] [--clinvar location_of_vcf_data] [--genestopheno location_of_genes_to_phenotype.txt]
```
--help for options
Output: annotated vcf file with HPO terms and disease id's
