package ar.edu.itba.ss.core.neigbour;

import ar.edu.itba.ss.core.Particle;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Cell {

    private List<Particle> particles;
    private Set<Cell> neighbours;
    private int ID;
    private static int counter = 1;

    public Cell() {
        this.particles=new ArrayList<>();
        this.neighbours=new HashSet<>();
        this.ID = counter++;
    }

    public List<Particle> getParticles() {
        return particles;
    }

    public Set<Cell> getNeighbours() {
        return neighbours;
    }

    public void addParticle (Particle particle){
        this.particles.add(particle);
    }

    public void addNeighbour (Cell cell){
        if(!this.equals(cell))
            this.neighbours.add(cell);
    }

    public int getID() {
        return ID;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof Cell))
            return false;

        Cell other = (Cell) obj;

        return (getID() == other.getID());
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(ID);
    }
}
