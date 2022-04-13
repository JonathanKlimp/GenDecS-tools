import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class ClinVarFilter {
    private final StarRating starRating;
    private final File clinvarFile;
    private final String outputLocation;
    private static final Logger logger = LoggerFactory.getLogger(ClinVarFilter.class);

    public ClinVarFilter(StarRating starRating, String clinvarLocation, String outputLocation) {
        this.starRating = starRating;
        this.clinvarFile = new File(clinvarLocation);

        if (outputLocation == null) {
            String clinvarLoc = clinvarLocation.replace(".vcf", "");
            this.outputLocation = String.format(clinvarLoc + "_filtered_%s.vcf", starRating);
        } else {
            if (!outputLocation.endsWith("/")) {
                throw new IllegalArgumentException("Given output location is not a directory: " + outputLocation);
            } else {
                File clinvarFile = new File(clinvarLocation);
                String clinvarName = clinvarFile.getName().replace(".vcf", "");
                this.outputLocation = String.format(outputLocation + clinvarName + "_filtered_%s.vcf", starRating);
            }
        }
    }

    private ArrayList<String> getRating(StarRating starRating) {
        switch (starRating) {
            case ZEROSTAR -> {
                return new ArrayList<>(List.of("no_assertion_provided", "no_assertion_criteria_provided"));
            }
            case ONESTAR -> {
                return new ArrayList<>(
                        List.of(
                                "criteria_provided,_single_submitter",
                                "criteria_provided,_conflicting_interpretations",
                                "no_assertion_provided",
                                "no_assertion_criteria_provided"));
            }
            case TWOSTAR -> {
                return new ArrayList<>(
                        List.of(
                                "criteria_provided,_multiple_submitters,_no_conflicts",
                                "criteria_provided,_single_submitter",
                                "criteria_provided,_conflicting_interpretations",
                                "no_assertion_provided",
                                "no_assertion_criteria_provided"));
            }
            case THREESTAR -> {
                return new ArrayList<>(
                        List.of(
                                "reviewed_by_expert_panel",
                                "criteria_provided,_multiple_submitters,_no_conflicts",
                                "criteria_provided,_single_submitter",
                                "criteria_provided,_conflicting_interpretations",
                                "no_assertion_provided",
                                "no_assertion_criteria_provided"));
            }
        }
        return null;
    }

    /**
     * Method removeStatus checks if variant lines are the correct given star rating and
     * if it's pathogenic. If these checks result in true the variant line is written to a
     * new file.
     *
     * @return String with the location of the filtered file.
     */
    public String removeStatus(){
        File filteredClinVar = new File(this.outputLocation);
        try {
            if (filteredClinVar.createNewFile()) {
                logger.debug("Creating file: " + this.outputLocation);

                BufferedWriter writer = new BufferedWriter(new FileWriter(this.outputLocation));

                Scanner reader = new Scanner(this.clinvarFile);
                logger.debug("Removing " + this.starRating + "from: " + this.clinvarFile);
                while (reader.hasNextLine()) {
                    String currentLine = reader.nextLine();
                    if (stringContainsItemFromList(
                            currentLine, Objects.requireNonNull(getRating(this.starRating)))) {
                        continue;
                    }
                    if (currentLine.startsWith("#")) {
                        writer.write(currentLine + System.getProperty("line.separator"));
                    } else if (isPathogenic(currentLine)) {
                        writer.write(currentLine + System.getProperty("line.separator"));
                    }
                }
                reader.close();
                writer.close();
            }
            return this.outputLocation;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static boolean stringContainsItemFromList(String inputString, ArrayList<String> items) {
        return items.stream().anyMatch(inputString::contains);
    }

    private static boolean isPathogenic(String variant) {
        ArrayList<String> clinSig =
                new ArrayList<>(List.of("likely_pathogenic", "pathogenic", "pathogenic/likely_pathogenic",
                        "uncertain_significance"));
        String[] splittedLine = variant.split("\t");

        String[] infoString = splittedLine[7].split(";");
        for (String i : infoString) {
            if (i.contains("CLNSIG")) {
                if (clinSig.contains(i.split("=")[1].toLowerCase())) {
                    return true;
                }
            }
        }
        return false;
    }
}

