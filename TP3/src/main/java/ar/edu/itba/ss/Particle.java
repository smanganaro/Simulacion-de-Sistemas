package ar.edu.itba.ss;

import java.util.List;

public class Particle {

    private static int counter = 1;
    private int ID;
    private Coordinates position;
    private Double radius;
    private Double mass;
    private Velocity velocity;

    public Particle(Coordinates position, double radius,double mass, Velocity velocity, List<Particle> particles) {
        if(!validCoordinates(position.getX(),position.getY(),radius,particles))
            throw new RuntimeException();

        this.ID=counter++;
        this.position = position;
        this.radius = radius;
        this.velocity = velocity;
        this.mass = mass;

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

    public Double getMass() {
        return mass;
    }

    public Velocity getVelocity() {
        return velocity;
    }

    public Double getDistance(Particle particle){
        return Math.sqrt(Math.pow(this.getPosition().getX()-particle.getPosition().getX(), 2) +
                Math.pow(this.getPosition().getY()-particle.getPosition().getY(), 2));
    }

    public void updatePos(double time){
        double x = position.getX()+velocity.getX()*time;
        double y = position.getY()+velocity.getY()*time;
        this.setPosition(x,y);
    }

    public void setPosition(double x, double y){
        this.position.setX(x);
        this.position.setY(y);
    }

    /**
     * Checks if there is already a particle on that coordinates.
     * @param x particle position x to check.
     * @param y particle position y to check.
     * @param radius particle radius to check.
     * @param particles list of particles in the cell.
     * @return true if there is already a particle on the given coordinates, false otherwise.
     */
    public static boolean validCoordinates(double x, double y, double radius, List<Particle> particles) {

        for (Particle p: particles){
            boolean valid = Math.pow(p.getPosition().getX() - x, 2)
                    + Math.pow(p.getPosition().getY() - y, 2)
                    > Math.pow(p.getRadius() + radius, 2);
            if (!valid){
                return false;
            }
        }

        return true;
    }

    /**
     * Update system particles positions.
     * @param particles list of particles.
     * @param tc time of the collisionType.
     */
    public static void updatePositions(List<Particle> particles, double tc) {
        for (Particle particle : particles){
            particle.updatePos(tc);
        }

    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof Particle))
            return false;

        Particle other = (Particle) obj;

        return (getID() == other.getID());
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(getID());
    }
}
