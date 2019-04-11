package ar.edu.itba.ss;

import java.util.*;

public class BrownianMotion {
    private double L;
    private double time;
    private List<Particle> particles;

    public BrownianMotion(double l, double time, List<Particle> particles) {
        L = l;
        this.time = time;
        this.particles = particles;
    }

    public void simulate() {
        int colNum = 0;
        //List<Particle> notUpdatedParticles = Collections.emptyList();
        List<Collision> collisions;
        for (double t = 0; t < time; ){

            collisions = calculateTimes(particles,t);

            Collision nextCollision = Collections.min(collisions);
            Particle.updatePositions(particles,nextCollision.getTc());

            updateSpeed(nextCollision);

            t += nextCollision.getTc();

            printParticles(colNum++, nextCollision.getTc());
            //notUpdatedParticles = updateCollisionQueue(nextCollision);
        }
    }

    private void printParticles(int colNum, double tc){
        System.out.println(particles.size() + 2);
        System.out.println(colNum);
        for (Particle p : particles){
            System.out.println(p.getPosition().getX() + "\t" + p.getPosition().getY() + "\t" + p.getVelocity().getX() + "\t" + p.getVelocity().getY() + "\t" + p.getRadius() + "\t" + tc);
        }
        // Print two particles for fixed Simulation Box in Ovito animation
        System.out.println(0 + "\t" + 0 + "\t" + 0 + "\t" + 0 + "\t" + 0.001 + "\t" + 0);
        System.out.println(L + "\t" + L + "\t" + 0 + "\t" + 0 + "\t" + 0.001 + "\t" + 0);
    }

    private List<Collision> calculateTimes(List<Particle> particlesList, double currentTime){
        List<Collision> collisions = new ArrayList<>();
        for (Particle particle : particlesList){ /* Find next collision time (tc) */

            Double verticalTc = getVWallTime(particle);
            Double horizontalTc = getHWallTime(particle);

            /* Vertical Collision */
            if(verticalTc != null)
                collisions.add(new Collision(Collision.CollisionType.VERTICAL_WALL, verticalTc,particle));


            /* Horizontal Collision */
            if(horizontalTc != null)
                collisions.add(new Collision(Collision.CollisionType.HORIZONTAL_WALL, horizontalTc,particle));


            /* Particle collision */
            for (Particle otherParticle : particles){
                if (particle.getID() < otherParticle.getID()){

                    double dX = otherParticle.getPosition().getX() - particle.getPosition().getX();
                    double dY = otherParticle.getPosition().getY() - particle.getPosition().getY();
                    double dVx = otherParticle.getVelocity().getX() - particle.getVelocity().getX();
                    double dVy = otherParticle.getVelocity().getY() - particle.getVelocity().getY();

                    double dVdR = dVx*dX + dVy*dY;

                    if (Double.compare(dVdR, 0) >= 0){
                        continue;
                    }

                    double dVdV = Math.pow(dVx, 2) + Math.pow(dVy, 2);

                    double dRdR = Math.pow(dX, 2) + Math.pow(dY, 2);

                    double sigma = particle.getRadius() + otherParticle.getRadius();

                    double d = Math.pow(dVdR, 2) - dVdV * (dRdR - Math.pow(sigma ,2));

                    if (Double.compare(d, 0) < 0){
                        continue;
                    }

                    double ptc = (-1) * ((dVdR + Math.sqrt(d)) / dVdV);
                    collisions.add(new CollisionParticle(ptc,particle,otherParticle));
                }
            }
        }
        return collisions;
    }

    /**
     * Returns the time when a particle hits an vertical wall.
     * @param particle particle to get the time.
     * @return time of collision.
     */
    private Double getVWallTime(Particle particle) {

        if (particle.getVelocity().getX() > 0) {
            return (L - particle.getRadius() - particle.getPosition().getX()) / particle.getVelocity().getX();
        } else if (particle.getVelocity().getX() < 0) {
            return (0 + particle.getRadius() - particle.getPosition().getX()) / particle.getVelocity().getX();
        }

        return null;
    }

    /**
     * Returns the time when a particle hits an horizontal wall.
     * @param particle particle to get the time.
     * @return time of collision.
     */
    private Double getHWallTime(Particle particle) {
        if (particle.getVelocity().getY() > 0){
            return  (L - particle.getRadius() - particle.getPosition().getY()) / particle.getVelocity().getY();
        }else if (particle.getVelocity().getY() < 0){
            return  (0 + particle.getRadius() - particle.getPosition().getY())  / particle.getVelocity().getY();
        }

        return null;
    }

    /**
     * Updates the speed of the particle/s that collided.
     * @param collision particle colliding.
     */
    private void updateSpeed(Collision collision){
        if(collision == null)
            return;

        if(collision instanceof CollisionParticle) {
            updateParticleCollisionSpeed((CollisionParticle) collision);
        }else{
            updateWallCollisionSpeed(collision);
        }
    }

    /**
     * Update particle wall collision speed.
     * @param collision particle colliding wall.
     */
    private void updateWallCollisionSpeed(Collision collision) {
        if(Collision.CollisionType.VERTICAL_WALL == collision.getCollisionType()) {
            collision.getParticle().getVelocity().setNegativeX();
        }else if(Collision.CollisionType.HORIZONTAL_WALL == collision.getCollisionType()){
            collision.getParticle().getVelocity().setNegativeY();
        }
    }

    /**
     * Update particles collision speed.
     * @param collision particle colliding another particle.
     */
    private void updateParticleCollisionSpeed(CollisionParticle collision){
        Particle pi = collision.getParticle();
        Particle pj = collision.getCollidingParticle();

        double dX = pj.getPosition().getX() - pi.getPosition().getX();
        double dY = pj.getPosition().getY() - pi.getPosition().getY();
        double dVx = pj.getVelocity().getX() - pi.getVelocity().getX();
        double dVy = pj.getVelocity().getY() - pi.getVelocity().getY();

        double dVdR = dVx * dX + dVy * dY;
        double sigma = pi.getRadius() + pj.getRadius();

        double J = (2 * pi.getMass() * pj.getMass() * dVdR) / (sigma * (pi.getMass() + pj.getMass()));
        double Jx = J * dX / sigma;
        double Jy = J * dY / sigma;

        pi.getVelocity().setX(pi.getVelocity().getX() + Jx / pi.getMass());
        pi.getVelocity().setY(pi.getVelocity().getY() + Jy / pi.getMass());

        pj.getVelocity().setX(pj.getVelocity().getX() - Jx / pj.getMass());
        pj.getVelocity().setY(pj.getVelocity().getY() - Jy / pj.getMass());
    }

    public double getL() {
        return L;
    }

    public double getTime() {
        return time;
    }

    public List<Particle> getParticles() {
        return Collections.unmodifiableList(particles);
    }
      /*public List<Particle> updateCollisionQueue(Collision currentCollision){
        Iterator i = collisions.iterator();
        Collision c;
        List<Particle> notUpdatedParticles = new ArrayList<>();
        Particle p1 = currentCollision.getParticle();
        Particle p2 = null;
        double tc = currentCollision.getTc();
        notUpdatedParticles.add(p1);

        if(currentCollision instanceof CollisionParticle){
            p2 = ((CollisionParticle) currentCollision).getCollidingParticle();
            notUpdatedParticles.add(p2);
        }

        while (i.hasNext()) {
            c = (Collision) i.next();
            double prev_tc = c.getTc();
            c.setTc(prev_tc - tc);
            if(!(c.containsParticle(p1) || c.containsParticle(p2)))
                i.remove();
        }
        return notUpdatedParticles;
    }*/
}
