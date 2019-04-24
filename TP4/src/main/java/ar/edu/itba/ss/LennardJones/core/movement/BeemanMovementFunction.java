package ar.edu.itba.ss.LennardJones.core.movement;

import ar.edu.itba.ss.LennardJones.core.Coordinates;
import ar.edu.itba.ss.LennardJones.core.Particle;

import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;

public class BeemanMovementFunction implements MovementFunction {

    private final BiFunction<Particle, List<Particle>, Coordinates> forceFunction;
    private Coordinates previousAcceleration;

    public BeemanMovementFunction(final BiFunction<Particle, List<Particle>, Coordinates>forceFunction,
                                  final Coordinates previousAcceleration) {

        this.forceFunction = forceFunction;
        this.previousAcceleration = previousAcceleration;
    }

    @Override
    public Particle move(final Particle currentParticle, final List<Particle> neighbours,
                         final double dt) {

        final Coordinates currentAcceleration = forceFunction.apply(currentParticle, neighbours)
                .multiply(1.0 / currentParticle.getMass());

        final Coordinates predictedPosition = currentParticle.getPosition()
                .add(currentParticle.getPosition()
                        .multiply(dt))
                .add(currentAcceleration
                        .multiply(dt * dt * 2.0 / 3.0))
                .subtract(previousAcceleration
                        .multiply(dt * dt / 6.0));

        final Particle predictedParticle = new Particle(0,predictedPosition,currentParticle.getRadius(),
                currentParticle.getMass(),currentParticle.getVelocity(), Collections.emptyList());

        final Coordinates predictedAcceleration = forceFunction.apply(predictedParticle, neighbours)
                .multiply(1.0 / predictedParticle.getMass());

        final Coordinates predictedVelocity = currentParticle.getVelocity()
                .add(predictedAcceleration
                        .multiply(dt / 3.0))
                .add(currentAcceleration
                        .multiply(dt * 5.0 / 6.0))
                .subtract(previousAcceleration
                        .multiply(dt / 6.0));

        previousAcceleration = currentAcceleration;

        return new Particle(currentParticle.getID(),predictedPosition,currentParticle.getRadius(),
                currentParticle.getMass(),predictedVelocity, Collections.emptyList());
    }

    @Override
    public void clearState(Particle particle) {
        previousAcceleration = new Coordinates(0.0,0.0);
    }
}
