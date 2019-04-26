package ar.edu.itba.ss.LennardJones;

import ar.edu.itba.ss.core.neigbour.CellIndexMethod;
import ar.edu.itba.ss.movement.MovementFunction;
import ar.edu.itba.ss.core.Neighbour;
import ar.edu.itba.ss.criteria.Criteria;
import ar.edu.itba.ss.core.Coordinates;
import ar.edu.itba.ss.core.Particle;

import java.util.*;
import java.util.stream.Collectors;

public class Simulation {

  private final List<Particle> initialParticles;
  private final double dt;
  private final int writerIteration;
  private final double boxWidth;
  private final double boxHeight;
  private final double middleGap;
  private final double L;
  private final double rc;
  private final Map<Particle, MovementFunction> movementFunctions;

  public Simulation(final List<Particle> initialParticles, final double boxWidth,
                    final double boxHeight, final double middleGap, final double dt, final int writerIteration,
                    double rc,
                    final Map<Particle, MovementFunction> movementFunctions) {
    this.initialParticles = initialParticles;
    this.dt = dt;
    this.writerIteration = writerIteration;
    this.boxWidth = boxWidth;
    this.boxHeight = boxHeight;
    this.middleGap = middleGap;
    this.rc = rc;
    this.L = Math.max(boxHeight, boxWidth);
    this.movementFunctions = movementFunctions;
  }

  public Set<Particle> simulate(Criteria endCriteria) {
    double time = 0;
    int iteration = 1;
    List<Particle> particles = initialParticles;

    while (!endCriteria.test(time, particles)) {
      CellIndexMethod cellIndexMethod = new CellIndexMethod(particles,L, rc, particles.get(0).getRadius(), false);
      Map<Particle, Set<Neighbour>> neighbours = cellIndexMethod.getParticlesMapped();
      particles = nextParticles(neighbours);

      if (iteration == writerIteration) {
          iteration = 0;
          printParticles(particles,time, iteration);
      }

      time += dt;
      iteration++;
    }

    return new HashSet<>(particles);
  }

  private void printParticles(List<Particle> particles, double t, int iteration){
    System.out.println(particles.size() + 2);
    System.out.println(t);
    for (Particle p : particles){
      System.out.println(p.getPosition().getX() + "\t" + p.getPosition().getY() + "\t"
              + p.getVelocity().getX() + "\t" + p.getVelocity().getY() + "\t" + p.getRadius());
    }
    // Print two particles for fixed Simulation Box in Ovito animation
    System.out.println(0 + "\t" + 0 + "\t" + 0 + "\t" + 0 + "\t" + 0.001 + "\t" + 0);
    System.out.println(boxWidth + "\t" + boxHeight + "\t" + 0 + "\t" + 0 + "\t" + 0.001 + "\t" + 0);
  }

  private List<Particle> nextParticles(Map<Particle, Set<Neighbour>> neighbours) {
    List<Particle> nextParticles = new ArrayList<>(neighbours.size());

    for (Map.Entry<Particle, Set<Neighbour>> entry : neighbours.entrySet()) {
      nextParticles.add(moveParticle(entry.getKey(), entry.getValue()));
    }

    return nextParticles;
  }

  private Particle moveParticle(Particle particle, Set<Neighbour> neighbours) {
    neighbours = neighbours.stream()
        .filter(n -> !isWallBetween(particle, n.getNeighbourParticle()))
        .collect(Collectors.toSet());
    addWallParticles(particle, neighbours);

    MovementFunction function = movementFunctions.get(particle);
    return function.move(particle, neighbours, dt);
  }

