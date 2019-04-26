package ar.edu.itba.ss.movement;

import ar.edu.itba.ss.core.Coordinates;
import ar.edu.itba.ss.core.Neighbour;
import ar.edu.itba.ss.core.Particle;

import java.util.Collections;
import java.util.Set;
import java.util.function.BiFunction;

public class GearMovementFunction implements MovementFunction {

    public static final double[] GEAR_5_VD_ALPHAS = new double[]{
            3.0 / 16.0,
            251.0 / 360.0,
            1.0,
            11.0 / 18.0,
            1.0 / 6.0,
            1.0 / 60.0,
    };
    public static final double[] GEAR_5_ALPHAS = new double[]{
            3.0 / 20.0,
            251.0 / 360.0,
            1.0,
            11.0 / 18.0,
            1.0 / 6.0,
            1.0 / 60.0,
    };
    private static final double[] factorials = new double[]{
            1,
            1,
            2,
            6,
            24,
            120
    };

    private final BiFunction<Particle, Set<Neighbour>, Coordinates> forceFunction;
    private final int order;
    private final double[] alphas;
    private final Coordinates[] r;
    private final Coordinates[] rp;

    public GearMovementFunction(final BiFunction<Particle, Set<Neighbour>, Coordinates> forceFunction,
                                final double[] alphas, final Coordinates[] r) {

        if (alphas.length != r.length) {
            throw new IllegalArgumentException("Dimensions don't match");
        }

        if (alphas.length > factorials.length) {
            throw new IllegalArgumentException("Order not supported");
        }

        this.forceFunction = forceFunction;
        this.order = alphas.length - 1;
        this.alphas = alphas;
        this.r = r;
        this.rp = new Coordinates[order + 1];
    }

    @Override
    public Particle move(final Particle currentParticle, final Set<Neighbour> neighbours,
                         final double dt) {

        for (int i = order; i >= 0; i--) {
            rp[i] = r[i];

            for (int j = i + 1, l = 1; j < order + 1; j++, l++) {
                rp[i] = rp[i].add(r[j].multiply(Math.pow(dt, l) / factorials[l]));
            }
        }

        final Particle predictedParticle = new Particle(currentParticle.getID(),rp[0],currentParticle.getRadius(),currentParticle.getMass(),
                rp[1], Collections.emptyList());


        final Coordinates deltaR2 = forceFunction.apply(predictedParticle, neighbours)
                .multiply(1.0 / currentParticle.getMass())
                .subtract(rp[2])
                .multiply(dt * dt / 2);

        for (int i = 0; i < order + 1; i++) {
            r[i] = rp[i].add(deltaR2.multiply(alphas[i] * factorials[i] / Math.pow(dt, i)));
        }

        return new Particle(currentParticle.getID(),rp[0],currentParticle.getRadius(),currentParticle.getMass(),
                rp[1], Collections.emptyList());
    }
}
