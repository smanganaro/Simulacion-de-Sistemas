package ar.edu.itba.ss.LennardJones.core.movement;

import ar.edu.itba.ss.LennardJones.core.Particle;

import java.util.List;

public interface MovementFunction {

    Particle move(final Particle currentParticle, final List<Particle> neighbours, final double dt);

    default void clearState(Particle particle) {
        throw new UnsupportedOperationException();
    }

    ;
}