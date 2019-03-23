package ar.edu.itba.ss;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class OutputSimulation {

    private static OutputSimulation instance = null;
    private static String filename = CliParser.filename;

    public static OutputSimulation getInstance(){
        if(instance == null) {
            instance = new OutputSimulation();
            filename = CliParser.filename+"_noise_"+CliParser.noise+"_output_va.txt";
        }
        return instance;
    }

    public void write(double time, double Va, double N, double L) throws IOException {
        if(time == 0){
            PrintWriter pw = new PrintWriter(filename);
            pw.close();
        }

        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(filename, true)));
        out.write(generateFileString(time,Va,N,L,CliParser.noise));
        out.close();
    }

    public static String generateFileString(double time, double Va, double N, double L, double noise){
        double density = N/(L*L);
        StringBuilder builder = new StringBuilder()
                .append(time)
                .append("\t")
                .append(Va)
                .append("\t")
                .append(density)
                .append("\t")
                .append(noise)
                .append("\r\n");

        return builder.toString();
    }
}