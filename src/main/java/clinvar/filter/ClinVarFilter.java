package clinvar.filter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClinVarFilter {
    private final StarRating starRating;
    private final String clinvarLocation;
    private static final Logger logger = LoggerFactory.getLogger(ClinVarFilter.class);

    public ClinVarFilter(StarRating starRating, String clinvarLocation) {
        this.starRating = starRating;
        this.clinvarLocation = clinvarLocation;
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
     * Method removeStatus checks if variant lines are the correct given starrating and
     * if its pathogenic. If these checks result in true the variant line is written to a
     * new file.
     *
     * @return String with the location of the filtered file.
     */
    public String removeStatus() {
        String clinvarLoc = clinvarLocation.replace(".vcf", "");
        String pathName = String.format(clinvarLoc + "_filtered_%s.vcf", this.starRating);
        Path path = Paths.get(pathName);
        if (path.toFile().isFile()) {
            return pathName;
        } else {
            logger.debug("Creating file: " + pathName);
            File filteredClinVar = new File(pathName);
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(filteredClinVar));

                File fileObject = new File(clinvarLocation);
                Scanner reader = new Scanner(fileObject);
                logger.debug("Removing " + this.starRating + "from: " + clinvarLocation);
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
                return pathName;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }
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

