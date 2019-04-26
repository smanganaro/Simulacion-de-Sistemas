package ar.edu.itba.ss.movement;

import ar.edu.itba.ss.core.Neighbour;
import ar.edu.itba.ss.core.Particle;
import java.util.Set;

public interface MovementFunction {

  Particle move(final Particle currentParticle, final Set<Neighbour> neighbours, final double dt);
}
