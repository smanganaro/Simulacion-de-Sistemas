package ar.edu.itba.ss.core.neigbour;

import ar.edu.itba.ss.core.Particle;

import java.util.List;

//Best M (int) Math.floor(l / (rc + 2 * maxRadius));
public abstract class Grid {

    private Double L;
    private Integer M;
    private Cell[][] cells;
    private List<Particle> particles;

    public Grid(Double l, Integer m, List<Particle> particles) {
        this.L = l;
        this.M = m;
        this.particles = particles;
        this.cells = new Cell[M][M];
        for(int i=0; i < M; i++){
            for(int j=0; j < M; j++){
                cells[i][j] = new Cell();
            }
        }
        insertParticles(particles);
        calculateNeighbours();
    }

    public List<Particle> getParticles() {
        return particles;
    }

    public Grid(double l, double rc, List<Particle> particles) {
        this.L = l;
        this.M = (int) Math.floor(l / (rc + 2 * getMaxRadius(particles)));
        this.particles = particles;
        this.cells = new Cell[M][M];
        for(int i=0; i < M; i++){
            for(int j=0; j < M; j++){
                cells[i][j] = new Cell();
            }
        }
        insertParticles(particles);
        calculateNeighbours();
    }

    public Grid(double l,double rc, double maxRadius, List<Particle> particles) {
        this.L = l;
        this.M = (int) Math.floor(l / (rc + 2 * maxRadius));
        this.particles = particles;
        this.cells = new Cell[M][M];
        for(int i=0; i < M; i++){
            for(int j=0; j < M; j++){
                cells[i][j] = new Cell();
            }
        }
        insertParticles(particles);
        calculateNeighbours();
    }

    private double getMaxRadius(List<Particle> particles){
        double max = 0;
        for(Particle p : particles){
            if(p.getRadius() > max)
                max = p.getRadius();
        }
        return max;
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

    private void insertParticles(List<Particle> particles){
        for(Particle p: particles){
            insertParticle(p);
        }
    }

    private void insertParticle(Particle p){
        int x = (int) (Math.floor(p.getPosition().getX()/(L/M)));
        int y = (int) (Math.floor(p.getPosition().getY()/(L/M)));
        cells[x][y].addParticle(p);
    }
}
