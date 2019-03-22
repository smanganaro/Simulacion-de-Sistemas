package ar.edu.itba.ss;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;


public class InputParser {
    private final static double DEFAULT_COLOR = 1.0;
    private final static double DEFAULT_VELOCITY = 0.3;

    public static ParticlesInput readParticles(String dPath, Double velocity, Double radius) throws FileNotFoundException {
        File dFile = new File(dPath);
        Scanner read = new Scanner(dFile);
        Double length = read.nextDouble();

        CliParser.filename = dFile.getName();

        velocity = (velocity == null)? DEFAULT_VELOCITY : velocity;
        List<Particle> parts = new LinkedList<>();

        while (read.hasNextDouble()) {
            double x = read.nextDouble();
            double y = read.nextDouble();
            double angle = read.nextDouble();

            Coordinates position = new Coordinates(x,y);
            Velocity v = new Velocity(velocity, angle);
            Particle p = new Particle(position,radius,DEFAULT_COLOR,v);
            parts.add(p);

        }
        read.close();

        return new ParticlesInput(length,parts);
    }

    public static class ParticlesInput{
        private Double L;
        private List<Particle> particles;

        public ParticlesInput(Double l, List<Particle> particles) {
            L = l;
            this.particles = particles;
        }

        public Double getL() {
            return L;
        }

        public List<Particle> getParticles() {
            return particles;
        }
    }
}
