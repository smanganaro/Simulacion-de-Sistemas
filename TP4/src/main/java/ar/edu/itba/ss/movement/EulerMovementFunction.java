package ar.edu.itba.ss.movement;

import static java.util.Objects.requireNonNull;

import ar.edu.itba.ss.core.Neighbour;
import ar.edu.itba.ss.core.Coordinates;
import ar.edu.itba.ss.core.Particle;

import java.util.Collections;
import java.util.Set;
import java.util.function.BiFunction;

public class EulerMovementFunction implements MovementFunction {

  private final BiFunction<Particle, Set<Neighbour>, Coordinates> forceFunction;

  public EulerMovementFunction(final BiFunction<Particle, Set<Neighbour>, Coordinates> forceFunction) {
    this.forceFunction = forceFunction;
  }

  public static Particle move(final Particle currentParticle, final Set<Neighbour> neighbours,
      final double dt, final BiFunction<Particle, Set<Neighbour>, Coordinates> forceFunction) {

    final Coordinates force = forceFunction.apply(currentParticle, neighbours);

    final Coordinates newVelocity = currentParticle.getVelocity()
        .add(force
            .multiply(dt / currentParticle.getMass()));

    final Coordinates newPosition = currentParticle.getPosition()
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
