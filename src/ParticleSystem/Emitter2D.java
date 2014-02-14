package ParticleSystem;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.ListIterator;


/**
 * Some predefined collision modes
 * <pre>
 *     NONE:    don't do anything - keep going
 *     WRAP:    reappear on the opposite side of the frame (for boundary collisions)
 *     BOUNCE:  bounce off the collision object
 *     DIE:     remove yourself from the particle system
 * </pre>
 */
enum collisionMode {NONE,WRAP,BOUNCE,STICKY,DIE}


/**
 * Created by Frank on 04.02.14.
 */

/**
 * A Particle emitter class
 */
class Emitter2D {

    // emitter attributes
    private static PApplet parent;
    private ArrayList<Particle2D> particles;
    private int currentFrame;
    private float emissionRate; // particles per second
    private PVector emissionDirection;
    private float emissionSpeed;
    private float emissionAngle;
    private float emissionSpread;
    private PVector emitterPosition;

    public boolean showEmitter;


    // per particle attributes
    private PVector initialParticlePosition;
    private PVector initialParticleVelocity;
    private PVector initialParticleAcceleration;
    private PVector initialParticleSize;
    private collisionMode boundaryCollisionMode;
    private float particleLifetime; // in seconds - particle will live forever if this is null


    Emitter2D(PApplet p){
        parent = p;
        particles = new ArrayList<Particle2D>();
        showEmitter = false;
        emitterPosition = new PVector(parent.width/2,parent.height/2);
    }


    // maintain an array of particles for each frame
    private void populateArray(){
        int numParticles = 0;
        int fr = (int)parent.frameRate;
        float particlesPerFrame = getEmissionRate() / fr;

        // handle emission rates < 1
        if (getEmissionRate() == 0) numParticles = 0;
        else if (particlesPerFrame < 1){
            int n = PApplet.round(1 / particlesPerFrame);
            if (currentFrame % n == 0) numParticles = 1;
        }
        else numParticles = PApplet.round(particlesPerFrame);


        for (int i=0; i<numParticles; i++){
            // initialize a new particle...
            Particle2D particle = new Particle2D(parent);

            particle.setLifetime(getParticleLifetime());
            particle.boundaryCollisionMode = getBoundaryCollisionMode();
            particle.size = getInitialParticleSize();

            particle.position = getInitialParticlePosition().get();
            particle.velocity = getInitialParticleVelocity().get();
            particle.acceleration = getInitialParticleAcceleration().get();

            //...and add it to the array
            particles.add(particle);
        }
    }



    void drawEmitterShape(){
        parent.stroke(127);
        parent.noFill();
        parent.ellipse(getEmitterPosition().x, getEmitterPosition().y, 25, 25);
    }

    /** Update the particle system's state */
    public void update(){
        populateArray();
        for (Particle2D particle: this.particles) particle.update();
        removeDeadParticles();
        currentFrame++;
    }

    /** Display the emitter */
    public void draw(){
        if (showEmitter) drawEmitterShape();
        for (Particle2D particle : particles) particle.draw();
    }

    private void removeDeadParticles() {
        for(ListIterator<Particle2D> it = particles.listIterator(); it.hasNext();) {
            if (!it.next().isAlive) it.remove();
        }
    }

    private PVector getInitialParticlePosition() {
        if (initialParticlePosition == null) return getEmitterPosition();
        return initialParticlePosition;
    }


    private void setInitialParticlePosition(PVector initialParticlePosition) {
            this.initialParticlePosition = initialParticlePosition;
    }

    private PVector getInitialParticleVelocity() {
            PVector vel;
            vel = getEmissionDirection();
            vel.setMag(getEmissionSpeed());
            initialParticleVelocity = vel;
            return initialParticleVelocity;
    }


    private PVector getInitialParticleAcceleration() {
        if (initialParticleAcceleration == null){
            return new PVector(0f,0f);
        }
        return initialParticleAcceleration;

    }

    private void setInitialParticleAcceleration(PVector initialParticleAcceleration) {
        this.initialParticleAcceleration = initialParticleAcceleration;
    }

    private float getParticleLifetime() {
        return particleLifetime;
    }

    /** Set the emitted particles' lifetime in seconds */
    public void setParticleLifetime(float particleLifetime) {
        this.particleLifetime = particleLifetime;
    }


    private float getEmissionRate() {
        if(emissionRate > 0) return emissionRate;
        else return 0f;
    }

    /**
     * Set the number of emitted particles per second.<br></br>
     * Negative values will result in an emission rate of 0.
     * */
    public void setEmissionRate(float emissionRate) {
        if (emissionRate < 0) this.emissionRate = 0;
        else this.emissionRate = emissionRate;
    }


    private collisionMode getBoundaryCollisionMode() {
        if (boundaryCollisionMode != null) return boundaryCollisionMode;
        else return collisionMode.DIE;
    }

    /**
     * Set the mode for collisions with the edge of the frame.
     * @param boundaryCollisionMode  the {@link ParticleSystem.collisionMode}
     *                               to be applied
     */
    public void setBoundaryCollisionMode(collisionMode boundaryCollisionMode) {
        this.boundaryCollisionMode = boundaryCollisionMode;
    }

    /**
     * Set an initial size for the emitted particles
     * @param x width
     * @param y height
     */
    public void setInitialParticleSize(float x, float y) {
        this.initialParticleSize = new PVector(x, y);
    }

    private PVector getInitialParticleSize() {
        if (initialParticleSize == null) initialParticleSize = new PVector(1,1);
        return initialParticleSize;
    }

    /**
     * Returns the emitter's current position vector.
     * @return  position vector
     */
    public PVector getEmitterPosition() {
        return emitterPosition;
    }

    /**
     * Set the emitter's position
     * @param x horizontal position in pixels
     * @param y vertical position in pixels
     */
    public void setEmitterPosition(int x, int y) {
        this.emitterPosition.x = x;
        this.emitterPosition.y = y;
    }

    /**
     * Returns the emission direction
     * @return direction vector
     */
    private PVector getEmissionDirection() {
        if (emissionDirection == null){
            emissionDirection = new PVector(1,0);
        }

        // modify the direction based on the spread angle
        if(emissionSpread != 0){
            PVector modDirection = emissionDirection.get();
            double theta = Math.atan2(modDirection.y, modDirection.x);
            float rnd = parent.random(-emissionSpread, emissionSpread);
            double rnd_theta = Math.toRadians((double) rnd);
            modDirection = new PVector((float) Math.cos(theta+rnd_theta),(float) Math.sin(theta+rnd_theta));
            modDirection.normalize();
            return modDirection;
        }

        return emissionDirection;
    }

    /**
     * Specify a direction in which the particles will be emitted
     * @param emissionDirection direction vector
     */
    public void setEmissionDirection(PVector emissionDirection){
        this.emissionDirection = emissionDirection;
        this.emissionDirection.normalize();
    }

    /**
     * Set a speed for particle emission.
     * @param emissionSpeed speed at which the particles will be emitted
     */
    public void setEmissionSpeed(float emissionSpeed) {
        this.emissionSpeed = emissionSpeed;
    }

    private float getEmissionSpeed() {
        return emissionSpeed;
    }

    /**
     * Controls how much variation will be applied to the emission direction
     * @param emissionSpread maximum deviation from the emission direction in degrees
     */
    public void setEmissionSpread(float emissionSpread) {
        if (emissionSpread < 0) this.emissionSpread = 0;
        else if (emissionSpread > 180) this.emissionSpread = 180;
        else this.emissionSpread = emissionSpread;
    }

}
