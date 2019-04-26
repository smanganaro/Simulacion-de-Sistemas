package ar.edu.itba.ss.LennardJones;

import ar.edu.itba.ss.core.Neighbour;
import ar.edu.itba.ss.core.Particle;

import java.util.Map;
import java.util.Set;

public class CoordinatePrinter implements Printer {
    private final double boxWidth;
    private final double boxHeight;

    public CoordinatePrinter(double boxWidth, double boxHeight) {
        this.boxWidth = boxWidth;
        this.boxHeight = boxHeight;
    }

    @Override
    public void print(Map<Particle, Set<Neighbour>> particles, double t, int iteration) {
        System.out.println(particles.size() + 2);
        System.out.println(t);
        for (Map.Entry<Particle,Set<Neighbour>> entry: particles.entrySet()){
            Particle p = entry.getKey();
            System.out.println(p.getPosition().getX() + "\t" + p.getPosition().getY() + "\t"
                    + p.getVelocity().getX() + "\t" + p.getVelocity().getY() + "\t" + p.getRadius());
        }
        // Print two particles for fixed Simulation Box in Ovito animation
        System.out.println(0 + "\t" + 0 + "\t" + 0 + "\t" + 0 + "\t" + 0.001 + "\t" + 0);
        System.out.println(boxWidth + "\t" + boxHeight + "\t" + 0 + "\t" + 0 + "\t" + 0.001 + "\t" + 0);
    }
}
