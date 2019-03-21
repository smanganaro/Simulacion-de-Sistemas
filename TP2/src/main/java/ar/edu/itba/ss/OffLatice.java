package ar.edu.itba.ss;
import net.jafama.FastMath;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class OffLatice {
    private CellIndexMethod cellIndexMethod;
    private List<Particle> particles;
    private double totalTime;
    private double intervals;
    private double noiseAmp;

    public OffLatice(Grid grid, double rc, List<Particle> particles, double totalTime, double intervals, double noiseAmp) {
        if(totalTime<0 || intervals<0 || intervals>totalTime)
            throw new IllegalArgumentException("Invalid time parameters");
        this.cellIndexMethod = new CellIndexMethod(grid,rc);
        this.particles = particles;
        this.totalTime = totalTime;
        this.intervals = intervals;
        this.noiseAmp = noiseAmp;
    }

    public double run() throws IOException {
        double time = 0;
        printParticles(0);
        for(int i = 1;time<=totalTime;i++){
            simulate();
            Output.getInstance().write(particles,time);
            printParticles(i);
            time += intervals;
        }
        return calculateVa();
    }
    private void printParticles(int i){
        System.out.println(particles.size());
        System.out.println(i);
        for (Particle p: particles){
            System.out.println(p.getPosition().getX() + "\t" + p.getPosition().getY() + "\t" + p.getVelocity().getAngle());
        }
    }
    private void simulate(){
        Map<Particle, List<Particle>> mappedParticles = cellIndexMethod.getParticlesMapped();
        updateParticles(mappedParticles);
    }

    private void updateParticles(Map<Particle, List<Particle>> mappedParticles){
        for(Particle p: particles){
            cellIndexMethod.updatePosition(p,intervals);
            double avAngle = getAverageAngle(p, mappedParticles);
            p.setAngle(avAngle + (Math.random()-0.5)*noiseAmp);
        }
    }

    private double getAverageAngle(Particle p, Map<Particle, List<Particle>> mappedParticles){
        double totalSin = FastMath.sin(p.getVelocity().getAngle());
        double totalCos = FastMath.cos(p.getVelocity().getAngle());
        if(mappedParticles.containsKey(p)){
            for(Particle n: mappedParticles.get(p)){
                totalSin += FastMath.sin(n.getVelocity().getAngle());
                totalCos += FastMath.cos(n.getVelocity().getAngle());
            }
            totalSin /= mappedParticles.get(p).size()+1;
            totalCos /= mappedParticles.get(p).size()+1;
        }
        return Math.atan2(totalSin,totalCos);
    }

    private double calculateVa(){
        double xVel = 0;
        double yVel = 0;
        for(Particle p: particles){
            xVel += p.getVelocity().getX();
            yVel += p.getVelocity().getY();
        }
        return Math.sqrt(xVel*xVel+yVel*yVel)/particles.size();
    }
}
