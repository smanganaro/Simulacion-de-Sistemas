package ar.edu.itba.ss;

import org.apache.commons.cli.*;

import static java.lang.System.exit;

public class CliParser {

    static int time = 100;
    static int numberOfParticles;
    static double L = 0.5;
    static double temperature = 0;

    private static Options createOptions(){
        Options options = new Options();
        options.addOption("h", "help", false, "Shows this screen.");
        options.addOption("t", "time", true, "Total time of the simulation.");
        options.addOption("L", "length", true, "Box L size.");
        options.addOption("p", "particles", true, "Number of particles.");
        options.addOption("T", "temperature", true, "Initial temperature of the system.");
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

            if (cmd.hasOption("p")) {
                numberOfParticles = Integer.parseInt(cmd.getOptionValue("p"));
            }else{
                System.err.println("You must specify the number of particles.");
                exit(1);
            }

            if (cmd.hasOption("T")) {
                temperature = Double.parseDouble(cmd.getOptionValue("T"));

                if (temperature < 0){
                    System.err.println("Temperature must be greater or equal 0.");
                    exit(1);
                }
            }

            if (cmd.hasOption("t")) {
                time = Integer.parseInt(cmd.getOptionValue("t"));
            }

            if(cmd.hasOption("L")){
                L = Double.parseDouble(cmd.getOptionValue("L"));
            }


        }catch (Exception e){
            System.out.println("Command not recognized.");
            help(options);
        }
    }

    private static void help(Options options){
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp("brownian-motion", options);
        exit(0);
    }
}
