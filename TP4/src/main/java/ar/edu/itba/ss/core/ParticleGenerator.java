package ar.edu.itba.ss.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ParticleGenerator {


    /* Small Particles */
    private double R1;
    private double M1;
    private double MAGNITUDE;

    public ParticleGenerator(double r1, double m1, double magnitude) {
        R1 = r1;
        M1 = m1;
        MAGNITUDE = magnitude;

    }

    /**
     * Generates random valid particles
     * @return List of particles
     */
    public List<Particle> generateParticles(int numberOfParticles, double width, double height) {
        List<Particle> particles = new ArrayList<>();

        for (int i = 0; i < numberOfParticles; i++) {

            double x;
            double y;

            do {
                x = randomCoordinates(width, R1);
                y = randomCoordinates(height, R1);
            }
            while (!Particle.validCoordinates(x, y, R1, particles));
            double randomAngle = randomAngle();
            double vx = MAGNITUDE*Math.cos(randomAngle);
            double vy = MAGNITUDE*Math.sin(randomAngle);
            Coordinates c = new Coordinates(x,y);
            Coordinates v = new Coordinates(vx,vy);
            particles.add(new Particle(c,R1,M1,v, Collections.EMPTY_LIST));
        }

        return particles;
    }

    /**
     * Obtain a random angle between 0 and 2PI
     * @return a velocity
     */
    private double randomAngle(){
        return  2 * Math.PI * Math.random();
    }

    /**
     * Returns a random coordinate between the radius and L - radius.
     * @param radius radius of the particle.
     * @return a coordinate in the (radius, L - radius) interval.
     */
    private double randomCoordinates(double L, double radius){
        return  radius + (L - 2 * radius) * Math.random();
    }
}
