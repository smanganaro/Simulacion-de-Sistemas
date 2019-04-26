package ar.edu.itba.ss.core;

import java.util.List;

public class Particle {

  private static int counter = 1;
  private int ID;
  private Coordinates position;
  private Double radius;
  private Double mass;
  private Coordinates velocity;

  public Particle(Coordinates position, double radius,double mass, Coordinates velocity, List<Particle> particles) {
    if(!validCoordinates(position.getX(),position.getY(),radius,particles))
      throw new RuntimeException();

    this.ID = counter++;
    this.position = position;
    this.radius = radius;
    this.velocity = velocity;
    this.mass = mass;

  }

  public Particle(Coordinates position, double radius,double mass, List<Particle> particles){
    this(position,radius,mass,new Coordinates(0.0,0.0),particles);
  }

  public Particle(int ID, Coordinates position, double radius,double mass, Coordinates velocity, List<Particle> particles){
    if(!validCoordinates(position.getX(),position.getY(),radius,particles))
      throw new RuntimeException();

    this.ID = ID;
    this.position = position;
    this.radius = radius;
    this.velocity = velocity;
    this.mass = mass;
  }

  public Particle(int ID, Coordinates position, double radius,double mass, List<Particle> particles){
    this(ID, position,radius,mass,new Coordinates(0.0,0.0),particles);
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

  public Coordinates getVelocity() {
    return velocity;
  }

  public Double getDistance(Particle particle){
    return Math.sqrt(Math.pow(this.getPosition().getX()-particle.getPosition().getX(), 2) +
            Math.pow(this.getPosition().getY()-particle.getPosition().getY(), 2));
  }

  /**
   * Checks if there is already a particle on that Coordinates.
   * @param x particle position x to check.
   * @param y particle position y to check.
   * @param radius particle radius to check.
   * @param particles list of particles in the cell.
   * @return true if there is already a particle on the given Coordinates, false otherwise.
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

  @Override
  public boolean equals(Object obj) {
    if (obj == this)
      return true;
    if (!(obj instanceof Particle))
      return false;

    Particle other = (Particle) obj;

    return (getID() == other.getID()) || (getPosition().equals(other.getPosition())
            && getMass() == other.getMass() && getRadius().equals(other.getRadius()));
  }

  @Override
  public int hashCode() {
    return Integer.hashCode(getID());
  }
}

