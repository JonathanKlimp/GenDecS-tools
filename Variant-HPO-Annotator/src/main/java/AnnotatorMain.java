import java.io.IOException;

public class AnnotatorMain {
    public static void main(String[] args) throws IOException {
        String vcfDataLocation = args[0];
        String genesToPhenotypeLoc = args[1];
        String dataLocation = GeneHpoAnnotator.annotateVcfWithHpo(vcfDataLocation, genesToPhenotypeLoc);
        System.out.println(dataLocation);
    }
}
