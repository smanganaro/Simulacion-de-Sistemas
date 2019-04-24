package ar.edu.itba.ss.LennardJones.movement;

import ar.edu.itba.ss.LennardJones.core.Neighbour;
import ar.edu.itba.ss.LennardJones.core.Particle;
import java.util.Set;

public interface MovementFunction {

  Particle move(final Particle currentParticle, final Set<Neighbour> neighbours, final double dt);
}
