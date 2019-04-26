package ar.edu.itba.ss.core.neigbour;

import ar.edu.itba.ss.core.Neighbour;
import ar.edu.itba.ss.core.Particle;

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

    public CellIndexMethod(List<Particle> particles, double L, double Rc, boolean periodic) {
        //if(periodic)
        //  this(new CircularGrid(L,Rc,particles),Rc);
        this(new LinearGrid(L,Rc,particles),Rc);
    }

    public CellIndexMethod(List<Particle> particles, double L, double Rc, double maxRadius, boolean periodic) {
        //if(periodic)
        //  this(new CircularGrid(L,Rc,particles),Rc);
        this(new LinearGrid(L,Rc,maxRadius,particles),Rc);
    }

    public Grid getGrid() {
        return grid;
    }

    public Double getRc() {
        return Rc;
    }

    public Map<Particle, Set<Neighbour>> getParticlesMapped() {
        Integer M = grid.getM();
        Double L = grid.getL();
        Map<Particle, Set<Neighbour>> particlesMapped = new HashMap<>();
        fillMap(particlesMapped);
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

    private void fillMap(Map<Particle,Set<Neighbour>> map){
        for(Particle p: grid.getParticles()){
            map.put(p, new HashSet<>());
        }
    }
    private void addToMap(Particle p, Particle q, Map<Particle, Set<Neighbour>> particlesMapped) {
        if(!particlesMapped.containsKey(p)){
            particlesMapped.put(p, new HashSet<>());
        }
        particlesMapped.get(p).add(new Neighbour(q,q.getDistance(p)));

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
