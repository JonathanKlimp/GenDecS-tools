import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

public class VariantMatcher {
    private final String vcfFile;
    private final File clinvarFile;
    private String outputLocation;

    private static final Logger logger = LogManager.getLogger(VariantMatcher.class);

    /**
     *
     * @param clinvarLocation Location of the ClinVar file
     * @param vcfDataLocation Location of the vcf data file
     * @param outputLocation Location of the desired output directory.
     *          If null the directory of the vcfDataLocation is used.
     */
    public VariantMatcher(String clinvarLocation, String vcfDataLocation, String outputLocation) {
        createOutputLocation(vcfDataLocation, outputLocation);
        this.vcfFile = vcfDataLocation;
        this.clinvarFile = new File(clinvarLocation);
    }

    private void createOutputLocation(String vcfDataLocation, String outputLocation) {
        if (outputLocation == null) {
            String inputFileLoc = vcfDataLocation.replace(".vcf", "");
            this.outputLocation = inputFileLoc + "_clinvar_matched.vcf";
        } else {
                File vcfFile = new File(vcfDataLocation);
                String vcfFileName = vcfFile.getName().replace(".vcf", "");
                this.outputLocation = outputLocation + vcfFileName + "_clinvar_matched.vcf";
        }
        logger.debug("output location for the clinvar matched variants: " + this.outputLocation);
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
                logger.info("Building regex of the variants in: " + vcfFile.getAbsolutePath());
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
                    logger.trace("Created the following pattern: " + pattern + "for this line: " + currentLine);
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

        File resultFile = new File(this.outputLocation);
        BufferedWriter writerResult = new BufferedWriter(new FileWriter(resultFile));

        for(String headerLine : header) {
            writerResult.write(headerLine + System.getProperty("line.separator"));
        }

        while (reader.hasNextLine()) {
            String currentLine = reader.nextLine();
            for (Pattern stringToFind : stringsToFind.values()) {
                if (currentLine.matches(String.valueOf(stringToFind))) {
                    logger.trace("Current variant line matched with ClinVar: " + currentLine);
                    writerResult.write(
                            getKeyFromValue(stringsToFind, stringToFind)
                                    + System.getProperty("line.separator"));

                }
            }
        }
        reader.close();
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
