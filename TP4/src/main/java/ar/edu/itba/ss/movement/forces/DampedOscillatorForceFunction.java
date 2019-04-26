package ar.edu.itba.ss.movement.forces;

import ar.edu.itba.ss.core.Coordinates;
import ar.edu.itba.ss.core.Neighbour;
import ar.edu.itba.ss.core.Particle;

import java.util.Set;
import java.util.function.BiFunction;

public class DampedOscillatorForceFunction implements BiFunction<Particle, Set<Neighbour>, Coordinates> {

        private final double k;
        private final double b;

        public DampedOscillatorForceFunction(final double k, final double b) {
            this.k = k;
            this.b = b;
        }

        @Override
        public Coordinates apply(final Particle particle, final Set<Neighbour> neighbours) {
            return apply(particle, k, b);
        }

        public static Coordinates apply(final Particle particle, final double k, final double b) {
            return particle.getPosition().multiply(-k).subtract(particle.getVelocity().multiply(b));
        }
    }
