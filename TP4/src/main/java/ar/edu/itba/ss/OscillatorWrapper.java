package ar.edu.itba.ss;

import ar.edu.itba.ss.core.Coordinates;
import ar.edu.itba.ss.core.Particle;
import ar.edu.itba.ss.movement.*;
import ar.edu.itba.ss.movement.forces.DampedOscillatorForceFunction;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class OscillatorWrapper {
    private double MASS = 70;
    private double K = 10000;
    private double B = 100;
    private double DT = 0.01;
    private double TIMES = 2.5 / DT;

    private DampedOscillatorForceFunction FORCE_FUNCTION;

    public OscillatorWrapper(double MASS, double k, double b, double DT, double TIMES) {
        this.MASS = MASS;
        this.K = k;
        this.B = b;
        this.DT = DT;
        this.TIMES = TIMES;
        this.FORCE_FUNCTION = new DampedOscillatorForceFunction(K, B);

        final Particle particle = new Particle(1,new Coordinates(1, 0),0,MASS, new Coordinates(-B / (2 * MASS), 0), Collections.emptyList());
        final MovementFunction eulerFunction = new EulerMovementFunction(FORCE_FUNCTION);

        final NumericalOscillator numericalOscillator = new NumericalOscillator(particle, K, B, eulerFunction);

        final List<Coordinates> pointsAnalytic = AnalyticCoordinates(particle);


        final List<Coordinates> pointsEuler = EulerCoordinates(particle, numericalOscillator);
        System.out.println("Euler mse: " + quadraticMeanError(pointsAnalytic, pointsEuler));

        final List<Coordinates> pointsVerlet = VerletCoordinates(particle, numericalOscillator);
        System.out.println("Verlet mse: " + quadraticMeanError(pointsAnalytic, pointsVerlet));

        final List<Coordinates> pointsBeeman = BeemanCoordinates(particle, numericalOscillator);
        System.out.println("Beeman mse: " + quadraticMeanError(pointsAnalytic, pointsBeeman));

        final List<Coordinates> pointsGear = Gear5Coordinates(particle);
        System.out.println("Gear mse: " + quadraticMeanError(pointsAnalytic, pointsGear));

    }

    public OscillatorWrapper(){
        this(70,10000,100,0.01,2.5/0.01);
    }

    private  double quadraticMeanError(final List<Coordinates> pointsAnalytic,
                                             final List<Coordinates> pointsNumerical) {

        double quadraticError = 0;
        for (int i = 0; i < pointsAnalytic.size(); i++) {
            final double error = pointsAnalytic.get(i).getY() - pointsNumerical.get(i).getY();
            quadraticError += error * error;
        }

        return quadraticError / pointsAnalytic.size();
    }

    private  List<Coordinates> EulerCoordinates(final Particle particle,
                                                final NumericalOscillator numericalOscillator) {

        Particle nextParticle = particle;
        final List<Coordinates> pointsEuler = new LinkedList<>();
        for (int i = 1; i <= TIMES; i++) {
            nextParticle = numericalOscillator.move(nextParticle, DT);
            pointsEuler.add(new Coordinates(DT * i, nextParticle.getPosition().getX()));
        }

        return pointsEuler;
    }

    private List<Coordinates> AnalyticCoordinates(final Particle particle) {
        final AnalyticOscillator analyticOscillator = new AnalyticOscillator(particle, K, B);

        Particle nextParticle = analyticOscillator.move(particle, DT);
        final List<Coordinates> pointsAnalytic = new LinkedList<>();
        for (int i = 1; i <= TIMES; i++) {
            nextParticle = analyticOscillator.move(nextParticle, DT);
            pointsAnalytic.add(new Coordinates(DT * i, nextParticle.getPosition().getX()));
        }

        return pointsAnalytic;
    }

    private List<Coordinates> VerletCoordinates(final Particle particle,
                                                final NumericalOscillator eulerUDHOscillator) {
        final MovementFunction verletFunction = new VerletMovementFunction(FORCE_FUNCTION, particle.getPosition());

        final NumericalOscillator verletUDHOscillator = new NumericalOscillator(particle, K, B,
                verletFunction);

        Particle nextParticle = eulerUDHOscillator.move(particle, DT);
        final List<Coordinates> pointsVerlet = new LinkedList<>();
        for (int i = 1; i <= TIMES; i++) {
            nextParticle = verletUDHOscillator.move(nextParticle, DT);
            pointsVerlet.add(new Coordinates(DT * i, nextParticle.getPosition().getX()));
        }

        return pointsVerlet;
    }

    private List<Coordinates> BeemanCoordinates(final Particle particle,
                                                final NumericalOscillator eulerUDHOscillator) {
        final MovementFunction beemanFunction = new VDBeemanMovementFunction(FORCE_FUNCTION,
                FORCE_FUNCTION.apply(particle, null).multiply(1.0 / particle.getMass()));

        final NumericalOscillator beemanUDHOscillator = new NumericalOscillator(particle, K, B,
                beemanFunction);

        Particle nextParticle = eulerUDHOscillator.move(particle, DT);
        final List<Coordinates> pointsBeeman = new LinkedList<>();
        for (int i = 1; i <= TIMES; i++) {
            nextParticle = beemanUDHOscillator.move(nextParticle, DT);
            pointsBeeman.add(new Coordinates(DT * i, nextParticle.getPosition().getX()));
        }

        return pointsBeeman;
    }

    private List<Coordinates> Gear5Coordinates(final Particle particle) {
        final Coordinates[] r = new Coordinates[5 + 1];
        r[0] = particle.getPosition();
        r[1] = particle.getVelocity();
        for (int i = 2; i < 5 + 1; i++) {
            r[i] = r[i - 2].multiply(-K).subtract(r[i - 1].multiply(B)).multiply(1.0 / particle.getMass());
        }

        final MovementFunction gearFunction = new GearMovementFunction(FORCE_FUNCTION, GearMovementFunction.GEAR_5_VD_ALPHAS, r);

        final NumericalOscillator gearUDHOscillator = new NumericalOscillator(particle, K, B, gearFunction);

        Particle nextParticle = particle;
        final List<Coordinates> pointsGear = new LinkedList<>();
        for (int i = 1; i <= TIMES; i++) {
            nextParticle = gearUDHOscillator.move(nextParticle, DT);
            pointsGear.add(new Coordinates(DT * i, nextParticle.getPosition().getX()));
        }

        return pointsGear;
    }

}
