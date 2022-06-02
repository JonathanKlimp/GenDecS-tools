import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class GeneHpoAnnotator {
    private static final Logger logger = LogManager.getLogger(GeneHpoAnnotator.class);

    private final File vcfDatalocation;
    private final File genesToPhenotypeLoc;
    private final String outputLocation;

    /**
     *
     * @param vcfDataLocation location of the vcf data.
     * @param genesToPhenotypeLoc location of genes_to_phenotype.txt
     * @param outputLocation desired output directory. If null the directory of the given vcf data is used.
     */
    public GeneHpoAnnotator(String vcfDataLocation, String genesToPhenotypeLoc, String outputLocation) {
        if(outputLocation == null) {
            String vcfDataLoc = vcfDataLocation.replace(".vcf", "");
            this.outputLocation = vcfDataLoc + "_annotated_hpo.vcf";
        } else {
            File vcfDataFile = new File(vcfDataLocation);
            String vcfFileName = vcfDataFile.getName().replace(".vcf", "");
            this.outputLocation = outputLocation + vcfFileName + "_annotated_hpo.vcf";
        }
        this.genesToPhenotypeLoc = new File(genesToPhenotypeLoc);
        this.vcfDatalocation = new File(vcfDataLocation);
    }

    /**
     * Static method that reads a vcf file and annotates each variant with its associated HPO terms.
     * @return String with the new written file location.
     */
    public String annotateVcfWithHpo() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(this.vcfDatalocation));

            File annotatedFile = new File(this.outputLocation);
            BufferedWriter writer = new BufferedWriter(new FileWriter(annotatedFile));
            VariantHpoMatcher variantHpoMatcher = new VariantHpoMatcher();
            logger.info("creating file " + annotatedFile.getAbsolutePath());

            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                if (currentLine.startsWith("#")) {
                    writer.write(currentLine + System.getProperty("line.separator"));
                } else {
                    HashMap<String, ArrayList<String>> termsAndDiseases = variantHpoMatcher.matchVariantWithHpo(currentLine, this.genesToPhenotypeLoc);
                    logger.trace("annotating " + annotatedFile.getAbsolutePath() + " with: " + termsAndDiseases);
                    writer.write(currentLine + '|'
                            + termsAndDiseases.get("hpoTerms")
                            + "|"
                            + termsAndDiseases.get("diseaseIds")
                            + System.getProperty("line.separator"));
                }
            }
            reader.close();
            writer.close();
            logger.info("Annotated the file " + annotatedFile.getAbsolutePath() + " with HPO terms and disease ids");
            return annotatedFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
