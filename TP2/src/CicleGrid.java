import java.util.List;

public class CicleGrid extends Grid {


    public CicleGrid(Double l, Integer m, List<Particle> particles) {
        super(l, m, particles);
    }

    @Override
    public void calculateNeighbours() {
        int[][] np = {{-1,0},{-1,1},{0,1},{1,1}};

        for(int i=0; i<getM(); i++){
            for(int j=0; j<getM(); j++){
                for(int n = 0; n < np.length;n++){
                    CellIndex cellIndex = calculateCircularPosition(i + np[n][0],j + np[n][1],getM());
                    if(cellIndex != null)
                        getCell(i,j).addNeighbour(getCell(cellIndex.getX(),cellIndex.getY()));
                }
            }
        }
    }

    private boolean isValidIndex(int index, int m){
        return (index >= 0 && index < m);
    }

    private CellIndex calculateCircularPosition(int x,int y, int m){
        boolean validX = isValidIndex(x,m);
        boolean validY = isValidIndex(y,m);

        if(validX == true && validY == true)
            return new CellIndex(x,y);

        //Elimino diagonales (LAS DIAGONALES VAN)
        //if(validX == false && validY == false)
        //    return null;

        return new CellIndex(x%m, y%m);
    }

    private class CellIndex{
        private int x;
        private int y;

        public CellIndex(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }
}
