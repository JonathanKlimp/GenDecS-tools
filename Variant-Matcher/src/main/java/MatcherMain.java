import org.apache.commons.cli.*;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.File;


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
        options.addOption("h", "help", false, "print the help of the program");

        try {
            CommandLine commandLine = parser.parse(options, args);
            if (commandLine.hasOption("help")) {
                printHelp(options);
            }
            String clinvarLocation = commandLine.getArgList().get(0);
            String vcfDataLocation = commandLine.getArgList().get(1);
            String fileLocation;
            if (commandLine.hasOption("output")) {
                File outputLoc = new File(commandLine.getOptionValue("output"));
                if (!outputLoc.isDirectory()) {
                    throw new IllegalArgumentException("Given output location is not a directory: " + commandLine.getOptionValue("output"));
                }
                VariantMatcher variantMatcher = new VariantMatcher(clinvarLocation, vcfDataLocation, commandLine.getOptionValue("output"));
                fileLocation = variantMatcher.matchWithClinvar();
            } else {
                VariantMatcher variantMatcher = new VariantMatcher(clinvarLocation, vcfDataLocation, null);
                fileLocation = variantMatcher.matchWithClinvar();
            }
            logger.info("Created the filtered file at the following location: " + fileLocation);
            System.exit(0);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private static void printHelp(Options options) {
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp("help", options);
        System.out.println("program usage: " +
                "java -jar Variant-Matcher-[version] [options] [location of filtered clinvar] [location of vcf data]");
        System.exit(0);
    }
}
