
public class Velocity {

    private Double speed;
    private Double angle;

    public Velocity(double speed, double angle) {
        this.speed = speed;
        this.angle = angle;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Double getAngle() {
        return angle;
    }

    public void setAngle(Double angle) {
        this.angle = angle;
    }

    public double getX(){
        return speed* Math.cos(angle);
    }

    public double getY(){
        return speed* Math.sin(angle);
    }
}
