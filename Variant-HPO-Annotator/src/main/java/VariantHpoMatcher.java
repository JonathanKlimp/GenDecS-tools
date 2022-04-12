import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class VariantHpoMatcher {

    /**
     * Method that gets a variant string and returns a list with HPO terms
     * associated with the gene in the variant.
     * @param variant Sting of a variant from a vcf file
     * @param genesToPhenotypeLoc location of genes_to_phenotype.txt
     * @return ArrayList with associated HPO terms as strings
     */
    public HashMap<String, ArrayList<String>> matchVariantWithHpo(String variant, String genesToPhenotypeLoc) {
        String geneSymbol = this.getGene(variant);
        return this.getHpo(geneSymbol, genesToPhenotypeLoc);
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
        return infoString[3];
    }

    /**
     * Method that returns an array list with HPO terms for a given gene. It reads the file
     * gene_to_phenotype.txt and gathers all HPO terms that are associated with the given gene.
     * @param geneSymbol String with the gene symbol
     * @return ArrayList<String> with all found HPO terms
     */
    public HashMap<String, ArrayList<String>> getHpo(String geneSymbol, String genesToPhenotypeLoc) {
        HashMap<String, ArrayList<String>> termsAndDiseases = new HashMap<>();
        ArrayList<String> hpoTerms = new ArrayList<>();
        ArrayList<String> diseases = new ArrayList<>();
        try {
            File file = new File(genesToPhenotypeLoc);
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String currentLine = reader.nextLine();
                if (currentLine.contains(geneSymbol)) {
                    String[] lineSplit = currentLine.split("\t");
                    String gene = lineSplit[1];
                    String hpoId = lineSplit[2];
                    String hpoTerm = lineSplit[3];
                    String diseaseId = lineSplit[8];
                    hpoTerms.add(hpoTerm.trim());
                    diseases.add(diseaseId.trim());
                }
            }
            termsAndDiseases.put("hpoTerms", hpoTerms);
            termsAndDiseases.put("diseaseIds", diseases);
            return termsAndDiseases;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return termsAndDiseases;
    }
}
