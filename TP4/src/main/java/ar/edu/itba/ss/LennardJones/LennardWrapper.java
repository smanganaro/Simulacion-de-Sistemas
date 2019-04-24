package ar.edu.itba.ss.LennardJones;


import ar.edu.itba.ss.LennardJones.core.ParticleGenerator;
import ar.edu.itba.ss.LennardJones.movement.BeemanMovementFunction;
import ar.edu.itba.ss.LennardJones.movement.EulerMovementFunction;
import ar.edu.itba.ss.LennardJones.movement.LennardJonesForceFunction;
import ar.edu.itba.ss.LennardJones.movement.MovementFunction;
import ar.edu.itba.ss.LennardJones.core.neigbour.CellIndexMethod;
import ar.edu.itba.ss.LennardJones.core.Neighbour;
import ar.edu.itba.ss.LennardJones.core.Particle;
import ar.edu.itba.ss.LennardJones.criteria.Criteria;
import ar.edu.itba.ss.LennardJones.criteria.TimeCriteria;
import javafx.geometry.Point2D;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;


public class LennardWrapper {

  private static final int N = 1000;
  private static final double MASS = 0.1;
  private static final double RADIUS = 2;
  private static final double INITIAL_VELOCITY_MAGNITUDE = 10;
  private static final double DT = 0.0001;
  private static final int WRITER_ITERATION = (int) (1 / DT) / 10;

  private static final double BOX_HEIGHT = 200;
  private static final double BOX_WIDTH = 400;
  private static final double BOX_GAP = 10;

  private static final double RC = 5;
  private static final double EPSILON = 2;
  private static final double RM = 1;

  private static final BiFunction<Particle, Set<Neighbour>, Point2D> FORCE_FUNCTION = new LennardJonesForceFunction(EPSILON, RM);

  private static final CellIndexMethod cellIndexMethod = new CellIndexMethod(Math.max(BOX_HEIGHT, BOX_WIDTH), false);

  public static void runBeemanTime(double time) {
    List<Particle> previousParticles = new ParticleGenerator(RADIUS,MASS,INITIAL_VELOCITY_MAGNITUDE).generateParticles(N,BOX_WIDTH/2,BOX_HEIGHT);
    previousParticles = previousParticles.stream()
            .map(p ->new Particle(p.getID(),p.getPosition(),0,p.getMass(),
                    p.getVelocity(), Collections.emptyList()))
            .collect(Collectors.toList());

    final Map<Particle, Set<Neighbour>> previousNeighbours = cellIndexMethod.apply(previousParticles, previousParticles.get(0).getRadius(), RC);

    final List<Particle> currentParticles = new ArrayList<>(previousParticles.size());
    final Map<Particle, MovementFunction> movementFunctions = new HashMap<>(previousParticles.size());

    beemanAddCurrentParticlesAndMovementFunctions(previousParticles, previousNeighbours,
        currentParticles, movementFunctions);

    final Simulation simulator = new Simulation(currentParticles,
        BOX_WIDTH, BOX_HEIGHT, BOX_GAP, DT, WRITER_ITERATION, RC, movementFunctions);

    final Criteria criteria = new TimeCriteria(time);

    simulator.simulate(criteria);
  }

  private static void beemanAddCurrentParticlesAndMovementFunctions(
      final List<Particle> previousParticles,
      final Map<Particle, Set<Neighbour>> previousNeighbours, final List<Particle> currentParticles,
      final Map<Particle, MovementFunction> movementFunctions) {
    for (final Particle previousParticle : previousParticles) {
      final Point2D previousAcceleration = FORCE_FUNCTION
          .apply(previousParticle, previousNeighbours.get(previousParticle))
          .multiply(1.0 / previousParticle.getMass());

      final Particle currentParticle = EulerMovementFunction
          .move(previousParticle, previousNeighbours.get(previousParticle), DT, FORCE_FUNCTION);

      final MovementFunction movementFunction = new BeemanMovementFunction(FORCE_FUNCTION,
          previousAcceleration);

      currentParticles.add(currentParticle);
      movementFunctions.put(currentParticle, movementFunction);
    }
  }

  private static void eulerAddCurrentParticlesAndMovementFunctions(
      final List<Particle> previousParticles,
      final Map<Particle, Set<Neighbour>> previousNeighbours, final List<Particle> currentParticles,
      final Map<Particle, MovementFunction> movementFunctions) {

    final MovementFunction movementFunction = new EulerMovementFunction(FORCE_FUNCTION);
    for (final Particle previousParticle : previousParticles) {
      currentParticles.add(previousParticle);
      movementFunctions.put(previousParticle, movementFunction);
    }
  }

}
