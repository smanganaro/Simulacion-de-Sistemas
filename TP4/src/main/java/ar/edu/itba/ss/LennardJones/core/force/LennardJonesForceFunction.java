package ar.edu.itba.ss.LennardJones.core.force;

import ar.edu.itba.ss.LennardJones.core.Coordinates;
import ar.edu.itba.ss.LennardJones.core.Particle;

import java.util.List;
import java.util.function.BiFunction;

import static java.lang.Math.pow;

public class LennardJonesForceFunction implements BiFunction<Particle, List<Particle>, Coordinates> {

    private final double epsilon;
    private final double rm;

    public LennardJonesForceFunction(final double epsilon, final double rm) {
        this.epsilon = epsilon;
        this.rm = rm;
    }

    private static double forceMagnitude(final double epsilon, final double rm, final double r) {
        return 12 * epsilon * (pow(rm / r, 13) - pow(rm / r, 7)) / rm;
    }

    @Override
    public Coordinates apply(final Particle particle, final List<Particle> neighbours) {

        double totalForceX = 0;
        double totalForceY = 0;

        for (final Particle neighbour : neighbours) {
            final double magnitude = forceMagnitude(epsilon, rm, neighbour.getPosition().getDistance(particle.getPosition()));
            final Coordinates distanceVector = particle.getPosition()
                    .subtract(neighbour.getPosition());

            totalForceX += magnitude * (distanceVector.getX()) / distanceVector.magnitude();
            totalForceY += magnitude * (distanceVector.getY()) / distanceVector.magnitude();
        }

        return new Coordinates(totalForceX, totalForceY);
    }
}
