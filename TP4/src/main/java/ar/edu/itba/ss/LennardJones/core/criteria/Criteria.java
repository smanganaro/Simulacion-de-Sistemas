package ar.edu.itba.ss.LennardJones.core.criteria;

import ar.edu.itba.ss.LennardJones.core.Particle;

import java.util.ArrayList;
import java.util.List;

public interface Criteria {

    public boolean test(final double time, final List<Particle> particles);
}
