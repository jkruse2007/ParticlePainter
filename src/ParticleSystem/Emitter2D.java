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
public class Emitter2D {

    // emitter attributes
    private PApplet parent;
    private ArrayList<Particle2D> particles;
    public PVector emitterPosition;
    public boolean showEmitter;

    private int currentFrame;
    private float emissionRate; // particles per second

    // particle attributes
    private PVector initialParticlePosition;
    private PVector initialParticleVelocity;
    private PVector initialParticleAcceleration;
    private PVector initialParticleSize;
    private collisionMode boundaryCollisionMode;
    private float particleLifetime; // in seconds - particle will live forever if this is null



    Emitter2D(PApplet p){
        parent = p;
        showEmitter = false;
        emitterPosition = new PVector(parent.width/2,parent.height/2);
        particles = new ArrayList<Particle2D>();
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
            particle.position = getInitialParticlePosition();
            particle.velocity = getInitialParticleVelocity();
            particle.acceleration = getInitialParticleAcceleration();

            //...and add it to the array
            particles.add(particle);
        }
    }



    public void drawEmitterShape(){
        parent.stroke(127);
        parent.noFill();
        parent.ellipse(emitterPosition.x, emitterPosition.y, 25, 25);
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
        if (initialParticlePosition == null) return emitterPosition.get();
        return initialParticlePosition;
    }


    public void setInitialParticlePosition(PVector initialParticlePosition) {
            this.initialParticlePosition = initialParticlePosition;
    }

    /**
     * Returns a <b>copy</b> of the initial particle velocity vector, or
     * a new PVector(0,0) if initialParticleVelocity == null
     * @return  velocity vector
     */
    private PVector getInitialParticleVelocity() {
        if (initialParticleVelocity == null) return new PVector(0f,0f);
        return initialParticleVelocity.get();
    }

    public void setInitialParticleVelocity(PVector initialParticleVelocity) {
        this.initialParticleVelocity = initialParticleVelocity;
    }

    //@todo
    /**
     * Stub implementation
     *  @return a random {@link processing.core.PVector} of magnitude 0.1
     */
    private PVector getInitialParticleAcceleration() {
        if (initialParticleAcceleration == null){
            // add some random acceleration for now...
            PVector acc = PVector.random2D();
            acc.normalize();
            acc.mult(0.1f);
            return acc;
        }
        return initialParticleAcceleration;

    }

    public void setInitialParticleAcceleration(PVector initialParticleAcceleration) {
        this.initialParticleAcceleration = initialParticleAcceleration;
    }

    private float getParticleLifetime() {
        return particleLifetime;
    }

    /** Set the emitted particles' lifetime in seconds */
    public void setParticleLifetime(float particleLifetime) {
        this.particleLifetime = particleLifetime;
    }

    /**
     * Return the number of emitted particles per second
     * or the parent's frame rate if emissionRate is undefined
     * @return {@link ParticleSystem.Emitter2D#emissionRate}
     * @see processing.core.PApplet#frameRate
     */
    public float getEmissionRate() {
        if(emissionRate >= 0) return emissionRate;
        else return parent.frameRate;
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
     * @param boundaryCollisionMode  {@link ParticleSystem.collisionMode}
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
}
