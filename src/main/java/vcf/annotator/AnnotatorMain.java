package vcf.annotator;

import java.io.IOException;

public class AnnotatorMain {
    public static void main(String[] args) throws IOException {
        String vcfDataLocation = args[0];
        GeneHpoAnnotator geneHpoAnnotator = new GeneHpoAnnotator(vcfDataLocation);
        String dataLocation = geneHpoAnnotator.annotateVcfWithHpo();
        System.out.println(dataLocation);
    }
}
