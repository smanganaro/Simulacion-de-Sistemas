package ar.edu.itba.ss.core;

public class Coordinates {

    private Double x;
    private Double y;

    public Coordinates(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public Double getDistance(Coordinates c){
        return Math.sqrt(Math.pow(this.getX()-c.getX(), 2) + Math.pow(this.getY()-c.getY(), 2));
    }

    public Coordinates subtract(double x, double y){
        double x1 = this.getX()-x;
        double y1 = this.getY()-y;
        return new Coordinates(x1,y1);
    }

    public Coordinates subtract(Coordinates c){
        return subtract(c.getX(),c.getY());
    }

    public Coordinates multiply(double factor) {
        return new Coordinates(getX() * factor, getY() * factor);
    }

    public Coordinates add(double x, double y){
        double x1 = this.getX()+x;
        double y1 = this.getY()+y;
        return new Coordinates(x1,y1);
    }

    public Coordinates add(Coordinates c){
        return add(c.getX(),c.getY());
    }

    public double magnitude(){
        final double x = getX();
        final double y = getY();

        return Math.sqrt(x * x + y * y);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof Coordinates))
            return false;

        Coordinates other = (Coordinates) obj;

        return (getX().equals(other.getX()) && getY().equals(other.getY()));
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash = hash * 23 + x.hashCode();
        hash = hash * 23 + y.hashCode();
        return hash;
    }
}
