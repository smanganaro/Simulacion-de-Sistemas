public class Particle {

    private static int counter = 1;
    private Coordinates position;
    private Double radius;
    private Double color;
    private Velocity velocity;
    private int ID;


    public Particle(Coordinates position, double radius,double color, Velocity velocity) {
        this.position = position;
        this.radius = radius;
        this.velocity = velocity;
        this.ID=counter++;
        this.color = color;
    }

    public Particle(Coordinates position, double radius, double color) {
        this(position,radius,color, new Velocity(0,0));
    }

    public Coordinates getPosition() {
        return position;
    }

    public Double getRadius() {
        return radius;
    }

    public int getID(){
        return this.ID;
    }

    public Double getColor() {
        return color;
    }

    public Velocity getVelocity() {
        return velocity;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
    }

    public Double getDistance(Particle particle){
       return Math.sqrt(Math.pow(this.getPosition().getX()-particle.getPosition().getX(), 2) +
               Math.pow(this.getPosition().getY()-particle.getPosition().getY(), 2));
   }

    public void updatePos(double time){
        double x = position.getX()+velocity.getX()*time;
        double y = position.getY()+velocity.getY()*time;
        this.position.setX(x);
        this.position.setY(y);
    }

    public void setPosition(double x, double y){
        this.position.setX(x);
        this.position.setY(y);
    }

    public void setAngle(double ang){
        this.velocity.setAngle(ang);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof Particle))
            return false;

        Particle other = (Particle) obj;

        return (getPosition().equals(other.getPosition()) && getRadius().equals(other.getRadius()));
    }
    @Override
    public int hashCode() {
        int hash = 17;
        hash = hash * 23 + position.hashCode();
        hash = hash * 23 + radius.hashCode();
        //hash = hash * 23 + color.hashCode();
        return hash;
    }
}
