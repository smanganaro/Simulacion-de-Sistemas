package ar.edu.itba.ss;
import java.util.List;

public abstract class Grid {

    private Double L;
    private Integer M;
    private Cell[][] cells;


    public Grid(Double l, Integer m, List<Particle> particles) {
        this.L = l;
        this.M = m;
        this.cells = new Cell[M][M];
        for(int i=0; i < M; i++){
            for(int j=0; j < M; j++){
                cells[i][j] = new Cell();
            }
        }
        insertParticles(particles);
    }

    public abstract void calculateNeighbours();

    public Double getL() {
        return L;
    }

    public Integer getM() {
        return M;
    }

    public Cell[][] getCells() {
        return cells;
    }

    public Cell getCell(int x, int y){
        return cells[x][y];
    }

    public void insertParticles(List<Particle> particles){
        for(Particle p: particles){
            insertParticle(p);
        }
    }
    public void insertParticle(Particle p){
        int x = (int) (Math.floor(p.getPosition().getX()/(L/M)));
        int y = (int) (Math.floor(p.getPosition().getY()/(L/M)));
        cells[x][y].getParticles().add(p);
    }
}
