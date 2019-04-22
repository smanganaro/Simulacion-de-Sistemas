package ar.edu.itba.ss.LennardJones.core.criteria;

import ar.edu.itba.ss.LennardJones.core.Particle;

import java.util.List;

public class TimeCriteria implements Criteria{
    private final double limit;

    public TimeCriteria(double limit) {
        this.limit = limit;
    }

    @Override
    public boolean test(double time, List<Particle> particles) {
        return time > limit;
    }
}
