import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class GeneHpoAnnotator {
    private final File vcfDatalocation;
    private final File genesToPhenotypeLoc;
    private final String outputLocation;

    public GeneHpoAnnotator(String vcfDataLocation, String genesToPhenotypeLoc, String outputLocation) {
        if(outputLocation == null) {
            String vcfDataLoc = vcfDataLocation.replace(".vcf", "");
            this.outputLocation = vcfDataLoc + "_annotated_hpo.vcf";
        } else {
            if (!outputLocation.endsWith("/")) {
                throw new IllegalArgumentException("Given output location is not a directory: " + outputLocation);
            } else {
                File vcfDataFile = new File(vcfDataLocation);
                String vcfFileName = vcfDataFile.getName().replace(".vcf", "");
                this.outputLocation = outputLocation + vcfFileName + "_annotated_hpo.vcf";
            }
        }
        this.genesToPhenotypeLoc = new File(genesToPhenotypeLoc);
        this.vcfDatalocation = new File(vcfDataLocation);
    }

    /**
     * Static method that reads a vcf file and annotates each variant with its associated HPO terms
     * @return String with the new written file location
     * @throws IOException when no file is found
     */
    public String annotateVcfWithHpo() throws IOException {
        Scanner reader = new Scanner(this.vcfDatalocation);

        File annotatedFile = new File(this.outputLocation);
        BufferedWriter writer = new BufferedWriter(new FileWriter(annotatedFile));
        VariantHpoMatcher variantHpoMatcher = new VariantHpoMatcher();

        while (reader.hasNextLine()) {
            String currentLine = reader.nextLine();
            if (currentLine.startsWith("#")) {
                writer.write(currentLine + System.getProperty("line.separator"));
            } else {
                HashMap<String, ArrayList<String>> termsAndDiseases = variantHpoMatcher.matchVariantWithHpo(currentLine, this.genesToPhenotypeLoc);
                writer.write(currentLine + '|'
                        + termsAndDiseases.get("hpoTerms")
                        + "|"
                        + termsAndDiseases.get("diseaseIds")
                        + System.getProperty("line.separator"));
            }
        }
        reader.close();
        writer.close();
        return annotatedFile.getAbsolutePath();
    }
}
