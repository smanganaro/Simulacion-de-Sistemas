package ar.edu.itba.ss.LennardJones.movement;

import static java.util.Objects.requireNonNull;

import ar.edu.itba.ss.LennardJones.core.Neighbour;
import javafx.geometry.Point2D;
import ar.edu.itba.ss.LennardJones.core.Particle;

import java.util.Collections;
import java.util.Set;
import java.util.function.BiFunction;

public class BeemanMovementFunction implements MovementFunction {

  private final BiFunction<Particle, Set<Neighbour>, Point2D> forceFunction;
  private Point2D previousAcceleration;

  public BeemanMovementFunction(final BiFunction<Particle, Set<Neighbour>, Point2D> forceFunction,
      final Point2D previousAcceleration) {

    this.forceFunction = forceFunction;
    this.previousAcceleration = previousAcceleration;
  }

  @Override
  public Particle move(final Particle currentParticle, final Set<Neighbour> neighbours, final double dt) {

    final Point2D currentAcceleration = forceFunction.apply(currentParticle, neighbours)
        .multiply(1.0 / currentParticle.getMass());

    final Point2D predictedPosition = currentParticle.getPosition()
        .add(currentParticle.getVelocity()
            .multiply(dt))
        .add(currentAcceleration
            .multiply(dt * dt * 2.0 / 3.0))
        .subtract(previousAcceleration
            .multiply(dt * dt / 6.0));

    final Particle predictedParticle = new Particle(currentParticle.getID(),predictedPosition,currentParticle.getRadius(),currentParticle.getMass(),
            currentParticle.getVelocity(), Collections.emptyList());

    final Point2D predictedAcceleration = forceFunction.apply(predictedParticle, neighbours)
        .multiply(1.0 / predictedParticle.getMass());

    final Point2D predictedVelocity = currentParticle.getVelocity()
        .add(predictedAcceleration
            .multiply(dt / 3.0))
        .add(currentAcceleration
            .multiply(dt * 5.0 / 6.0))
        .subtract(previousAcceleration
            .multiply(dt / 6.0));

    previousAcceleration = currentAcceleration;
    return new Particle(currentParticle.getID(),predictedPosition,currentParticle.getRadius(),currentParticle.getMass(),
            predictedVelocity, Collections.emptyList());
  }
}
