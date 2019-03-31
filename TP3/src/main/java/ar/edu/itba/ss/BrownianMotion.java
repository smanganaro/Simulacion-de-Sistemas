package ar.edu.itba.ss;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BrownianMotion{
    private double L;
    private double time;
    private List<Particle> particles;

    public BrownianMotion(double l, double time,List<Particle> particles) {
        L = l;
        this.time = time;
        this.particles = particles;
    }

    public void simulate() {
        int colNum = 0;
        for (double t = 0; t < time; ){
            double tc = Double.POSITIVE_INFINITY;
            Particle pi = null;
            Particle pj = null;
            for (Particle particle : particles){ /* Find next collision time (tc) */

                double verticalTc = getVWallTime(particle);
                double horizontalTc = getHWallTime(particle);

                if (verticalTc < tc){
                    tc = verticalTc;
                    particle.setCollision(Collision.VERTICAL_WALL);
                    pi = particle;
                    pj = null;
                }

                if (horizontalTc < tc) {
                    tc = horizontalTc;
                    particle.setCollision(Collision.HORIZONTAL_WALL);
                    pi = particle;
                    pj = null;
                }

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

                        if (ptc < tc){
                            tc = ptc;
                            particle.setCollision(Collision.PARTICLE);
                            otherParticle.setCollision(Collision.PARTICLE);
                            pi = particle;
                            pj = otherParticle;
                        }
                    }
                }
            }

            Particle.updatePositions(particles, tc);

            updateSpeed(pi, pj);

            t += tc;

            System.out.println(particles.size() + 2);
            System.out.println(colNum++);
            for (Particle p : particles){
                System.out.println(p.getPosition().getX() + "\t" + p.getPosition().getY() + "\t" + p.getVelocity().getX() + "\t" + p.getVelocity().getY() + "\t" + p.getRadius() + "\t" + tc);
            }
            // Print two particles for fixed Simulation Box in Ovito animation
            System.out.println(0 + "\t" + 0 + "\t" + 0 + "\t" + 0 + "\t" + 0.001 + "\t" + 0);
            System.out.println(L + "\t" + L + "\t" + 0 + "\t" + 0 + "\t" + 0.001 + "\t" + 0);
        }
    }

    /**
     * Returns the time when a particle hits an vertical wall.
     * @param particle particle to get the time.
     * @return time of collision.
     */
    private double getVWallTime(Particle particle) {

        if (particle.getVelocity().getX() > 0) {
            return (L - particle.getRadius() - particle.getPosition().getX()) / particle.getVelocity().getX();
        } else if (particle.getVelocity().getX() < 0) {
            return (0 + particle.getRadius() - particle.getPosition().getX()) / particle.getVelocity().getX();
        }

        return Double.POSITIVE_INFINITY;
    }

    /**
     * Returns the time when a particle hits an horizontal wall.
     * @param particle particle to get the time.
     * @return time of collision.
     */
    private double getHWallTime(Particle particle) {
        if (particle.getVelocity().getY() > 0){
            return  (L - particle.getRadius() - particle.getPosition().getY()) / particle.getVelocity().getY();
        }else if (particle.getVelocity().getY() < 0){
            return  (0 + particle.getRadius() - particle.getPosition().getY())  / particle.getVelocity().getY();
        }

        return Double.POSITIVE_INFINITY;
    }

    /**
     * Updates the speed of the particle/s that collided.
     * @param pi first particle
     * @param pj second particle
     */
    private void updateSpeed(Particle pi, Particle pj){
        if(pj == null && pi == null)
            return;

        if(pj == null)
            updateWallCollisionSpeed(pi);

        updateParticleCollisionSpeed(pi,pj);
    }

    /**
     * Update particle wall collision speed.
     * @param pi particle colliding wall.
     */
    private void updateWallCollisionSpeed(Particle pi) {
        if(pi.getCollision() == Collision.VERTICAL_WALL) {
            pi.getVelocity().setNegativeX();
        }else if(pi.getCollision() == Collision.HORIZONTAL_WALL){
            pi.getVelocity().setNegativeY();
        }
    }

    /**
     * Update particles collision speed.
     * @param pi first particle.
     * @param pj second particle.
     */
    private void updateParticleCollisionSpeed(Particle pi, Particle pj){
        if(pi.getCollision() == Collision.PARTICLE) {
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
}
