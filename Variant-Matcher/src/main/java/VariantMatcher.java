import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

public class VariantMatcher {
    private final String vcfFile;
    private final String clinvarFile;

    private static final Logger logger = LoggerFactory.getLogger(VariantMatcher.class);

    public VariantMatcher(String clinvarLocation, String vcfDataLocation) {
        this.vcfFile = vcfDataLocation;
        this.clinvarFile = clinvarLocation;
    }

    /**
     * Method matchWithClinvar reads given vcf file with variants and builds regex for each line.
     * Which is then used to match this with variants in the clinvar vcf file. The found matches are
     * written to a new file.
     *
     * @return String with the location of the new file.
     */
    public String matchWithClinvar() {
        Map<String, Pattern> stringsToFind = new HashMap<>();
        try {
            ArrayList<String> header = new ArrayList<>();
            File vcfFile = new File(this.vcfFile);
            Scanner reader = new Scanner(vcfFile);
            logger.info("Building regex of the variants in: " + vcfFile);
            while (reader.hasNextLine()) {
                String currentLine = reader.nextLine();
                if (currentLine.startsWith("#")) {
                    header.add(currentLine);
                    continue;
                }
                String[] splittedData = currentLine.split("\t");
                String chromosome = splittedData[0];
                String position = splittedData[1];
                String ref = splittedData[3];
                String alt = splittedData[4];
                Pattern pattern =
                        Pattern.compile(
                                String.format("%s\\t%s\\t[0-9]+\\t%s\\t%s.+", chromosome, position, ref, alt));
                stringsToFind.put(currentLine, pattern);
            }
            reader.close();

            return getMatchesClinvar(stringsToFind, header);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getMatchesClinvar(Map<String, Pattern> stringsToFind, ArrayList<String> header) throws IOException {
        File clinvarFile = new File(this.clinvarFile);
        Scanner reader = new Scanner(clinvarFile);
        String inputFileLoc = vcfFile.replace(".vcf", "");
        String pathName = inputFileLoc + "_clinvar_matched.vcf";
        String pathNameClinvar = inputFileLoc + "_clinvar_matched_ClinvarVariants.vcf";

        File resultFileClinvar = new File(pathNameClinvar);
        File resultFile = new File(pathName);
        BufferedWriter writerResult = new BufferedWriter(new FileWriter(resultFile));
        BufferedWriter writerClinvar = new BufferedWriter(new FileWriter(resultFileClinvar));

        for(String headerLine : header) {
            writerResult.write(headerLine + System.getProperty("line.separator"));
        }

        while (reader.hasNextLine()) {
            String currentLine = reader.nextLine();
            if (currentLine.startsWith("#")){
                writerClinvar.write(currentLine + System.getProperty("line.separator"));
            }
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
        return resultFile.toString();
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
