import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

public class FilterMain {
    private static final Logger logger = LoggerFactory.getLogger(FilterMain.class);

    public static void main(String[] args) {
        CommandLineParser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("o", "output", true, "output directory");
        options.addOption("h", "help", false, "print the help of the program");
        try {
            CommandLine commandLine = parser.parse(options, args);
            if (commandLine.hasOption("help")) {
                HelpFormatter helpFormatter = new HelpFormatter();
                helpFormatter.printHelp("help", options);
                System.out.println("program usage: " +
                        "java -jar ClinVar-Filter-[version] [options] [dir to clinvar]");
                System.exit(0);
            }
            String clinvarLocation = commandLine.getArgList().get(0);

            if (commandLine.hasOption("output")) {
                ClinVarFilter clinVarFilter = new ClinVarFilter(StarRating.ZEROSTAR, clinvarLocation, commandLine.getOptionValue("output"));
                String fileLoc = clinVarFilter.removeStatus();
                logger.info("Created the filtered file at the following location: " + fileLoc);
            } else {
                ClinVarFilter clinVarFilter = new ClinVarFilter(StarRating.ZEROSTAR, clinvarLocation, null);
                String fileLoc = clinVarFilter.removeStatus();
                logger.info("Created the filtered file at the following location: " + fileLoc);
            }
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
