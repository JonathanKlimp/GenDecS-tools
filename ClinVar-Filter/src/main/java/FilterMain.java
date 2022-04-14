import org.apache.commons.cli.*;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.File;

public class FilterMain {
    private static final Logger logger = LogManager.getLogger(FilterMain.class);

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
            String fileLoc;
            if (commandLine.hasOption("output")) {
                String outputLocation = commandLine.getOptionValue("output");
                File outputLoc = new File(outputLocation);
                if (!outputLoc.isDirectory()) {
                    throw new IllegalArgumentException("Given output location is not a directory: " + outputLocation);
                }
                if(!outputLocation.endsWith("/")) {
                    outputLocation = commandLine.getOptionValue("output") + "/";
                }
                ClinVarFilter clinVarFilter = new ClinVarFilter(StarRating.ZEROSTAR, clinvarLocation, outputLocation);
                fileLoc = clinVarFilter.removeStatus();
            } else {
                ClinVarFilter clinVarFilter = new ClinVarFilter(StarRating.ZEROSTAR, clinvarLocation, null);
                fileLoc = clinVarFilter.removeStatus();
            }
            logger.info("Created the filtered file at the following location: " + fileLoc);
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
                "java -jar ClinVar-Filter-[version] [options] [dir to clinvar]");
        System.exit(0);
    }
}
