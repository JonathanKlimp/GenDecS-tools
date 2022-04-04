package clinvar.matcher;

public class matcherMain {
    public static void main(String[] args) {
        String clinvarLocation = args[0];
        String vcfDataLocation = args[1];

        VariantMatcher variantMatcher = new VariantMatcher(clinvarLocation, vcfDataLocation);
        String fileLocation = variantMatcher.matchWithClinvar();
        System.out.println(fileLocation);
    }
}
