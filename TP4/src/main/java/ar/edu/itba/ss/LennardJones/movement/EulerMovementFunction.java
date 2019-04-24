package ar.edu.itba.ss.LennardJones.movement;

import static java.util.Objects.requireNonNull;

import ar.edu.itba.ss.LennardJones.core.Neighbour;
import javafx.geometry.Point2D;
import ar.edu.itba.ss.LennardJones.core.Particle;

import java.util.Collections;
import java.util.Set;
import java.util.function.BiFunction;

public class EulerMovementFunction implements MovementFunction {

  private final BiFunction<Particle, Set<Neighbour>, Point2D> forceFunction;

  public EulerMovementFunction(final BiFunction<Particle, Set<Neighbour>, Point2D> forceFunction) {
    this.forceFunction = forceFunction;
  }

  public static Particle move(final Particle currentParticle, final Set<Neighbour> neighbours,
      final double dt, final BiFunction<Particle, Set<Neighbour>, Point2D> forceFunction) {

    final Point2D force = forceFunction.apply(currentParticle, neighbours);

    final Point2D newVelocity = currentParticle.getVelocity()
        .add(force
            .multiply(dt / currentParticle.getMass()));

    final Point2D newPosition = currentParticle.getPosition()
        .add(newVelocity.multiply(dt))
        .add(force
            .multiply(dt * dt / (2 * currentParticle.getMass())));

    return new Particle(currentParticle.getID(),newPosition,currentParticle.getRadius(),currentParticle.getMass(),
            newVelocity, Collections.emptyList());
  }

  @Override
  public Particle move(final Particle currentParticle, final Set<Neighbour> neighbours,
      final double dt) {
    return move(currentParticle, neighbours, dt, forceFunction);
  }
}
