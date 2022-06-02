import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * Object VariantHpoMatcher which matches a given variant String with its associates HPO terms found in
 * genes_to_phenotype.txt.
 * The method matchVariantWithHpo is the method that matches the variants with HPO terms. It uses
 * the methods getGene and getHPO which are also public.
 */
public class VariantHpoMatcher {
    private static final Logger logger = LogManager.getLogger(VariantHpoMatcher.class);

    /**
     * Method that gets a variant string and returns a list with HPO terms
     * associated with the gene in the variant.
     * @param variant Sting of a variant from a vcf file
     * @param genesToPhenotype location of genes_to_phenotype.txt
     * @return ArrayList with associated HPO terms as strings
     */
    public HashMap<String, ArrayList<String>> matchVariantWithHpo(String variant, File genesToPhenotype) {
        String geneSymbol = this.getGene(variant);
        return this.getHpo(geneSymbol, genesToPhenotype);
    }

    /**
     * Method that returns the gene from a variant line. Variant from V.I.P output is expected
     * @param variant String with the variant line
     * @return String with the gene symbol
     */
    public String getGene(String variant) {
        if(variant.equals("")) {
            return "";
        }
        String[] splittedLine = variant.split("\t");
        String[] infoString = splittedLine[7].split("\\|");
        logger.trace("found the following gene: " + infoString[3] + " from: " + variant);
        return infoString[3];
    }

    /**
     * Method that returns an array list with HPO terms for a given gene. It reads the file
     * gene_to_phenotype.txt and gathers all HPO terms that are associated with the given gene.
     * @param geneSymbol String with the gene symbol
     * @return ArrayList<String> with all found HPO terms
     */
    public HashMap<String, ArrayList<String>> getHpo(String geneSymbol, File genesToPhenotype) {
        HashMap<String, ArrayList<String>> termsAndDiseases = new HashMap<>();
        ArrayList<String> hpoTerms = new ArrayList<>();
        ArrayList<String> diseases = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(genesToPhenotype));

            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.contains(geneSymbol)) {
                    String[] lineSplit = currentLine.split("\t");
                    String gene = lineSplit[1];
                    String hpoId = lineSplit[2];
                    String hpoTerm = lineSplit[3];
                    String diseaseId = lineSplit[8];
                    hpoTerms.add(hpoTerm.trim().replace(",", ";"));
                    diseases.add(diseaseId.trim());
                }
            }
            termsAndDiseases.put("hpoTerms", hpoTerms);
            termsAndDiseases.put("diseaseIds", diseases);
            logger.trace("Found the following HPO terms and diseases for the gene " + geneSymbol + ": " + hpoTerms + diseases);
            return termsAndDiseases;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return termsAndDiseases;
    }
}