  private void addWallParticles(Particle particle, Set<Neighbour> neighbours) {
    int wallId = -1;
    final double gapStart = (boxHeight / 2) - (middleGap / 2);
    final double gapEnd = boxHeight - gapStart;
    // up wall
    double distanceToExtremeWall = boxHeight - particle.getPosition().getY();
    double distanceToMiddleWall = gapEnd - particle.getPosition().getY();
    if (particle.getPosition().getX() == boxWidth / 2 && distanceToMiddleWall <= rc
            && distanceToMiddleWall >= 0) {
//      neighbours.add(new Neighbour(ImmutableParticle.builder()
//          .id(wallId--).position(new Coordinates(boxWidth / 2, gapEnd))
//          .mass(Double.POSITIVE_INFINITY)
//          .velocity(Coordinates.ZERO).build(), distanceToMiddleWall));
    } else if (distanceToExtremeWall <= rc) {
      Coordinates c = new Coordinates(particle.getPosition().getX(), boxHeight);
      neighbours.add(new Neighbour(new Particle(wallId--,c,0,Double.POSITIVE_INFINITY,Collections.emptyList()), distanceToExtremeWall));
    }

    // down wall
    distanceToExtremeWall = particle.getPosition().getY();
    distanceToMiddleWall = particle.getPosition().getY() - gapStart;
    if (particle.getPosition().getX() == boxWidth / 2 && distanceToMiddleWall <= rc
            && distanceToMiddleWall >= 0) {
//      neighbours.add(new Neighbour(ImmutableParticle.builder()
//          .id(wallId--).position(new Coordinates(boxWidth / 2, gapStart))
//          .mass(Double.POSITIVE_INFINITY)
//          .velocity(Coordinates.ZERO).build(), distanceToMiddleWall));
    } else if (distanceToExtremeWall <= rc) {
      Coordinates c = new Coordinates(particle.getPosition().getX(), 0);
      neighbours.add(new Neighbour(new Particle(wallId--,c,0,Double.POSITIVE_INFINITY,Collections.emptyList()), distanceToExtremeWall));
    }

    // left wall
    distanceToExtremeWall = particle.getPosition().getX();
    distanceToMiddleWall = distanceToExtremeWall - boxWidth / 2;
    if (distanceToMiddleWall > 0 && distanceToMiddleWall <= rc &&
            (particle.getPosition().getY() <= gapStart || particle.getPosition().getY() >= gapEnd)
//        && !(particle.velocity().getX() > 0 && particle.position().getY() > gapStart - 5
//            && particle.position().getY() < gapEnd + 5)
    ) {
      Coordinates c = new Coordinates(boxWidth / 2, particle.getPosition().getY());
      neighbours.add(new Neighbour(new Particle(wallId--,c,0,Double.POSITIVE_INFINITY,Collections.emptyList()), distanceToMiddleWall));
    } else if (distanceToExtremeWall <= rc) {
      Coordinates c = new Coordinates(0, particle.getPosition().getY());
      neighbours.add(new Neighbour(new Particle(wallId--,c,0,Double.POSITIVE_INFINITY,Collections.emptyList()), distanceToExtremeWall));
    }

    // right wall
    distanceToExtremeWall = boxWidth - particle.getPosition().getX();
    distanceToMiddleWall = distanceToExtremeWall - boxWidth / 2;
    if (distanceToMiddleWall > 0 && distanceToMiddleWall <= rc &&
            (particle.getPosition().getY() <= gapStart || particle.getPosition().getY() >= gapEnd)
//        && !(particle.velocity().getX() < 0 && particle.position().getY() > gapStart - 0.005
//            && particle.position().getY() < gapEnd + 0.005)
    ) {
      Coordinates c = new Coordinates(boxWidth / 2, particle.getPosition().getY());
      neighbours.add(new Neighbour(new Particle(wallId--,c,0,Double.POSITIVE_INFINITY, Collections.emptyList()), distanceToMiddleWall));
    } else if (distanceToExtremeWall <= rc) {
      Coordinates c = new Coordinates(boxWidth, particle.getPosition().getY());
      neighbours.add(new Neighbour(new Particle(wallId--,c,0,Double.POSITIVE_INFINITY,Collections.emptyList()), distanceToExtremeWall));
    }

    if (particle.getPosition().getY() > gapStart && particle.getPosition().getY() < gapEnd) {
      final Coordinates gapStartPosition = new Coordinates(boxWidth / 2, gapStart);
      final Coordinates gapEndPosition = new Coordinates(boxWidth / 2, gapEnd);
      final double gapStartDistance = particle.getPosition().getDistance(gapStartPosition);
      final double gapEndDistance = particle.getPosition().getDistance(gapEndPosition);

      if (gapStartDistance <= rc) {
        neighbours.add(new Neighbour(new Particle(wallId--,gapStartPosition,0,Double.POSITIVE_INFINITY,Collections.emptyList()), gapStartDistance));
      }

      if (gapEndDistance <= rc) {
        neighbours.add(new Neighbour(new Particle(wallId--,gapEndPosition,0,Double.POSITIVE_INFINITY,Collections.emptyList()), gapEndDistance));
      }
    }
  }

  private boolean isWallBetween(Particle particle1, Particle particle2) {
    final double x1 = particle1.getPosition().getX();
    final double x2 = particle2.getPosition().getX();
    final double y1 = particle1.getPosition().getY();
    final double y2 = particle2.getPosition().getY();

    if (x1 == x2) {
      return false;
    }

    final double m = (y2 - y1) / (x2 - x1);
    final double b = y1 - m * x1;
    final double xp = boxWidth / 2;
    final double yp = m * xp + b;

    final double gapStart = (boxHeight / 2) - (middleGap / 2);
    final double gapEnd = boxHeight - gapStart;

    return yp > gapStart && yp < gapEnd &&
            ((x1 > boxWidth / 2 && x2 < boxWidth / 2) ||
                    (x1 < boxWidth / 2 && x2 > boxWidth / 2));
  }
}
