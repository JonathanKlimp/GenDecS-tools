import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MatcherMain {
    private static final Logger logger = LoggerFactory.getLogger(MatcherMain.class);

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
                        "java -jar Variant-Matcher-[version] [options] [location of filtered clinvar] [location of vcf data]");
                System.exit(0);
            }
            String clinvarLocation = commandLine.getArgList().get(0);
            String vcfDataLocation = commandLine.getArgList().get(1);
            if (commandLine.hasOption("output")) {
                VariantMatcher variantMatcher = new VariantMatcher(clinvarLocation, vcfDataLocation, commandLine.getOptionValue("output"));
                String fileLoc = variantMatcher.matchWithClinvar();
                logger.info("Created the filtered file at the following location: " + fileLoc);
            } else {
                VariantMatcher variantMatcher = new VariantMatcher(clinvarLocation, vcfDataLocation, null);
                String fileLoc = variantMatcher.matchWithClinvar();
                logger.info("Created the filtered file at the following location: " + fileLoc);
            }
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
