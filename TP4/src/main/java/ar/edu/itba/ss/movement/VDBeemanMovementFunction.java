package ar.edu.itba.ss.movement;


import ar.edu.itba.ss.core.Neighbour;
import ar.edu.itba.ss.core.Coordinates;
import ar.edu.itba.ss.core.Particle;

import java.util.Collections;
import java.util.Set;
import java.util.function.BiFunction;

public class VDBeemanMovementFunction implements MovementFunction {

  private final BiFunction<Particle, Set<Neighbour>, Coordinates> forceFunction;
  private Coordinates previousAcceleration;

  public VDBeemanMovementFunction(final BiFunction<Particle, Set<Neighbour>, Coordinates> forceFunction,
      final Coordinates previousAcceleration) {

    this.forceFunction = forceFunction;
    this.previousAcceleration = previousAcceleration;
  }

  @Override
  public Particle move(final Particle currentParticle, final Set<Neighbour> neighbours,
      final double dt) {

    final Coordinates currentAcceleration = forceFunction.apply(currentParticle, neighbours)
        .multiply(1.0 / currentParticle.getMass());

    final Coordinates predictedPosition = currentParticle.getPosition()
        .add(currentParticle.getVelocity()
            .multiply(dt))
        .add(currentAcceleration
            .multiply(dt * dt * 2.0 / 3.0))
        .subtract(previousAcceleration
            .multiply(dt * dt / 6.0));

    final Coordinates predictedVelocity = currentParticle.getVelocity()
        .add(currentAcceleration
            .multiply(dt * 3.0 / 2.0))
        .subtract(previousAcceleration
            .multiply(dt / 2.0));

    final Particle predictedParticle = new Particle(currentParticle.getID(),predictedPosition,currentParticle.getRadius(),currentParticle.getMass(),
            predictedVelocity, Collections.emptyList());

    final Coordinates predictedAcceleration = forceFunction.apply(predictedParticle, neighbours)
        .multiply(1.0 / predictedParticle.getMass());

    final Coordinates correctedVelocity = currentParticle.getVelocity()
        .add(predictedAcceleration
            .multiply(dt / 3.0))
        .add(currentAcceleration
            .multiply(dt * 5.0 / 6.0))
        .subtract(previousAcceleration
            .multiply(dt / 6.0));

    previousAcceleration = currentAcceleration;

    return new Particle(predictedParticle.getID(),predictedParticle.getPosition(),predictedParticle.getRadius(),predictedParticle.getMass(),
            correctedVelocity, Collections.emptyList());
  }
}
