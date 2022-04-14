import org.apache.commons.cli.*;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.File;
import java.io.IOException;

public class AnnotatorMain {
    private static final Logger logger = LogManager.getLogger(AnnotatorMain.class);

    public static void main(String[] args) throws IOException {
        CommandLineParser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("o", "output", true, "output directory");
        options.addOption("h", "help", false, "print the help of the program");

        try {
            CommandLine commandLine = parser.parse(options, args);
            if (commandLine.hasOption("help")) {
                printHelp(options);
            }
            String vcfDataLocation = commandLine.getArgList().get(0);
            String genesToPhenotypeLoc = commandLine.getArgList().get(1);
            String fileLocation;
            if (commandLine.hasOption("output")) {
                String outputLocation = commandLine.getOptionValue("output");
                File outputLoc = new File(outputLocation);

                if (!outputLoc.exists() && !outputLoc.isDirectory()) {
                    throw new IllegalArgumentException("Given output location is not a directory: " + outputLocation);
                }
                if(!commandLine.getOptionValue("output").endsWith("/")) {
                    outputLocation = commandLine.getOptionValue("output") + "/";
                }
                GeneHpoAnnotator geneHpoAnnotator = new GeneHpoAnnotator(vcfDataLocation, genesToPhenotypeLoc, outputLocation);
                fileLocation = geneHpoAnnotator.annotateVcfWithHpo();
            } else {
                GeneHpoAnnotator geneHpoAnnotator = new GeneHpoAnnotator(vcfDataLocation, genesToPhenotypeLoc, null);
                fileLocation = geneHpoAnnotator.annotateVcfWithHpo();
            }
            logger.info("Created the annotated file at the following location: " + fileLocation);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private static void printHelp(Options options) {
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp("help", options);
        System.out.println("program usage: " +
                "java -jar Variant-HPO-Annotator-[version] [options] [location of vcf data] [location of genes_to_phenotype.txt]");
        System.exit(0);
    }
}
