package ar.edu.itba.ss.LennardJones;

import ar.edu.itba.ss.LennardJones.core.*;
import ar.edu.itba.ss.LennardJones.core.criteria.Criteria;
import ar.edu.itba.ss.LennardJones.core.criteria.TimeCriteria;
import ar.edu.itba.ss.LennardJones.core.force.LennardJonesForceFunction;
import ar.edu.itba.ss.LennardJones.core.movement.BeemanMovementFunction;
import ar.edu.itba.ss.LennardJones.core.movement.EulerMovementFunction;
import ar.edu.itba.ss.LennardJones.core.movement.MovementFunction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class LennardJones {
    private static final int N = 1000;
    private static final double MASS = 0.1;
    private static final double RADIUS = 2;
    private static final double INITIAL_VELOCITY_MAGNITUDE = 10;
    private static final double DT = 0.0001;


    private static final double BOX_HEIGHT = 200;
    private static final double BOX_WIDTH = 400;
    private static final double BOX_GAP = 10;

    private static final double RC = 5;
    private static final double EPSILON = 2;
    private static final double RM = 1;

    private static final BiFunction<Particle, List<Particle>, Coordinates> FORCE_FUNCTION = new LennardJonesForceFunction(EPSILON, RM);

    public static void runBeemanTime(double time){
        List<Particle> previousParticles = new ParticleGenerator(RADIUS,MASS,INITIAL_VELOCITY_MAGNITUDE).generateParticles(N,BOX_WIDTH/2,BOX_HEIGHT);
        final LinearGrid linearGrid = new LinearGrid(Math.max(BOX_WIDTH,BOX_HEIGHT),RC,RADIUS,previousParticles);
        final CellIndexMethod cellIndexMethod = new CellIndexMethod(linearGrid,RC);
        final Map<Particle, List<Particle>> previousNeighbours = cellIndexMethod.getParticlesMapped();
        final List<Particle> currentParticles = new ArrayList<>(previousParticles.size());
        final Map<Particle, MovementFunction> movementFunctions = new HashMap<>(previousParticles.size());
        beemanAddCurrentParticlesAndMovementFunctions(previousParticles, previousNeighbours,
                currentParticles, movementFunctions);

        final Simulation simulation = new Simulation(currentParticles, BOX_WIDTH, BOX_HEIGHT, BOX_GAP, DT, RC, movementFunctions);
        final Criteria criteria = new TimeCriteria(time);
        simulation.run(criteria);
    }

    private static void beemanAddCurrentParticlesAndMovementFunctions(final List<Particle> previousParticles,
                                                                      final Map<Particle, List<Particle>> previousNeighbours,
                                                                      final List<Particle> currentParticles,
                                                                      final Map<Particle, MovementFunction> movementFunctions) {
        for (final Particle previousParticle : previousParticles) {
            final Coordinates previousAcceleration = FORCE_FUNCTION.apply(previousParticle, previousNeighbours.get(previousParticle))
                    .multiply(1.0 / previousParticle.getMass());

            final Particle currentParticle = EulerMovementFunction.move(previousParticle, previousNeighbours.get(previousParticle), DT, FORCE_FUNCTION);

            final MovementFunction movementFunction = new BeemanMovementFunction(FORCE_FUNCTION, previousAcceleration);

            currentParticles.add(currentParticle);
            movementFunctions.put(currentParticle, movementFunction);
        }
    }

    private static void eulerAddCurrentParticlesAndMovementFunctions(final List<Particle> previousParticles,
                                                                     final List<Particle> currentParticles,
                                                                     final Map<Particle, MovementFunction> movementFunctions) {

        final MovementFunction movementFunction = new EulerMovementFunction(FORCE_FUNCTION);
        for (final Particle previousParticle : previousParticles) {
            currentParticles.add(previousParticle);
            movementFunctions.put(previousParticle, movementFunction);
        }
    }
}
