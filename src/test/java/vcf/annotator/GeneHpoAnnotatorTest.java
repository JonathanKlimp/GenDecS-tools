package vcf.annotator;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GeneHpoAnnotatorTest {
    @Test
    void annotateVcfWithHpo_NormalInput() throws IOException {
        GeneHpoAnnotator geneHpoAnnotator = new GeneHpoAnnotator("testdata/oneVariant.vcf");
        String fileLocation = geneHpoAnnotator.annotateVcfWithHpo();
        List<String> annotatedLines = Files.readAllLines(Path.of(fileLocation));
        List<String> expectedList = new ArrayList<>();
        expectedList.add("# This is a test vcf file with 1 variant.");
        expectedList.add("#CHROM\tPOS\tID\tREF\tALT\tQUAL\tFILTER\tINFO");
        expectedList.add("1\t17588689\t.\tT\tA\t.\tPASS\tCSQ=A|missense_variant|MODERATE|PADI3|51702|Transcript|NM_016233.2|protein_coding|3/16||NM_016233.2:c.335T>A|NP_057317.2:p.Leu112His|375/3189|335/1995|112/664|L/H|cTc/cAc|rs142129409&CM1615418|1||1||1|EntrezGene|||||0|0.993||pathogenic||1&1|27866708&28550590|||||||-38|17|11|15|0.00|0.00|0.00|0.00|PADI3|VUS|0.8706367||AR||1:17588689-17588689|0.00452301|1;VIPC=LP;VIPL=.;VIPP=filter|vkgl|clinVar|exit_lp\t[Pili canaliculi, Uncombable hair, Dry hair, Autosomal recessive inheritance, Woolly hair, Patchy alopecia, White hair, Coarse hair, Trichodysplasia]");

        Files.delete(Path.of(fileLocation));
        assertEquals(expectedList, annotatedLines);
    }
}