package ar.edu.itba.ss.movement.forces;

import static java.lang.Math.pow;

import ar.edu.itba.ss.core.Neighbour;
import ar.edu.itba.ss.core.Coordinates;
import ar.edu.itba.ss.core.Particle;
import java.util.Set;
import java.util.function.BiFunction;

public class LennardJonesForceFunction implements BiFunction<Particle, Set<Neighbour>, Coordinates> {

  private final double epsilon;
  private final double rm;

  public LennardJonesForceFunction(final double epsilon, final double rm) {
    this.epsilon = epsilon;
    this.rm = rm;
  }

  @Override
  public Coordinates apply(final Particle particle, final Set<Neighbour> neighbours) {

    double totalForceX = 0;
    double totalForceY = 0;

    for (final Neighbour neighbour : neighbours) {
      final double magnitude = forceMagnitude(epsilon, rm, neighbour.getDistance());
      final Coordinates distanceVector = particle.getPosition().subtract(neighbour.getNeighbourParticle().getPosition());
      totalForceX += magnitude * (distanceVector.getX()) / distanceVector.magnitude();
      totalForceY += magnitude * (distanceVector.getY()) / distanceVector.magnitude();
    }

    return new Coordinates(totalForceX, totalForceY);
  }

  private static double forceMagnitude(final double epsilon, final double rm, final double r) {
    return 12 * epsilon * (pow(rm / r, 13) - pow(rm / r, 7)) / rm;
  }
}
