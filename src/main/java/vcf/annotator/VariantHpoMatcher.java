package vcf.annotator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class VariantHpoMatcher {

    /**
     * Method that gets a variant string and returns a list with HPO terms
     * associated with the gene in the variant.
     * @param variant Sting of a variant from a vcf file
     * @return ArrayList with associated HPO terms as strings
     */
    public ArrayList<String> matchVariantWithHpo(String variant) {
        String geneSymbol = this.getGene(variant);
        return this.getHpo(geneSymbol);
    }

    private String getGene(String variant) {
        String[] splittedLine = variant.split("\t");
        String[] infoString = splittedLine[7].split("\\|");
        return infoString[3];
    }

    private ArrayList<String> getHpo(String geneSymbol) {
        ArrayList<String> hpoTerms = new ArrayList<>();
        try {
            File file = new File("data/genes_to_phenotype.txt");
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
                }
            }
            return hpoTerms;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return hpoTerms;
    }
}
