import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class GeneHpoAnnotator {

    /**
     * Static method that reads a vcf file and annotates each variant with its associated HPO terms
     * @return String with the new written file location
     * @throws IOException when no file is found
     */
    public static String annotateVcfWithHpo(String vcfDatalocation, String genesToPhenotypeLoc) throws IOException {
        File dataFile = new File(vcfDatalocation);
        Scanner reader = new Scanner(dataFile);
        String vcfDataLocation = vcfDatalocation.replace(".vcf", "");
        String pathName = vcfDataLocation + "_annotated_hpo.vcf";

        File annotatedFile = new File(pathName);
        BufferedWriter writer = new BufferedWriter(new FileWriter(annotatedFile));
        VariantHpoMatcher variantHpoMatcher = new VariantHpoMatcher();

        while (reader.hasNextLine()) {
            String currentLine = reader.nextLine();
            if (currentLine.startsWith("#")) {
                writer.write(currentLine + System.getProperty("line.separator"));
            } else {
                ArrayList<String> hpoTerms = variantHpoMatcher.matchVariantWithHpo(currentLine, genesToPhenotypeLoc);
                writer.write(currentLine + '\t' + hpoTerms
                        + System.getProperty("line.separator"));
            }
        }
        reader.close();
        writer.close();
        return vcfDataLocation;
    }
}
