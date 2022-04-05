package clinvar.filter;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClinVarFilterTest {

    @Test
    void removeStatus_noPathogenicVariants() throws IOException {
        ClinVarFilter clinVarFilter = new ClinVarFilter(StarRating.ZEROSTAR, "testdata/noPathogenic.vcf");
        String clinvarLoc = clinVarFilter.removeStatus();
        List<String> listClinvar = Files.readAllLines(Path.of(clinvarLoc));
        List<String> expectedList = new ArrayList<>();
        expectedList.add("# This is a test vcf file with no pathogenic variants");
        expectedList.add("#CHROM\tPOS\tID\tREF\tALT\tQUAL\tFILTER\tINFO");
        Files.delete(Path.of(clinvarLoc));
        assertEquals(expectedList, listClinvar);
    }

    @Test
    void removeSatus_pathogenicVariants() throws IOException {
        ClinVarFilter clinVarFilter = new ClinVarFilter(StarRating.ZEROSTAR, "testdata/pathogenic.vcf");
        String clinvarLoc = clinVarFilter.removeStatus();
        List<String> listClinvar = Files.readAllLines(Path.of(clinvarLoc));
        List<String> expectedList = new ArrayList<>();
        expectedList.add("# This is a test vcf file with pathogenic variants");
        expectedList.add("#CHROM\tPOS\tID\tREF\tALT\tQUAL\tFILTER\tINFO");
        expectedList.add("1\t879375\t950448\tC\tT\t.\t.\tALLELEID=929884;CLNDISDB=MedGen:CN517202;CLNDN=not_provided;CLNHGVS=NC_000001.10:g.879375C>T;CLNREVSTAT=criteria_provided,_single_submitter;CLNSIG=Pathogenic;CLNVC=single_nucleotide_variant;CLNVCSO=SO:0001483;GENEINFO=SAMD11:148398;MC=SO:0001587|nonsense;ORIGIN=1;RS=761448939");
        expectedList.add("11\t6099\t1952\tCAG\tC\t.\t.\tALLELEID=16991;CLNDISDB=MONDO:MONDO:0012490,MedGen:C4041558,OMIM:610427|MedGen:CN517202;CLNDN=Congenital_stationary_night_blindness,_type_2B|not_provided;CLNHGVS=NC_000011.9:g.67226100AG[1];CLNREVSTAT=criteria_provided,_multiple_submitters,_no_conflicts;CLNSIG=Pathogenic/Likely_pathogenic;CLNVC=Microsatellite;CLNVCSO=SO:0000289;CLNVI=OMIM_Allelic_Variant:608965.0001|Invitae:2343735;GENEINFO=CABP4:57010;ORIGIN=1;RS=786205249");
        expectedList.add("11\t929\t1076898\tG\tT\t.\t.\tALLELEID=1062468;CLNDISDB=MedGen:CN517202;CLNDN=not_provided;CLNHGVS=NC_000011.9:g.67225929G>T;CLNREVSTAT=criteria_provided,_single_submitter;CLNSIG=Pathogenic;CLNVC=single_nucleotide_variant;CLNVCSO=SO:0001483;GENEINFO=CABP4:57010;MC=SO:0001587|nonsense,SO:0001619|non-coding_transcript_variant;ORIGIN=1");
        Files.delete(Path.of(clinvarLoc));
        assertEquals(expectedList, listClinvar);
    }

    @Test
    void removeSatus_starRating() throws IOException {
        ClinVarFilter clinVarFilter = new ClinVarFilter(StarRating.ZEROSTAR, "testdata/varyingStarRating.vcf");
        String clinvarLoc = clinVarFilter.removeStatus();
        List<String> listClinvar = Files.readAllLines(Path.of(clinvarLoc));
        List<String> expectedList = new ArrayList<>();
        expectedList.add("# This is a test vcf file with variants of varying star ratings");
        expectedList.add("#CHROM\tPOS\tID\tREF\tALT\tQUAL\tFILTER\tINFO");
        expectedList.add("1\t957605\t243036\tG\tA\t.\t.\tAF_EXAC=0.00001;ALLELEID=244110;CLNDISDB=MONDO:MONDO:0014052,MedGen:C3808739,OMIM:615120|MONDO:MONDO:0018940,MeSH:D020294,MedGen:C0751882,OMIM:PS601462,Orphanet:ORPHA590;CLNDN=Myasthenic_syndrome,_congenital,_8|Congenital_myasthenic_syndrome;CLNHGVS=NC_000001.10:g.957605G>A;CLNREVSTAT=criteria_provided,_single_submitter;CLNSIG=Likely_pathogenic;CLNVC=single_nucleotide_variant;CLNVCSO=SO:0001483;GENEINFO=AGRN:375790;MC=SO:0001583|missense_variant;ORIGIN=1;RS=756623659");
        expectedList.add("1\t11169679\t1296984\tG\tT\t.\t.\tALLELEID=1286774;CLNDISDB=MONDO:MONDO:0016054,MedGen:C0266449,Orphanet:ORPHA199633;CLNDN=Brain_malformation;CLNHGVS=NC_000001.10:g.11169679G>T;CLNREVSTAT=reviewed_by_expert_panel;CLNSIG=Uncertain_significance;CLNVC=single_nucleotide_variant;CLNVCSO=SO:0001483;GENEINFO=MTOR:2475;MC=SO:0001627|intron_variant;ORIGIN=1");
        Files.delete(Path.of(clinvarLoc));
        assertEquals(expectedList, listClinvar);
    }

    @Test
    void removeSatus_starRatingThreeStar() throws IOException {
        ClinVarFilter clinVarFilter = new ClinVarFilter(StarRating.TWOSTAR, "testdata/varyingStarRating.vcf");
        String clinvarLoc = clinVarFilter.removeStatus();
        List<String> listClinvar = Files.readAllLines(Path.of(clinvarLoc));
        List<String> expectedList = new ArrayList<>();
        expectedList.add("# This is a test vcf file with variants of varying star ratings");
        expectedList.add("#CHROM\tPOS\tID\tREF\tALT\tQUAL\tFILTER\tINFO");
        expectedList.add("1\t11169679\t1296984\tG\tT\t.\t.\tALLELEID=1286774;CLNDISDB=MONDO:MONDO:0016054,MedGen:C0266449,Orphanet:ORPHA199633;CLNDN=Brain_malformation;CLNHGVS=NC_000001.10:g.11169679G>T;CLNREVSTAT=reviewed_by_expert_panel;CLNSIG=Uncertain_significance;CLNVC=single_nucleotide_variant;CLNVCSO=SO:0001483;GENEINFO=MTOR:2475;MC=SO:0001627|intron_variant;ORIGIN=1");
        Files.delete(Path.of(clinvarLoc));
        assertEquals(expectedList, listClinvar);
    }
}