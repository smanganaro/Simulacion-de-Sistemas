package ar.edu.itba.ss;

import java.time.Duration;
import java.time.Instant;

import static java.lang.System.exit;

public class Main {

    public static void main( String[] args ) {

        CliParser.parseOptions(args);
        try{
            InputParser.ParticlesInput particlesInput = InputParser.readParticles(CliParser.dynamicFile,CliParser.speed,CliParser.particleRadius);
            int M = getM(particlesInput.getL(),CliParser.interactionRadius);
            Grid grid = new CicleGrid(particlesInput.getL(), M, particlesInput.getParticles());
            Instant start = Instant.now();
            OffLatice offLatice = new OffLatice(grid,CliParser.interactionRadius,particlesInput.getParticles(),CliParser.time,CliParser.intervals,CliParser.noise);
            double Va = offLatice.run();
            Instant finish = Instant.now();
            long timeElapsed = Duration.between(start, finish).toMillis();
            System.out.println("Simulation Va: "+Va);
            System.out.println("Time elapsed: "+timeElapsed);
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("There was an error while executing the program, please try again");
            exit(1);
        }

    }
    // Ver esto de getM optimo
    private static int getM(double L, double rc){
        int max = (int)((int)L/rc);
        while(L%max != 0){
            max--;
        }
        return max;
    }
}
