package ar.edu.itba.ss;

public class Velocity {
    private Double x;
    private Double y;

    public Velocity(Double x, Double y) {
        this.x = x;
        this.y = y;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public void setNegativeX(){
        this.x = x * -1;
    }

    public Double getY() {
        return y;
    }

    public void setNegativeY(){
        this.y = y * -1;
    }

    public void setY(Double y) {
        this.y = y;
    }
}
