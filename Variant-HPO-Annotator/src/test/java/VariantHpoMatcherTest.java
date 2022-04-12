import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VariantHpoMatcherTest {

    @Test
    void getGene_VariantInput() {
        VariantHpoMatcher variantHpoMatcher = new VariantHpoMatcher();
        String variant = "21\t34927287\t.\tCAGTT\tC\t.\tPASS\tCSQ=-|frameshift_variant|HIGH|SON|6651|Transcript|NM_001291411.2|protein_coding|3/5||NM_001291411.2:c.5753_5756del|NP_001278340.2:p.Val1918GlufsTer87|5808-5811/7897|5753-5756/6327|1918-1919/2108|VR/X|gTTAGa/ga|rs886039773|1||1|||EntrezGene|||||||2|likely_pathogenic&pathogenic||1|25590979&27256762&27545676&27545680||||||||||||||||VUS|0.98468506|1|AD||||,-|intron_variant|MODIFIER|SON|6651|Transcript|NM_001291412.3|protein_coding||2/10|NM_001291412.3:c.245-2172_245-2169del|||||||rs886039773|1||1|||EntrezGene|||||||2|likely_pathogenic&pathogenic||1|25590979&27256762&27545676&27545680||||||||||||||||VUS|0.0276116|1|AD||||,-|frameshift_variant|HIGH|SON|6651|Transcript|NM_032195.3|protein_coding|3/7||NM_032195.3:c.5753_5756del|NP_115571.3:p.Val1918GlufsTer87|5808-5811/7503|5753-5756/6912|1918-1919/2303|VR/X|gTTAGa/ga|rs886039773|1||1|||EntrezGene|||||||2|likely_pathogenic&pathogenic||1|25590979&27256762&27545676&27545680||||||||||||||||VUS|0.98468506|1|AD||||,-|frameshift_variant|HIGH|SON|6651|Transcript|NM_138927.4|protein_coding|3/12||NM_138927.4:c.5753_5756del|NP_620305.3:p.Val1918GlufsTer87|5808-5811/8393|5753-5756/7281|1918-1919/2426|VR/X|gTTAGa/ga|rs886039773|1||1||1|EntrezGene|||||||2|likely_pathogenic&pathogenic||1|25590979&27256762&27545676&27545680||||||||||||||||VUS|0.98468506|1|AD||||,-|non_coding_transcript_exon_variant|MODIFIER|SON|6651|Transcript|NR_103797.2|misc_RNA|3/13||NR_103797.2:n.5808_5811del||5808-5811/8420|||||rs886039773|1||1|||EntrezGene|||||||2|likely_pathogenic&pathogenic||1|25590979&27256762&27545676&27545680||||||||||||||||VUS|0.11383859|1|AD||||,-|downstream_gene_variant|MODIFIER|MIR6501|102465248|Transcript|NR_106756.1|miRNA||||||||||rs886039773|1|4254|1|||EntrezGene||||||||likely_pathogenic&pathogenic||1|25590979&27256762&27545676&27545680||||||||||||||||VUS|0.08589135||||||,-|regulatory_region_variant|MODIFIER|||RegulatoryFeature|ENSR00001953475|enhancer||||||||||rs886039773|1|||||||||||||likely_pathogenic&pathogenic||1|25590979&27256762&27545676&27545680|||||||||||||||||||||||;VIPC=LP;VIPL=.;VIPP=filter|vkgl|clinVar|exit_lp\n";
        String gene = variantHpoMatcher.getGene(variant);
        assertEquals("SON", gene);
    }

    @Test
    void getGene_EmptyStringInput() {
        VariantHpoMatcher variantHpoMatcher = new VariantHpoMatcher();
        String variant = "";
        String gene = variantHpoMatcher.getGene(variant);
        assertEquals("", gene);
    }

    @Test
    void getHpo_GeneInput() {
        VariantHpoMatcher variantHpoMatcher = new VariantHpoMatcher();
        String gene = "PADI3";
        ArrayList<String> hpoTermsExcp = new ArrayList<>(List.of("Pili canaliculi", "Uncombable hair", "Dry hair", "Autosomal recessive inheritance", "Woolly hair", "Patchy alopecia", "White hair", "Coarse hair", "Trichodysplasia"));
        ArrayList<String> diseaseIdsExcp = new ArrayList<>(List.of("OMIM:191480", "OMIM:191480", "OMIM:191480", "OMIM:191480", "ORPHA:1410", "ORPHA:1410", "ORPHA:1410", "ORPHA:1410", "ORPHA:1410"));
        HashMap<String, ArrayList<String>> termsAndDiseasesExcp = new HashMap<>();
        termsAndDiseasesExcp.put("hpoTerms", hpoTermsExcp);
        termsAndDiseasesExcp.put("diseaseIds", diseaseIdsExcp);
        HashMap<String, ArrayList<String>> termsAndDiseases = variantHpoMatcher.getHpo(gene, "/Users/jonathan/Documents/GitHub/GenDecS-tools/data/genes_to_phenotype.txt");
        assertEquals(termsAndDiseasesExcp, termsAndDiseases);
    }


}