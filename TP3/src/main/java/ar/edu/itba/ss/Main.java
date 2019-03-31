package ar.edu.itba.ss;

import java.util.List;

public class Main {
    public static void main( String[] args ){
        CliParser.parseOptions(args);
        ParticleGenerator particleGenerator = new ParticleGenerator();
        List<Particle> particles = particleGenerator.generateParticles(CliParser.numberOfParticles,CliParser.temperature,CliParser.L);
        BrownianMotion brownianMotion = new BrownianMotion(CliParser.L,CliParser.time,particles);
        brownianMotion.simulate();
    }
}
