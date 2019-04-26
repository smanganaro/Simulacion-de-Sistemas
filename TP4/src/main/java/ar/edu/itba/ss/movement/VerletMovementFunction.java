package ar.edu.itba.ss.movement;

import static java.util.Objects.requireNonNull;

import ar.edu.itba.ss.core.Neighbour;
import ar.edu.itba.ss.core.Coordinates;
import ar.edu.itba.ss.core.Particle;

import java.util.Collections;
import java.util.Set;
import java.util.function.BiFunction;

public class VerletMovementFunction implements MovementFunction {

  private final BiFunction<Particle, Set<Neighbour>, Coordinates> forceFunction;
  private Coordinates previousPosition;

  public VerletMovementFunction(final BiFunction<Particle, Set<Neighbour>, Coordinates> forceFunction,
      final Coordinates previousPosition) {

    this.forceFunction = forceFunction;
    this.previousPosition = previousPosition;
  }

  @Override
  public Particle move(final Particle currentParticle, final Set<Neighbour> neighbours,
      final double dt) {

    final Coordinates currentForce = forceFunction.apply(currentParticle, neighbours);

    final Coordinates predictedPosition = currentParticle.getPosition()
        .multiply(2)
        .subtract(previousPosition)
        .add(currentForce.multiply(dt * dt / currentParticle.getMass()));

    final Coordinates predictedVelocity = predictedPosition
        .subtract(previousPosition)
        .multiply(1.0 / (2.0 * dt));

    previousPosition = currentParticle.getPosition();

    return new Particle(currentParticle.getID(),predictedPosition,currentParticle.getRadius(),currentParticle.getMass(),
            predictedVelocity, Collections.emptyList());
  }
}
