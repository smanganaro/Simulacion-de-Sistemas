package ar.edu.itba.ss.LennardJones.movement;

import static java.util.Objects.requireNonNull;

import ar.edu.itba.ss.LennardJones.core.Neighbour;
import javafx.geometry.Point2D;
import ar.edu.itba.ss.LennardJones.core.Particle;

import java.util.Collections;
import java.util.Set;
import java.util.function.BiFunction;

public class VerletMovementFunction implements MovementFunction {

  private final BiFunction<Particle, Set<Neighbour>, Point2D> forceFunction;
  private Point2D previousPosition;

  public VerletMovementFunction(final BiFunction<Particle, Set<Neighbour>, Point2D> forceFunction,
      final Point2D previousPosition) {

    this.forceFunction = forceFunction;
    this.previousPosition = previousPosition;
  }

  @Override
  public Particle move(final Particle currentParticle, final Set<Neighbour> neighbours,
      final double dt) {

    final Point2D currentForce = forceFunction.apply(currentParticle, neighbours);

    final Point2D predictedPosition = currentParticle.getPosition()
        .multiply(2)
        .subtract(previousPosition)
        .add(currentForce.multiply(dt * dt / currentParticle.getMass()));

    final Point2D predictedVelocity = predictedPosition
        .subtract(previousPosition)
        .multiply(1.0 / (2.0 * dt));

    previousPosition = currentParticle.getPosition();

    return new Particle(currentParticle.getID(),predictedPosition,currentParticle.getRadius(),currentParticle.getMass(),
            predictedVelocity, Collections.emptyList());
  }
}
