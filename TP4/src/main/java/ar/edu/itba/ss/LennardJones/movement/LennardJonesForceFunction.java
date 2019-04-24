package ar.edu.itba.ss.LennardJones.movement;

import static java.lang.Math.pow;

import ar.edu.itba.ss.LennardJones.core.Neighbour;
import javafx.geometry.Point2D;
import ar.edu.itba.ss.LennardJones.core.Particle;
import java.util.Set;
import java.util.function.BiFunction;

public class LennardJonesForceFunction implements BiFunction<Particle, Set<Neighbour>, Point2D> {

  private final double epsilon;
  private final double rm;

  public LennardJonesForceFunction(final double epsilon, final double rm) {
    this.epsilon = epsilon;
    this.rm = rm;
  }

  @Override
  public Point2D apply(final Particle particle, final Set<Neighbour> neighbours) {

    double totalForceX = 0;
    double totalForceY = 0;

    for (final Neighbour neighbour : neighbours) {
      final double magnitude = forceMagnitude(epsilon, rm, neighbour.getDistance());
      final Point2D distanceVector = particle.getPosition().subtract(neighbour.getNeighbourParticle().getPosition());
      totalForceX += magnitude * (distanceVector.getX()) / distanceVector.magnitude();
      totalForceY += magnitude * (distanceVector.getY()) / distanceVector.magnitude();
    }

    return new Point2D(totalForceX, totalForceY);
  }

  private static double forceMagnitude(final double epsilon, final double rm, final double r) {
    return 12 * epsilon * (pow(rm / r, 13) - pow(rm / r, 7)) / rm;
  }
}
