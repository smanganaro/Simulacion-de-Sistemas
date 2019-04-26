package ar.edu.itba.ss;

import ar.edu.itba.ss.core.Coordinates;
import ar.edu.itba.ss.core.Particle;

import java.util.Collections;

public class AnalyticOscillator {
    private final double b;
    private final double k;

    private Particle particle;
    private double time;

    public AnalyticOscillator(final Particle particle, final double k, final double b) {

        if (particle.getVelocity().getY() != 0) {
            throw new IllegalArgumentException("Movement has to be un 1D");
        }

        this.particle = particle;
        this.k = k;
        this.b = b;
        this.time = 0;
    }

    public Particle move(final Particle particle, final double dt) {
        time += dt;

        final double gamma = b / (2 * particle.getMass());
        final double wa = Math.sqrt(k / particle.getMass() - gamma * gamma);

        final Coordinates newPosition = new Coordinates(Math.exp(-gamma * time) * Math.cos(wa * time),
                particle.getPosition().getY());

        return new Particle(particle.getID(),newPosition,particle.getRadius(),particle.getMass(),
                particle.getVelocity(), Collections.emptyList());
    }
}
