package ar.edu.itba.ss.LennardJones;

import ar.edu.itba.ss.LennardJones.core.CellIndexMethod;
import ar.edu.itba.ss.LennardJones.core.Coordinates;
import ar.edu.itba.ss.LennardJones.core.LinearGrid;
import ar.edu.itba.ss.LennardJones.core.Particle;
import ar.edu.itba.ss.LennardJones.core.criteria.Criteria;
import ar.edu.itba.ss.LennardJones.core.movement.MovementFunction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Simulation {
    private final List<Particle> particles;
    private final double dt;
    private final double boxWidth;
    private final double boxHeight;
    private final double middleGap;
    private final CellIndexMethod cim;
    private final double rc;
    private final double L;
    private final LinearGrid linearGrid;
    private final Map<Particle, MovementFunction> movementFunctions;

    public Simulation(final List<Particle> particles, final double boxWidth,
                      final double boxHeight, final double middleGap, final double dt,
                      double rc, final Map<Particle, MovementFunction> movementFunctions) {
        this.L = boxHeight > boxWidth ? boxHeight : boxWidth;
        double maxRadius = getMaxRadius(particles);
        int M = (int) Math.floor(L / (rc + 2 * maxRadius));

        this.particles = particles;
        this.dt = dt;
        this.boxWidth = boxWidth;
        this.boxHeight = boxHeight;
        this.middleGap = middleGap;
        this.rc = rc;

        this.linearGrid = new LinearGrid(L,M, particles);
        this.cim = new CellIndexMethod(linearGrid,rc);
        this.movementFunctions = movementFunctions;
    }

    public List<Particle> run(Criteria endCriteria) {
        double time = 0;
        int iteration = 0;

        while (!endCriteria.test(time, particles)) {
            Map<Particle, List<Particle>> neighbours = cim.getParticlesMapped();
            moveParticles(neighbours);

            //print particles
            printParticles(time);
            time += dt;
            iteration++;
        }

        return new ArrayList<>(particles);
    }

    private void printParticles(double t){
        System.out.println(particles.size() + 2);
        for (Particle p : particles){
            System.out.println(p.getPosition().getX() + "\t" + p.getPosition().getY() + "\t"
                    + p.getVelocity().getX() + "\t" + p.getVelocity().getY() + "\t" + p.getRadius() + "\t" + t);
        }
        // Print two particles for fixed Simulation Box in Ovito animation
        System.out.println(0 + "\t" + 0 + "\t" + 0 + "\t" + 0 + "\t" + 0.001 + "\t" + 0);
        System.out.println(L + "\t" + L + "\t" + 0 + "\t" + 0 + "\t" + 0.001 + "\t" + 0);
    }

    private void moveParticles(Map<Particle, List<Particle>> neighbours) {
        for (Map.Entry<Particle, List<Particle>> entry : neighbours.entrySet()) {
            Particle new_particle = moveParticle(entry.getKey(), entry.getValue());
            Particle old_particle = entry.getKey();
            updateParticle(old_particle,new_particle);
        }
    }

    private void updateParticle(Particle oldP, Particle newP){
        cim.updatePosition(oldP,newP.getPosition());
        oldP.setVelocity(newP.getVelocity());
    }

    private Particle moveParticle(Particle particle, List<Particle> neighbours) {
        List<Particle> neighbours_inside = neighbours.stream()
                .filter(n -> !isWallBetween(particle, n))
                .collect(Collectors.toList());

        addWallParticles(particle, neighbours_inside);

        MovementFunction function = movementFunctions.get(particle);
        return function.move(particle, neighbours_inside, dt);
    }

    private void addWallParticles(Particle particle, List<Particle> neighbours) {
        int wallId = -1;
        final double gapStart = (boxHeight / 2) - (middleGap / 2);
        final double gapEnd = boxHeight - gapStart;
        List<Particle> particles = Collections.emptyList();
        // up wall
        double distanceToExtremeWall = boxHeight - particle.getPosition().getY();
        double distanceToMiddleWall = gapEnd - particle.getPosition().getY();

        if (particle.getPosition().getX() == boxWidth / 2 && distanceToMiddleWall <= rc
                && distanceToMiddleWall >= 0) {
        } else if (distanceToExtremeWall <= rc) {
            Coordinates coordinates = new Coordinates(particle.getPosition().getX(),boxHeight);
            neighbours.add(new Particle(wallId--, coordinates,0,Double.POSITIVE_INFINITY,particles));
        }

        // down wall
        distanceToExtremeWall = particle.getPosition().getY();
        distanceToMiddleWall = particle.getPosition().getY() - gapStart;
        if (particle.getPosition().getX() == boxWidth / 2 && distanceToMiddleWall <= rc
                && distanceToMiddleWall >= 0) {
        } else if (distanceToExtremeWall <= rc) {
            Coordinates coordinates = new Coordinates(particle.getPosition().getX(),0);
            neighbours.add(new Particle(wallId--, coordinates,0,Double.POSITIVE_INFINITY,particles));
        }

        // left wall
        distanceToExtremeWall = particle.getPosition().getX();
        distanceToMiddleWall = distanceToExtremeWall - boxWidth / 2;
        if (distanceToMiddleWall > 0 && distanceToMiddleWall <= rc &&
                (particle.getPosition().getY() <= gapStart || particle.getPosition().getY() >= gapEnd)) {
            Coordinates coordinates = new Coordinates(boxWidth / 2, particle.getPosition().getY());
            neighbours.add(new Particle(wallId--, coordinates,0,Double.POSITIVE_INFINITY,particles));
        } else if (distanceToExtremeWall <= rc) {
            Coordinates coordinates = new Coordinates(0, particle.getPosition().getY());
            neighbours.add(new Particle(wallId--, coordinates,0,Double.POSITIVE_INFINITY,particles));
        }

        // right wall
        distanceToExtremeWall = boxWidth - particle.getPosition().getX();
        distanceToMiddleWall = distanceToExtremeWall - boxWidth / 2;
        if (distanceToMiddleWall > 0 && distanceToMiddleWall <= rc &&
                (particle.getPosition().getY() <= gapStart || particle.getPosition().getY() >= gapEnd)) {
            Coordinates coordinates = new Coordinates(boxWidth / 2, particle.getPosition().getY());
            neighbours.add(new Particle(wallId--, coordinates,0,Double.POSITIVE_INFINITY,particles));
        } else if (distanceToExtremeWall <= rc) {
            Coordinates coordinates = new Coordinates(boxWidth, particle.getPosition().getY());
            neighbours.add(new Particle(wallId--, coordinates,0,Double.POSITIVE_INFINITY,particles));
        }

        if (particle.getPosition().getY() > gapStart && particle.getPosition().getY() < gapEnd) {
            final Coordinates gapStartPosition = new Coordinates(boxWidth / 2, gapStart);
            final Coordinates gapEndPosition = new Coordinates(boxWidth / 2, gapEnd);
            final double gapStartDistance = gapStartPosition.getDistance(particle.getPosition());
            final double gapEndDistance = gapEndPosition.getDistance(particle.getPosition());

            if (gapStartDistance <= rc) {
                neighbours.add(new Particle(wallId--, gapStartPosition,0,Double.POSITIVE_INFINITY,particles));
            }

            if (gapEndDistance <= rc) {
                neighbours.add(new Particle(wallId--, gapEndPosition,0,Double.POSITIVE_INFINITY,particles));
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

    private double getMaxRadius(List<Particle> particles){
        double max = 0;
        for(Particle p : particles){
            if(p.getRadius() > max)
                max = p.getRadius();
        }
        return max;
    }
}
