package ar.edu.itba.ss;
import org.apache.commons.cli.*;
public class CliParser {

    public static String dynamicFile;
    static double particleRadius = 0.2;
    static double interactionRadius = 1;
    static double noise = 0.1;
    static double speed = 0.3;
    static double time = 100;
    static double intervals = 1;

    private static Options createOptions(){
        Options options = new Options();
        options.addOption("h", "help", false, "Shows this screen.");
        options.addOption("rc", "radius_interaction", true, "Radius of interaction between particles.");
        options.addOption("df", "dynamic_file", true, "Path to the file with the dynamic values.");
        options.addOption("rp", "radius_particles", true, "Radius of the particles");
        options.addOption("n", "noise", true, "Noise of the environment.");
        options.addOption("s", "speed", true, "Speed module of the particles.");
        options.addOption("t", "time", true, "Total time of the simulation.");
        options.addOption("i", "intervals", true, "Intervals of time.");
        return options;
    }

    public static void parseOptions(String[] args){
        Options options = createOptions();
        CommandLineParser parser = new BasicParser();

        try{
            CommandLine cmd = parser.parse(options, args);
            if(cmd.hasOption("h")){
                help(options);
            }

            if (!cmd.hasOption("df")){
                System.out.println("You must specify a dynamic file!");
                System.exit(1);
            }
            
            dynamicFile = cmd.getOptionValue("df");

            if (cmd.hasOption("n")) {
                noise = Double.parseDouble(cmd.getOptionValue("n"));
            }

            if (cmd.hasOption("s")) {
                speed = Double.parseDouble(cmd.getOptionValue("s"));
            }

            if (cmd.hasOption("rc")) {
                interactionRadius = Double.parseDouble(cmd.getOptionValue("rc"));
            }

            if (cmd.hasOption("rp")) {
                particleRadius = Double.parseDouble(cmd.getOptionValue("rp"));
            }

            if (cmd.hasOption("t")) {
                time = Double.parseDouble(cmd.getOptionValue("t"));
            }

            if (cmd.hasOption("i")) {
                intervals = Double.parseDouble(cmd.getOptionValue("t"));
            }

        }catch (Exception e){
            System.out.println("Command not recognized.");
            help(options);
        }
    }

    private static void help(Options options){
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp("Main", options);
        System.exit(0);
    }
}
