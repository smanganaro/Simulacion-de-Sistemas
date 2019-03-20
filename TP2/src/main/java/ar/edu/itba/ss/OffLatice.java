package ar.edu.itba.ss;
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

    public double run(){
        double time = 0;
        while(time<=totalTime){
            simulate();
            Output.getInstance().write(particles,time);
            time += intervals;
        }
        return calculateVa();
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
        double totalSin = Math.sin(p.getVelocity().getAngle());
        double totalCos = Math.cos(p.getVelocity().getAngle());
        if(mappedParticles.containsKey(p)){
            for(Particle n: mappedParticles.get(p)){
                totalSin += Math.sin(n.getVelocity().getAngle());
                totalCos += Math.cos(n.getVelocity().getAngle());
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
        return Math.sqrt(xVel*xVel+yVel*yVel)/particles.size();//ESTO ESTA BIEN EL SIZE???
    }
}