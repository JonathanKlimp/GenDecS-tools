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
    private final File clinvarFile;
    private final String outputLocation;
    private final String outputLocationClin;

    private static final Logger logger = LoggerFactory.getLogger(VariantMatcher.class);

    public VariantMatcher(String clinvarLocation, String vcfDataLocation, String outputLocation) {
        if (outputLocation == null) {
            String inputFileLoc = vcfDataLocation.replace(".vcf", "");
            String pathName = inputFileLoc + "_clinvar_matched.vcf";
            String pathNameClinvar = inputFileLoc + "_clinvar_matched_ClinvarVariants.vcf";
            this.outputLocation = pathName;
            this.outputLocationClin = pathNameClinvar;
        } else {
            if (!outputLocation.endsWith("/")) {
                throw new IllegalArgumentException("Given output location is not a directory: " + outputLocation);
            } else {
                File vcfFile = new File(vcfDataLocation);
                String vcfFileName = vcfFile.getName().replace(".vcf", "");
                this.outputLocation = outputLocation + vcfFileName + "_clinvar_matched.vcf";
                this.outputLocationClin = outputLocation + vcfFileName + "_clinvar_matched_ClinvarVariants.vcf";
            }
        }
        this.vcfFile = vcfDataLocation;
        this.clinvarFile = new File(clinvarLocation);
    }

    /**
     * Method matchWithClinvar reads given vcf file with variants and builds regex for each line.
     * Which is then used to match this with variants in the clinvar vcf file. The found matches are
     * written to a new file.
     *
     * @return String with the location of the new file.
     */
    public String matchWithClinvar() {
        File outputFile = new File(this.outputLocation);
        Map<String, Pattern> stringsToFind = new HashMap<>();
        try {
            if (outputFile.createNewFile()) {
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
            } else {
                return this.outputLocation;
            }
        } catch(IOException e){
            e.printStackTrace();
        }
        return "";
    }

    private String getMatchesClinvar(Map<String, Pattern> stringsToFind, ArrayList<String> header) throws IOException {
        Scanner reader = new Scanner(this.clinvarFile);

        File resultFileClinvar = new File(this.outputLocationClin);
        File resultFile = new File(this.outputLocation);
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
