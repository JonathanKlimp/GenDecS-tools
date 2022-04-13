import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class AnnotatorMain {
    private static final Logger logger = LoggerFactory.getLogger(AnnotatorMain.class);

    public static void main(String[] args) throws IOException {
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
                        "java -jar Variant-HPO-Annotator-[version] [options] [location of vcf data] [location of genes_to_phenotype.txt]");
                System.exit(0);
            }
            String vcfDataLocation = commandLine.getArgList().get(0);
            String genesToPhenotypeLoc = commandLine.getArgList().get(1);

            if (commandLine.hasOption("output")) {
                GeneHpoAnnotator geneHpoAnnotator = new GeneHpoAnnotator(vcfDataLocation, genesToPhenotypeLoc, commandLine.getOptionValue("output"));
                String dataLocation = geneHpoAnnotator.annotateVcfWithHpo();

                logger.info("Created the annotated file at the following location: " + dataLocation);
            } else {
                GeneHpoAnnotator geneHpoAnnotator = new GeneHpoAnnotator(vcfDataLocation, genesToPhenotypeLoc, null);
                String dataLocation = geneHpoAnnotator.annotateVcfWithHpo();

                logger.info("Created the annotated file at the following location: " + dataLocation);
            }
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
