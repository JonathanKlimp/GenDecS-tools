package vcf.annotator;

import java.io.IOException;

public class AnnotatorMain {
    public static void main(String[] args) throws IOException {
        String vcfDataLocation = args[0];
        String dataLocation = GeneHpoAnnotator.annotateVcfWithHpo(vcfDataLocation);
        System.out.println(dataLocation);
    }
}
