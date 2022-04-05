package clinvar.filter;

public class FilterMain {
    public static void main(String[] args) {
        String clinvarLocation = args[0];
        ClinVarFilter clinVarFilter = new ClinVarFilter(StarRating.ZEROSTAR, clinvarLocation);

        String filteredClinvar = clinVarFilter.removeStatus();
        System.out.println(filteredClinvar);
    }
}
