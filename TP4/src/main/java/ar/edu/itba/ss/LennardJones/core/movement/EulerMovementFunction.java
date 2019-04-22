package ar.edu.itba.ss.LennardJones.core.movement;

import ar.edu.itba.ss.LennardJones.core.Coordinates;
import ar.edu.itba.ss.LennardJones.core.Particle;
import com.sun.istack.internal.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;

public class EulerMovementFunction implements MovementFunction {

    private final BiFunction<Particle, List<Particle>, Coordinates> forceFunction;

    public EulerMovementFunction(@NotNull final BiFunction<Particle, List<Particle>, Coordinates> forceFunction) {
        this.forceFunction = forceFunction;
    }

    public static Particle move(final Particle currentParticle, final List<Particle> neighbours,
                                final double dt, final BiFunction<Particle, List<Particle>, Coordinates> forceFunction) {

        final Coordinates force = forceFunction.apply(currentParticle, neighbours);

        final Coordinates newVelocity = currentParticle.getVelocity().add(force.multiply(dt / currentParticle.getMass()));

        final Coordinates newPosition = currentParticle.getPosition()
                .add(newVelocity.multiply(dt))
                .add(force.multiply(dt * dt / (2 * currentParticle.getMass())));

        return new Particle(currentParticle.getID(),newPosition,currentParticle.getRadius(),
                currentParticle.getMass(),newVelocity, Collections.emptyList());
    }

    @Override
    public Particle move(final Particle currentParticle, final List<Particle> neighbours, final double dt) {

        return move(currentParticle, neighbours, dt, forceFunction);
    }
}
