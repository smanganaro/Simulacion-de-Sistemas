package ar.edu.itba.ss.core.neigbour;

import ar.edu.itba.ss.core.Particle;

import java.util.List;

public class LinearGrid extends Grid {

    public LinearGrid(Double l, Integer m, List<Particle> particles) {
        super(l, m, particles);
    }

    public LinearGrid(double l,double rc, List<Particle> particles) {
        super(l,rc,particles);
    }

    public LinearGrid(double l,double rc,double maxRadius, List<Particle> particles) {
        super(l,rc,maxRadius,particles);
    }

    @Override
    public void calculateNeighbours() {
        int[][] np = {{-1,0},{-1,1},{0,1},{1,1}};

        for(int i=0; i<getM(); i++){

            for(int j=0; j<getM(); j++){

                for(int n = 0; n < np.length;n++){
                    int x = i + np[n][0];
                    int y = j + np[n][1];

                    if(isValidPosition(x,y,getM())){
                        getCell(i,j).addNeighbour(getCell(x,y));
                    }
                }
            }
        }
    }
    private boolean isValidIndex(int index, int m){
        return (index >= 0 && index < m);
    }

    private boolean isValidPosition(int x, int y, int m){
        return (isValidIndex(x,m) && isValidIndex(y,m));
    }
}