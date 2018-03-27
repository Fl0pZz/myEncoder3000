import algorithms.Algorithm;
import algorithms.IAlgorithm;
import io.TextFile;
import org.apache.commons.cli.*;

import java.io.IOException;

public class Main {
    private static void genInterface (Options options, Algorithm algs) {
        Option files = Option.builder("f" )
                .argName("file")
                .longOpt("files")
                .hasArgs()
                .desc("encode files")
                .build();
        Option text = Option.builder("t")
                .argName("text")
                .hasArg()
                .desc("enter text. It will be encoded")
                .build();
        Option algo = Option.builder("a")
                .argName("algName")
                .hasArg()
                .desc("Choose 1 of the presented algorithms: " + algs.allAlgorithms()[0])
                .build();

        options.addOption( "h","help", false, "print this message" );
        options.addOption( "v","version", false, "print the version information and exit" );
        options.addOption( "d","decode", false, "decode mode" );
        options.addOption( "e","encode", false, "encode mode" );
        options.addOption(files);
        options.addOption(text);
        options.addOption(algo);
    }

    private static void checkArgs (CommandLine line, Algorithm algs, Options options) {
        if (!line.hasOption("h") && !line.hasOption("d") && !line.hasOption("encode")) {
            System.out.println("Choose work mode: encode or decode!");
            return;
        }

        if (!line.hasOption("h") && !line.hasOption("f") && !line.hasOption("t")) {
            System.out.println("Choose work mode: file or text");
            return;
        }

        if (!line.hasOption("h") && !line.hasOption("a")) {
            System.out.println("Choose algorithm!");
            return;
        } else if (!line.hasOption("h") && line.hasOption("a") && !algs.checkAlgoName(line.getOptionValue("a").trim())) {
            System.out.println("Enter algorithm name correctly!");
            return;
        } else if (line.hasOption("h")) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp( "myapp", options );
        }
    }

    public static void main(String[] args) {
//    public static void main(String[] aa) {
//        String[] args = new String[]{ "-e",  "-t hello", "-a huffman" };
//        String[] args = new String[]{ "-h" };
        Options options = new Options();
        Algorithm algs = new Algorithm();

        genInterface(options, algs);

        // create the parser
        CommandLineParser parser = new DefaultParser();
        try {
            // parse the command line arguments
            CommandLine line = parser.parse( options, args);
            checkArgs(line, algs, options);


            if (line.hasOption("f")) {
                String[] filePaths = line.getOptionValue("f").trim().split(" ");

                for (String filePath : filePaths) {
                    if (line.hasOption("d")) {
                        TextFile.decodeFile(filePath, algs.selectAlgo(line.getOptionValue("a").trim()));
                    } else if (line.hasOption("e")) {
                        TextFile.encodeFile(filePath, algs.selectAlgo(line.getOptionValue("a").trim()));
                    } else {
                        System.out.println("Choose work mode: encode or decode!");
                        return;
                    }
                }
            } else if (line.hasOption("t")) {
                IAlgorithm alg = algs.selectAlgo(line.getOptionValue("a").trim());
                if (line.hasOption("d")) {
                    System.out.println(alg.decode(line.getOptionValue("t").trim()));
                } else if (line.hasOption("e")) {
                    System.out.println(alg.encode(line.getOptionValue("t").trim()));
                } else {
                    System.out.println("Choose work mode: encode or decode!");
                    return;
                }
            }
            if (line.hasOption("v")) {
                System.out.println("0.1.0-ALPHA");
            }
        } catch( ParseException exp ) {
            // oops, something went wrong
            System.err.println( "Parsing failed.  Reason: " + exp.getMessage() );
        } catch (IOException exp) {
            // oops, something went wrong
            System.err.println(": Oops!.  Reason: " + exp.getMessage() );
        }
    }
}
