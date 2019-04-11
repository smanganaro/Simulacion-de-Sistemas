package ar.edu.itba.ss;

public class Collision implements Comparable<Collision>{
    public enum CollisionType {HORIZONTAL_WALL, VERTICAL_WALL, PARTICLE};
    private CollisionType collisionType;
    private double tc;
    private Particle particle;

    public Collision(CollisionType collisionType, double tc, Particle p1) {
        if(collisionType == null || p1 == null){
            throw new IllegalArgumentException();
        }

        this.collisionType = collisionType;
        this.tc = tc;
        this.particle = p1;
    }

    public CollisionType getCollisionType() {
        return collisionType;
    }

    public double getTc() {
        return tc;
    }

    public Particle getParticle() {
        return particle;
    }

    public boolean containsParticle(Particle p2){
        return getParticle().equals(p2);
    }

    public void setTc(double tc) {
        this.tc = tc;
    }

    @Override
    public int compareTo(Collision o) {
        return Double.compare(getTc(),o.getTc());
    }
}
