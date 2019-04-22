package ar.edu.itba.ss.LennardJones.core;
import java.util.*;

public class CellIndexMethod {

    private Grid grid;
    private Double Rc;

    public CellIndexMethod(Grid grid, double Rc ) {
        if(Rc<0){
            throw new IllegalArgumentException("Invalid interaction radius");
        }

        this.grid=grid;
        this.Rc = Rc;

    }

    public Grid getGrid() {
        return grid;
    }

    public Double getRc() {
        return Rc;
    }

    public Map<Particle, List<Particle>> getParticlesMapped() {
        Integer M = grid.getM();
        Double L = grid.getL();
        Map<Particle, List<Particle>> particlesMapped = new HashMap<>();

        for(int i=0; i<M; i++){
            for (int j=0; j<M; j++){
                for(Particle p : grid.getCell(i,j).getParticles()){
                    //Particulas en mi celda
                    for (Particle q: grid.getCell(i,j).getParticles()) {
                        if (!p.equals(q)) {
                            if (calculateDistance(p, q, L) <= Rc) {
                                addToMap(p, q, particlesMapped);
                            }
                        }
                    }
                    //Particulas en celdas adyacentes
                    for(Cell neighbour : grid.getCell(i,j).getNeighbours()){
                        for(Particle q : neighbour.getParticles()){
                            if(calculateDistance(p,q,L)<=Rc){
                                //Agrego ambas para aprovecharme de la simetria
                                addToMap(p,q,particlesMapped);
                                addToMap(q,p,particlesMapped);
                            }
                        }
                    }
                }
            }
        }
       // printParticlesNeighborsCount(particlesMapped); DEBUG
        return particlesMapped;
    }


    private void addToMap(Particle p, Particle q, Map<Particle, List<Particle>> particlesMapped) {
        if(!particlesMapped.containsKey(p)){
            particlesMapped.put(p, new ArrayList<Particle>());
        }
        particlesMapped.get(p).add(q);

    }

    private double calculateDistance(Particle p, Particle q, Double L) {
        double dist = p.getDistance(q);
        //if(grid instanceof CicleGrid)
        //    dist = p.getPeriodicDistance(q,L);

        //Hasta ahora tenemos la distancia entre centros de masa,
        //con esta resta podemos saber si las particulas estan dentro de Rc
        dist = dist-p.getRadius()-q.getRadius();
        return dist;
    }

    public void updatePosition(Particle p, Double interval){
        double cellLength = grid.getL()/grid.getM();

        double prevX = p.getPosition().getX();
        double prevY = p.getPosition().getY();
        p.updatePos(interval);

        double newX = p.getPosition().getX();
        double newY = p.getPosition().getY();

        int cellX = (int) Math.floor(prevX/cellLength);
        int cellY = (int) Math.floor(prevY/cellLength);

        int newCellX = (int)Math.floor(newX/cellLength);
        int newCellY = (int)Math.floor(newY/cellLength);
        if(newCellX != cellX ||newCellY != cellY){
            grid.getCell(cellX, cellY).getParticles().remove(p);

            newX = mod(newX, grid.getL());
            newY = mod(newY, grid.getL());
            p.setPosition(newX, newY);

            grid.insertParticle(p);
        }
    }

    private double mod(double x, double n){
        double r = x % n;
        if (r < 0)
            r += n;

        return r;
    }

    private void printParticlesNeighborsCount(Map<Particle, List<Particle>> particlesMapped){
        for (Map.Entry<Particle, List<Particle>> entry : particlesMapped.entrySet()) {
            StringBuilder stringBuilder = new StringBuilder();
            for(Particle pe: entry.getValue()){
                stringBuilder.append(pe);
                stringBuilder.append(',');
            }
            System.out.println(entry.getKey() + " has {" +stringBuilder.toString()+"} neighbors");
        }
    }
}
