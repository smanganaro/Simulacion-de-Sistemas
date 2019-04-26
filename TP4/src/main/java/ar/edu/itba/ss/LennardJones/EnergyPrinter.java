package ar.edu.itba.ss.LennardJones;

import ar.edu.itba.ss.core.Coordinates;
import ar.edu.itba.ss.core.Neighbour;
import ar.edu.itba.ss.core.Particle;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.lang.Math.pow;

public class EnergyPrinter implements Printer{
    private List<Coordinates> points;
    private final double epsilon;
    private final double rm;

    public EnergyPrinter(double epsilon, double rm) {
        this.epsilon = epsilon;
        this.rm = rm;
        this.points = new LinkedList<>();
    }

    private double kineticEnergy(final Particle particle) {
        return 0.5 * particle.getMass()
                * particle.getVelocity().magnitude() * particle.getVelocity().magnitude();
    }

    private double potentialEnergy(final Particle particle, final Set<Neighbour> neighbours) {
        double potential = 0;

        for (Neighbour neighbour : neighbours) {
            potential += epsilon * (pow(rm / neighbour.getDistance(), 12) - 2.0 * pow(rm / neighbour.getDistance(), 6));
            if (neighbour.getNeighbourParticle().getID() < 0) {
                potential += epsilon * (pow(rm / neighbour.getDistance(), 12) - 2.0 * pow(rm / neighbour.getDistance(), 6));
            }
        }

        return potential;
    }

    @Override
    public void print(Map<Particle, Set<Neighbour>> particles, double t, int iteration) {
        Double sumKinetic = 0.0, sumPotential = 0.0, total;
        for(Map.Entry<Particle,Set<Neighbour>> entry: particles.entrySet()){
            Particle particle = entry.getKey();
            Set<Neighbour> neighbours = entry.getValue();
            sumKinetic += kineticEnergy(particle);
            sumPotential += potentialEnergy(particle,neighbours);

        }
        total = sumKinetic+sumPotential;
        System.out.println("Time: "+t+" Kinetic Energy: "+sumKinetic+" Potential Energy: "+sumPotential+" Total: "+total);
    }
}
