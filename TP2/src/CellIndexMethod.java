import java.util.*;

public class CellIndexMethod {

    private Grid grid;
    private Map<Particle, List<Particle>> particlesMapped;
    private Double timer;
    private Double interval;
    private Double Rc;


    public CellIndexMethod(Grid grid, double timer, double interval, double Rc ) {
        if(timer<0 || interval<0 || interval>timer){
            throw new IllegalArgumentException("Invalid time parameters");
        }
        if(Rc<0){
            throw new IllegalArgumentException("Invalid interaction radius");
        }
        this.particlesMapped = new HashMap<>();
        this.grid=grid;
        this.interval= interval;
        this.Rc = Rc;

    }

    public void run() {
        Integer M = grid.getM();

        for(int i=0; i<M; i++){

            for (int j=0; j<M; j++){

                for(Particle p : grid.getCell(i,j).getParticles()){
                    //Particulas en mi celda
                    for (Particle q: grid.getCell(i,j).getParticles()){

                        if(!p.equals(q)){
                            if(calculateDistance(p,q)<=Rc){
                                addToMap(p,q);
                            }
                        }
                    }

                    //Particulas en celdas adyacentes
                    for(Cell neighbour : grid.getCell(i,j).getNeighbours()){

                        for(Particle q : neighbour.getParticles()){

                            if(calculateDistance(p,q)<=Rc){
                                //Agrego ambas para aprovecharme de la simetria
                                addToMap(p,q);
                                addToMap(q,p);
                            }
                        }
                    }
                }
            }
        }
       // printParticlesNeighborsCount();
    }

    private void printParticlesNeighborsCount(){
        for (Map.Entry<Particle, List<Particle>> entry : particlesMapped.entrySet()) {
            StringBuilder stringBuilder = new StringBuilder();
            for(Particle p: entry.getValue()){
                stringBuilder.append(p);
                stringBuilder.append(',');
            }
            System.out.println(entry.getKey() + " has {" +stringBuilder.toString()+"} neighbors");
        }
    }

    private void addToMap(Particle p, Particle q) {
        if(!particlesMapped.containsKey(p)){
            particlesMapped.put(p, new ArrayList<Particle>());
        }
        particlesMapped.get(p).add(q);

    }

    /*public void simulate(){
        Double time = 0.0;
        while (time<=timer){
            time += interval;
            cellIndexrun();
        }
    }*/
    private double calculateDistance(Particle p, Particle q) {
        return Math.sqrt(Math.pow(p.getPosition().getX()-q.getPosition().getX(), 2) + Math.pow(p.getPosition().getY()-q.getPosition().getY(), 2))-p.getRadius()-q.getRadius();
    }
}
