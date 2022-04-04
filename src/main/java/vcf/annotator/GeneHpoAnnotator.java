package vcf.annotator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class GeneHpoAnnotator {
    private final String vcfDatalocation;

    public GeneHpoAnnotator(String vcfDataLocation) {
        this.vcfDatalocation = vcfDataLocation;
    }

    public String annotateVcfWithHpo() throws IOException {
        File dataFile = new File(this.vcfDatalocation);
        Scanner reader = new Scanner(dataFile);
        String vcfDataLocation = this.vcfDatalocation.replace(".vcf", "");
        String pathName = vcfDataLocation + "_annotated_hpo.vcf";

        File annotatedFile = new File(pathName);
        BufferedWriter writer = new BufferedWriter(new FileWriter(annotatedFile));
        VariantHpoMatcher variantHpoMatcher = new VariantHpoMatcher();

        while (reader.hasNextLine()) {
            String currentLine = reader.nextLine();
            if (currentLine.startsWith("#")) {
                writer.write(currentLine + System.getProperty("line.separator"));
            } else {
                ArrayList<String> hpoTerms = variantHpoMatcher.matchVariantWithHpo(currentLine);
                writer.write(currentLine + '\t' + hpoTerms
                        + System.getProperty("line.separator"));
            }
        }
        reader.close();
        writer.close();
        return vcfDataLocation;
    }
}
