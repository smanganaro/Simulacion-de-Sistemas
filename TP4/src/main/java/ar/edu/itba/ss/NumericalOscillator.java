package ar.edu.itba.ss;

import ar.edu.itba.ss.core.Particle;
import ar.edu.itba.ss.movement.MovementFunction;

public class NumericalOscillator extends AnalyticOscillator {
    private final MovementFunction movementFunction;

    public NumericalOscillator(final Particle particle, final double k,
                               final double b, final MovementFunction movementFunction) {
        super(particle, k, b);
        this.movementFunction = movementFunction;
    }

    @Override
    public Particle move(final Particle particle, final double dt) {
        return movementFunction.move(particle, null, dt);
    }
}
