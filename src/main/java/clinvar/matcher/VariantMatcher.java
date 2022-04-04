package clinvar.matcher;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VariantMatcher {
    private final File vcfFile;
    private final File clinvarFile;

    private static final Logger logger = LoggerFactory.getLogger(VariantMatcher.class);

    public VariantMatcher(String clinvarLocation, String vcfDataLocation) {
        vcfFile = new File(vcfDataLocation);
        clinvarFile = new File(clinvarLocation);
    }

    /**
     * Method matchWithClinvar reads given vcf file with variants and builds regex for each line.
     * Which is then used to match this with variants in the clinvar vcf file. The found matches are
     * filtered on the variant being pathogenic or not. The remaining variants are added to the
     * Variants class.
     */
    public String matchWithClinvar() {
        Map<String, Pattern> stringsToFind = new HashMap<>();
        try {
            Scanner reader = new Scanner(vcfFile);
            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                if (data.startsWith("#")) {
                    continue;
                }
                String[] splittedData = data.split("\t");
                String chromosome = splittedData[0];
                String position = splittedData[1];
                String ref = splittedData[3];
                String alt = splittedData[4];
                Pattern pattern =
                        Pattern.compile(
                                String.format("%s\\t%s\\t[0-9]+\\t%s\\t%s.+", chromosome, position, ref, alt));
                stringsToFind.put(data, pattern);
            }
            reader.close();

            return getMatchesClinvar(stringsToFind);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getMatchesClinvar(Map<String, Pattern> stringsToFind) throws IOException {
        Scanner reader = new Scanner(clinvarFile);

        File resultFileClinvar = new File("data/result_matches_clinvar.vcf");
        File resultFileResult = new File("data/result_matches.vcf");
        BufferedWriter writerResult = new BufferedWriter(new FileWriter(resultFileResult));
        BufferedWriter writerClinvar = new BufferedWriter(new FileWriter(resultFileClinvar));

        VcfFile.writeHeader(writerClinvar, "clinvar");
        VcfFile.writeHeader(writerResult, "result");

        while (reader.hasNextLine()) {
            String currentLine = reader.nextLine();
            for (Pattern stringToFind : stringsToFind.values()) {
                if (currentLine.matches(String.valueOf(stringToFind))) {
                    writerResult.write(
                            getKeyFromValue(stringsToFind, stringToFind)
                                    + System.getProperty("line.separator"));
                    writerClinvar.write(
                            currentLine + System.getProperty("line.separator"));
                }
            }
        }
        reader.close();
        writerClinvar.close();
        writerResult.close();
        return resultFileResult.toString();
    }

    private static String getKeyFromValue(Map<String, Pattern> map, Pattern value) {
        return map.entrySet().stream()
                .filter(entry -> Objects.equals(entry.getValue(), value))
                .map(Map.Entry::getKey)
                .findFirst()
                .map(Object::toString)
                .orElse("");
    }
}
