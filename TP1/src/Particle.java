public class Particle {

    private static int counter = 1;
    private Coordinates position;
    private Double radius;
    private Double color;
    private Movement movement;
    private int ID;


    public Particle(Coordinates position, double radius,double color, Coordinates velocity) {
        this.position = position;
        this.radius = radius;
        this.movement = new Movement(velocity);
        this.ID=counter++;
        this.color = color;
    }

    public Particle(Coordinates position, double radius, double color) {
        this(position,radius,color, new Coordinates(0,0));
    }

    public Coordinates getPosition() {
        return position;
    }

    public Double getRadius() {
        return radius;
    }

    public Movement getMovement(){
        return movement;
    }

    public int getID(){
        return this.ID;
    }

    public Double getColor() {
        return color;
    }

    public void addMovement(Double time){
        movement.Move(time, position.getX(), position.getY());
    }

    public void setPosition(Coordinates position) {
        this.position = position;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
    }

    public String toString(){
        return "Particle ID: " + ID;
    }
    

    public Double getDistance(Particle particle){
       return Math.sqrt(Math.pow(this.getPosition().getX()-particle.getPosition().getX(), 2) +
               Math.pow(this.getPosition().getY()-particle.getPosition().getY(), 2));
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
