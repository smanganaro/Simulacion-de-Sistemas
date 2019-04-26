package ar.edu.itba.ss.LennardJones;

import ar.edu.itba.ss.core.Neighbour;
import ar.edu.itba.ss.core.Particle;

import java.util.Map;
import java.util.Set;

public interface Printer {
    public void print(Map<Particle, Set<Neighbour>> particles, double t, int iteration);
}
