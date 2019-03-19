import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;



public class InputParser {
    private final static double DEFAULT_COLOR = 1.0;
    private final static double DEFAULT_VELOCITY = 1.0;

    public static void readParticles(String dPath, int N, Double velocity, Double radius, Map<Double, Set<Particle>> map) throws FileNotFoundException {
        File dFile = new File(dPath);
        Scanner read = new Scanner(dFile);

        System.out.println("Reading particles from file "+dPath);
        while (read.hasNextLine()) {
            Double actualTime = Double.parseDouble(read.next());
            Set<Particle> parts = new HashSet<>();
            velocity = (velocity == null)? DEFAULT_VELOCITY:velocity;
            //System.out.println("Actual time reading: "+ actualTime);
            for (int i = 0; i < N; i++) {
                int ID = Integer.parseInt(read.next()); //no lo uso para nadaaaaa
                //System.out.println("Reading data ID: " + ID);
                double x = Double.parseDouble(read.next());
                double y = Double.parseDouble(read.next());
                double angle = Double.parseDouble(read.next());

                Coordinates position = new Coordinates(x,y);
                Velocity v = new Velocity(velocity, angle);
                Particle p = new Particle(position,radius,DEFAULT_COLOR,v);
                parts.add(p);
            }
            map.put(actualTime, parts);
        }

        read.close();
    }

}
