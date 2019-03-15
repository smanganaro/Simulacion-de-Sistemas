import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Cell {

    private List<Particle> particles;
    private List<Cell> neighbours;

    public Cell() {
        this.particles=new ArrayList<>();
        this.neighbours=new ArrayList<>();
    }

    public List<Particle> getParticles() {
        return particles;
    }

    public List<Cell> getNeighbours() {
        return neighbours;
    }

    public void addParticle (Particle particle){
        this.particles.add(particle);
    }

    public void addNeighbour (Cell cell){
        this.neighbours.add(cell);
    }
}
