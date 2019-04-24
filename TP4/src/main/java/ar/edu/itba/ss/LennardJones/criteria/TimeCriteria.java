package ar.edu.itba.ss.LennardJones.criteria;

import ar.edu.itba.ss.LennardJones.core.Particle;
import java.util.Set;

public class TimeCriteria implements Criteria {

  private final double limit;

  public TimeCriteria(double limit) {
    this.limit = limit;
  }

  @Override
  public boolean test(double time, Set<Particle> particles) {
    return time > limit;
  }
}
