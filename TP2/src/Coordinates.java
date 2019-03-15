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
