# GenDecS-tools

This repository contains the tools that are used to process and annotate VCF data.
These tools are:

## ClinVar filter
A ClinVar filter which filters ClinVar on variants with a quality rating of at least one star
and which are pathogenic/likely pathogenic/ or have an uncertain significance.  
The output is a string with the location of the new filtered ClinVar file.


### Requirements
* ClinVar vcf file which can be downloaded from: [Clinvar](https://ftp.ncbi.nlm.nih.gov/pub/clinvar/vcf_GRCh37/)

### Usage 
```
java -jar ClinVar-Filter-[version].jar --clinvar [location of clinvar] --ouput [output directory]
```
--help for options  
Output: filtered ClinVar file.

## VariantMatcher

Tool that matches variants with ClinVar. If a variant is present in ClinVar the alleleid is annotated to said variant.  
A new file with the matched variants will be written.

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
java -jar Variant-Matcher-[version].jar --clinvar [location_of_filtered_clinvar] --data [location_of_vcf_data] --ouput [output directory]
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
java -jar Variant-HPO-Annotator-[version].jar --data [location_of_vcf_data] --genestopheno [location_of_genes_to_phenotype.txt] --ouput [output directory]
```
--help for options
Output: annotated vcf file with HPO terms and disease ids.

## Example

This is an example for GenDecS. This example will process al data
present in gendecsTestData and upload it to emx2.

* Make sure you are running molgenis emx2
* On the terminal navigate to the directory gendecsTestData/
* Once in the dir execute the following query:

This query loops through each file in the directory and executes the pipeline to process upload the data.
```
for FILE in *; do sh GenDecS_filter_and_upload_pipeline.sh -f $FILE -o [output dir] -c [path to clinvar] -g [path to genes_to_phenotype.txt]; done
```

### Contact

Jonathan Klimp
email: j.klimp@st.hanze.nl
