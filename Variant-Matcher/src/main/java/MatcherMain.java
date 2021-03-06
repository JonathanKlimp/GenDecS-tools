import org.apache.commons.cli.*;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.File;
import java.io.IOException;


public class MatcherMain {
    private static final Logger logger = LogManager.getLogger(MatcherMain.class);

    /**
     * Main method for Variant-Matcher.
     * @param args expected arguments are [location of clinvar] [location of vcf data]
     *             Optional arguments [--output to give an output directory | --help]
     * @throws IllegalArgumentException is the given --output is not a directory.
     */
    public static void main(String[] args) {
        CommandLineParser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("o", "output", true, "output directory");
        options.addOption("c", "clinvar", true, "location of the ClinVar file");
        options.addOption("d", "data", true, "location of the vcf data");
        options.addOption("h", "help", false, "print the help of the program");


        try {
            CommandLine commandLine = parser.parse(options, args);
            if (commandLine.hasOption("help")) {
                printHelp(options);
            }
            String clinvarLocation = commandLine.getOptionValue("clinvar");
            String vcfDataLocation = commandLine.getOptionValue("data");
            String fileLocation;
            if (commandLine.hasOption("output")) {
                String outputLocation = commandLine.getOptionValue("output");
                File outputLoc = new File(outputLocation);
                if (!outputLoc.isDirectory()) {
                    throw new IllegalArgumentException("Given output location is not a directory: " + outputLocation);
                }
                if(!outputLocation.endsWith("/")) {
                    outputLocation = commandLine.getOptionValue("output") + "/";
                }
                VariantMatcher variantMatcher = new VariantMatcher(clinvarLocation, vcfDataLocation, outputLocation);
                fileLocation = variantMatcher.matchWithClinvar();
            } else {
                VariantMatcher variantMatcher = new VariantMatcher(clinvarLocation, vcfDataLocation, null);
                fileLocation = variantMatcher.matchWithClinvar();
            }
            File sortScript = new File("sortVcf.sh");
            System.out.println(sortScript.getAbsolutePath());
            sortFile(fileLocation, sortScript.getAbsolutePath());

            logger.info("Created the filtered file at the following location: " + fileLocation);
            System.exit(0);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private static void sortFile(String resultFileLoc, String scriptLoc) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        String scriptArgument = String.format("sh %s -f %s",scriptLoc, resultFileLoc);
        processBuilder.command(scriptArgument);
        try {
            Process process = Runtime.getRuntime().exec(scriptArgument);
            int exitCode = process.waitFor();
            logger.info("\nExited vcf sorter with error code : " + exitCode);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void printHelp(Options options) {
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp("help", options);
        System.out.println("program usage: " +
                "java -jar Variant-Matcher-[version] [options] [--cinvar location of filtered clinvar] [--data location of vcf data]");
        System.exit(0);
    }
}
