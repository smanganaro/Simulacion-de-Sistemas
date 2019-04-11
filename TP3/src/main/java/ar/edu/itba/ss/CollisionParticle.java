package ar.edu.itba.ss;

public class CollisionParticle extends Collision {
    private Particle collidingParticle;

    public CollisionParticle(double tc, Particle p1, Particle p2) {
        super(CollisionType.PARTICLE,tc,p1);
        if(p2 == null){
            throw new IllegalArgumentException();
        }
        this.collidingParticle = p2;
    }

    public Particle getCollidingParticle() {
        return collidingParticle;
    }

    @Override
    public boolean containsParticle(Particle p2) {
        return super.containsParticle(p2) || getCollidingParticle().equals(p2);
    }
}
