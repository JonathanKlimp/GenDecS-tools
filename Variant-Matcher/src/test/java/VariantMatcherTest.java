import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VariantMatcherTest {
    @Test
    void matchwithClinvar_normalInput() throws IOException {
        VariantMatcher variantMatcher = new VariantMatcher("/Users/jonathan/Documents/GitHub/GenDecS-tools/data/clinvar_20220205_filtered_ZEROSTAR.vcf", "/Users/jonathan/Documents/GitHub/GenDecS-tools/testdata/threeVariants.vcf", null);
        String fileLocation = variantMatcher.matchWithClinvar();
        List<String> expectedList = new ArrayList<>();
        expectedList.add("# This is a test vcf file with three random variants");
        expectedList.add("#CHROM\tPOS\tID\tREF\tALT\tQUAL\tFILTER\tINFO");
        expectedList.add("1\t9787030\t.\tG\tA\t.\tPASS\tCSQ=A|downstream_gene_variant|MODIFIER|CLSTN1|22883|Transcript|NM_001009566.3|protein_coding||||||||||rs397518423&CM067447&COSV63126880|1|1954|-1|||EntrezGene||||||||pathogenic|0&0&1|1&1&1|25741868&24165795&16984281&24136356&24610295|||||||-6|-38|9|15|0.00|0.00|0.00|0.00|PIK3CD|VUS|0.03777721||||||,A|downstream_gene_variant|MODIFIER|CLSTN1|22883|Transcript|NM_001302883.1|protein_coding||||||||||rs397518423&CM067447&COSV63126880|1|2049|-1|||EntrezGene||||||||pathogenic|0&0&1|1&1&1|25741868&24165795&16984281&24136356&24610295|||||||-6|-38|9|15|0.00|0.00|0.00|0.00|PIK3CD|VUS|0.03777721||||||,A|missense_variant|MODERATE|PIK3CD|5293|Transcript|NM_001350234.2|protein_coding|24/24||NM_001350234.2:c.3058G>A|NP_001337163.1:p.Glu1020Lys|3373/5515|3058/3132|1020/1043|E/K|Gaa/Aaa|rs397518423&CM067447&COSV63126880|1||1|||EntrezGene||||||||pathogenic|0&0&1|1&1&1|25741868&24165795&16984281&24136356&24610295|||||||-6|-38|9|15|0.00|0.00|0.00|0.00|PIK3CD|VUS|0.13443969||AD&AR||||,A|missense_variant|MODERATE|PIK3CD|5293|Transcript|NM_001350235.1|protein_coding|23/23||NM_001350235.1:c.2974G>A|NP_001337164.1:p.Glu992Lys|3111/5064|2974/3048|992/1015|E/K|Gaa/Aaa|rs397518423&CM067447&COSV63126880|1||1|||EntrezGene||||||||pathogenic|0&0&1|1&1&1|25741868&24165795&16984281&24136356&24610295|||||||-6|-38|9|15|0.00|0.00|0.00|0.00|PIK3CD|VUS|0.13443969||AD&AR||||,A|missense_variant|MODERATE|PIK3CD|5293|Transcript|NM_005026.5|protein_coding|24/24||NM_005026.5:c.3061G>A|NP_005017.3:p.Glu1021Lys|3270/5412|3061/3135|1021/1044|E/K|Gaa/Aaa|rs397518423&CM067447&COSV63126880|1||1||1|EntrezGene|||||0|0.941||pathogenic|0&0&1|1&1&1|25741868&24165795&16984281&24136356&24610295|||||||-6|-38|9|15|0.00|0.00|0.00|0.00|PIK3CD|VUS|0.55297947||AD&AR||||,A|downstream_gene_variant|MODIFIER|CLSTN1|22883|Transcript|NM_014944.4|protein_coding||||||||||rs397518423&CM067447&COSV63126880|1|2049|-1|||EntrezGene||||||||pathogenic|0&0&1|1&1&1|25741868&24165795&16984281&24136356&24610295|||||||-6|-38|9|15|0.00|0.00|0.00|0.00|PIK3CD|VUS|0.03777721||||||;VIPC=LP;VIPL=.;VIPP=filter|vkgl|clinVar|exit_lp");
        expectedList.add("1\t10042538\t.\tC\tT\t.\tPASS\tCSQ=T|missense_variant|MODERATE|NMNAT1|64802|Transcript|NM_001297778.1|protein_coding|5/5||NM_001297778.1:c.619C>T|NP_001284707.1:p.Arg207Trp|778/3796|619/840|207/279|R/W|Cgg/Tgg|rs142968179&CM127756|1||1||1|EntrezGene|||||0.04|0.08||pathogenic||1&1|26103963&22842229&22842230|||||||0|-36|-16|17|0.00|0.00|0.00|0.00|NMNAT1|VUS|0.16572298||AR||1:10042538-10042538|3.88948e-05|0,T|downstream_gene_variant|MODIFIER|NMNAT1|64802|Transcript|NM_001297779.2|protein_coding||||||||||rs142968179&CM127756|1|714|1|||EntrezGene||||||||pathogenic||1&1|26103963&22842229&22842230|||||||0|-36|-16|17|0.00|0.00|0.00|0.00|NMNAT1|VUS|0.033541594||AR||1:10042538-10042538|3.88948e-05|0,T|missense_variant|MODERATE|NMNAT1|64802|Transcript|NM_022787.4|protein_coding|5/5||NM_022787.4:c.619C>T|NP_073624.2:p.Arg207Trp|716/3734|619/840|207/279|R/W|Cgg/Tgg|rs142968179&CM127756|1||1|||EntrezGene|||||0.04|0.08||pathogenic||1&1|26103963&22842229&22842230|||||||0|-36|-16|17|0.00|0.00|0.00|0.00|NMNAT1|VUS|0.14321531||AR||1:10042538-10042538|3.88948e-05|0;VIPC=LP;VIPL=.;VIPP=filter|vkgl|clinVar|exit_lp");
        expectedList.add("1\t11082521\t.\tA\tG\t.\tPASS\tCSQ=G|downstream_gene_variant|MODIFIER|MASP2|10747|Transcript|NM_006610.4|protein_coding||||||||||rs80356734&CM083787|1|4059|-1|||EntrezGene||||||||pathogenic||1&1||||||||-29|15|-39|-4|0.00|0.00|0.00|0.00|TARDBP|VUS|0.0571503||AR||||,G|missense_variant|MODERATE|TARDBP|23435|Transcript|NM_007375.4|protein_coding|6/6||NM_007375.4:c.1055A>G|NP_031401.1:p.Asn352Ser|1157/4185|1055/1245|352/414|N/S|aAt/aGt|rs80356734&CM083787|1||1||1|EntrezGene|||||0.57|0||pathogenic||1&1||||||||-29|15|-39|-4|0.00|0.00|0.00|0.00|TARDBP|VUS|0.023780355||AD||||;VIPC=LP;VIPL=.;VIPP=filter|vkgl|clinVar|exit_lp");

        List<String> listVariants = Files.readAllLines(Path.of(fileLocation));
        String fileLocationClinvar = fileLocation.replace(".vcf", "");
        String pathClinvarMatches = fileLocationClinvar + "_ClinvarVariants.vcf";

        Files.delete(Path.of(fileLocation));
        Files.delete(Path.of(pathClinvarMatches));
        assertEquals(expectedList, listVariants);
    }

    @Test
    void matchwithClinvar_fakeInput() throws IOException {
        VariantMatcher variantMatcher = new VariantMatcher("/Users/jonathan/Documents/GitHub/GenDecS-tools/data/clinvar_20220205_filtered_ZEROSTAR.vcf", "/Users/jonathan/Documents/GitHub/GenDecS-tools/testdata/pathogenic.vcf", null);
        String fileLocation = variantMatcher.matchWithClinvar();
        List<String> expectedList = new ArrayList<>();
        expectedList.add("# This is a test vcf file with pathogenic variants");
        expectedList.add("#CHROM\tPOS\tID\tREF\tALT\tQUAL\tFILTER\tINFO");
        expectedList.add("1\t879375\t950448\tC\tT\t.\t.\tALLELEID=929884;CLNDISDB=MedGen:CN517202;CLNDN=not_provided;CLNHGVS=NC_000001.10:g.879375C>T;CLNREVSTAT=criteria_provided,_single_submitter;CLNSIG=Pathogenic;CLNVC=single_nucleotide_variant;CLNVCSO=SO:0001483;GENEINFO=SAMD11:148398;MC=SO:0001587|nonsense;ORIGIN=1;RS=761448939");
        List<String> listVariants = Files.readAllLines(Path.of(fileLocation));
        String fileLocationClinvar = fileLocation.replace(".vcf", "");
        String pathClinvarMatches = fileLocationClinvar + "_ClinvarVariants.vcf";

        Files.delete(Path.of(fileLocation));
        Files.delete(Path.of(pathClinvarMatches));
        assertEquals(expectedList, listVariants);
    }

    @Test
    void matchwithClinvar_fakeInput_WithOutputDir() throws IOException {
        VariantMatcher variantMatcher = new VariantMatcher("/Users/jonathan/Documents/GitHub/GenDecS-tools/data/clinvar_20220205_filtered_ZEROSTAR.vcf", "/Users/jonathan/Documents/GitHub/GenDecS-tools/testdata/pathogenic.vcf", "/Users/jonathan/Documents/GitHub/GenDecS-tools/data/");
        String fileLocation = variantMatcher.matchWithClinvar();
        List<String> expectedList = new ArrayList<>();
        expectedList.add("# This is a test vcf file with pathogenic variants");
        expectedList.add("#CHROM\tPOS\tID\tREF\tALT\tQUAL\tFILTER\tINFO");
        expectedList.add("1\t879375\t950448\tC\tT\t.\t.\tALLELEID=929884;CLNDISDB=MedGen:CN517202;CLNDN=not_provided;CLNHGVS=NC_000001.10:g.879375C>T;CLNREVSTAT=criteria_provided,_single_submitter;CLNSIG=Pathogenic;CLNVC=single_nucleotide_variant;CLNVCSO=SO:0001483;GENEINFO=SAMD11:148398;MC=SO:0001587|nonsense;ORIGIN=1;RS=761448939");
        List<String> listVariants = Files.readAllLines(Path.of(fileLocation));
        String fileLocationClinvar = fileLocation.replace(".vcf", "");
        String pathClinvarMatches = fileLocationClinvar + "_ClinvarVariants.vcf";

        Files.delete(Path.of(fileLocation));
        Files.delete(Path.of(pathClinvarMatches));
        assertEquals(expectedList, listVariants);
    }

    @Test
    void matchwithClinvar_fakeInput_WithDirNoFwSlash() throws IOException {
        VariantMatcher variantMatcher = new VariantMatcher("/Users/jonathan/Documents/GitHub/GenDecS-tools/data/clinvar_20220205_filtered_ZEROSTAR.vcf", "/Users/jonathan/Documents/GitHub/GenDecS-tools/testdata/pathogenic.vcf", "/Users/jonathan/Documents/GitHub/GenDecS-tools/data");
        String fileLocation = variantMatcher.matchWithClinvar();
        List<String> expectedList = new ArrayList<>();
        expectedList.add("# This is a test vcf file with pathogenic variants");
        expectedList.add("#CHROM\tPOS\tID\tREF\tALT\tQUAL\tFILTER\tINFO");
        expectedList.add("1\t879375\t950448\tC\tT\t.\t.\tALLELEID=929884;CLNDISDB=MedGen:CN517202;CLNDN=not_provided;CLNHGVS=NC_000001.10:g.879375C>T;CLNREVSTAT=criteria_provided,_single_submitter;CLNSIG=Pathogenic;CLNVC=single_nucleotide_variant;CLNVCSO=SO:0001483;GENEINFO=SAMD11:148398;MC=SO:0001587|nonsense;ORIGIN=1;RS=761448939");
        List<String> listVariants = Files.readAllLines(Path.of(fileLocation));
        String fileLocationClinvar = fileLocation.replace(".vcf", "");
        String pathClinvarMatches = fileLocationClinvar + "_ClinvarVariants.vcf";

        Files.delete(Path.of(fileLocation));
        Files.delete(Path.of(pathClinvarMatches));
        assertEquals(expectedList, listVariants);
    }
}