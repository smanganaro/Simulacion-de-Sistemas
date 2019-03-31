package ar.edu.itba.ss;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ParticleGenerator {
    //Default values

    /* Small Particles */
    private double R1 = 0.005;
    private double M1 = 0.1;
    private double V1 = 0.1;

    /* Big Particle */
    private double R2 = 0.05;
    private double M2 = 100;

    public ParticleGenerator() {
    }

    public ParticleGenerator(double r1, double m1, double v1, double r2, double m2, double l) {
        R1 = r1;
        M1 = m1;
        V1 = v1;
        R2 = r2;
        M2 = m2;
    }

    /**
     * Generates random valid particles
     * @return List of particles
     */
    public List<Particle> generateParticles(int numberOfParticles,double temperature, double L) {
        List<Particle> particles = new ArrayList<>();

        /* Add big particle */
        particles.add(new Particle(new Coordinates( L/2, L/2),R2,M2,new Velocity(0.0,0.0), particles));

        for (int i = 0; i < numberOfParticles; i++){

            double x;
            double y;

            do {
                x = randomCoordinates(L,R1);
                y = randomCoordinates(L,R1);
            }
            while (!Particle.validCoordinates(x,y, R1, particles));

            double vx;
            double vy;

            if (temperature == 0){
                vx = randomSpeed();
                vy = randomSpeed();
            }else{
                double V = temperature/numberOfParticles;
                vx = V/Math.sqrt(2);
                vy = V/Math.sqrt(2);

                double sign = Math.random();
                if (sign > 0.5){
                    vx = vx * (-1);
                }
                sign = Math.random();
                if (sign > 0.5){
                    vy = vy * (-1);
                }

            }
            particles.add(new Particle(new Coordinates( x, y),R1,M1,new Velocity(vx,vy), particles));
        }

        return Collections.unmodifiableList(particles);
    }

    /**
     * Obtain a random speed on a given interval
     * @return a speed on the interval (-V1, V1)
     */
    private double randomSpeed(){
        return  2 * V1 * Math.random() - V1;
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
