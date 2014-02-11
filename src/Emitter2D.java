import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.ListIterator;


/**
 * Created by Frank on 04.02.14.
 */


public class Emitter2D {

    // emitter attributes
    private PApplet parent;
    private ArrayList<Particle2D> particles;
    public PVector emitterPosition;
    public boolean showEmitter;

    private int currentFrame;
    private Number emissionRate; // particles per second

    // particle attributes
    private PVector initialParticlePosition;
    private PVector initialParticleVelocity;
    private PVector initialParticleAcceleration;
    private PVector initialParticleSize;
    private collisionMode boundaryCollisionMode;
    private Number particleLifetime; // in seconds - particle will live forever if this is null



    Emitter2D(PApplet p){
        parent = p;
        showEmitter = true;
        emitterPosition = new PVector(parent.width/2,parent.height/2);
        particles = new ArrayList<Particle2D>();
    }



    public void populateArray(){
        int numParticles = 0;
        int fr = (int)parent.frameRate;
        float particlesPerFrame = getEmissionRate().floatValue() / fr;

        if (particlesPerFrame < 1){
            int n = PApplet.round(1 / particlesPerFrame);
            if (currentFrame % n == 0) numParticles = 1;
        }
        else numParticles = PApplet.round(particlesPerFrame);


        for (int i=0; i<numParticles; i++){
            // initialize a new particle...
            Particle2D particle = new Particle2D(parent);

            particle.lifetime = getParticleLifetime();
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
        parent.ellipse(this.emitterPosition.x, this.emitterPosition.y, 25, 25);
    }

    public void update(){
        populateArray();
        for (Particle2D particle: this.particles) particle.update();
        removeDeadParticles();
        currentFrame++;
    }

    public void removeDeadParticles() {
        for(ListIterator<Particle2D> it = particles.listIterator(); it.hasNext();) {
            if (!it.next().isAlive) it.remove();
        }
    }

    public void draw(){
        if (this.showEmitter) this.drawEmitterShape();
        for (Particle2D particle : particles) particle.draw();
    }

    public PVector getInitialParticlePosition() {
        if (initialParticlePosition == null) return emitterPosition.get();
        return initialParticlePosition;
    }

    public void setInitialParticlePosition(PVector initialParticlePosition) {
            this.initialParticlePosition = initialParticlePosition;
    }

    public PVector getInitialParticleVelocity() {
        if (initialParticleVelocity == null) return new PVector(0f,0f,0f);
        return initialParticleVelocity.get();
    }

    public void setInitialParticleVelocity(PVector initialParticleVelocity) {
        this.initialParticleVelocity = initialParticleVelocity;
    }

    public PVector getInitialParticleAcceleration() {
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

    public Number getParticleLifetime() {
        return particleLifetime;
    }

    public void setParticleLifetime(Number particleLifetime) {
        this.particleLifetime = particleLifetime.floatValue();
    }

    public Number getEmissionRate() {
        if(emissionRate == null) return parent.frameRate;
        return emissionRate;
    }

    public void setEmissionRate(Number emissionRate) {
        this.emissionRate = emissionRate;
    }

    public collisionMode getBoundaryCollisionMode() {
        if (boundaryCollisionMode != null) return boundaryCollisionMode;
        else return collisionMode.DIE;
    }

    public void setBoundaryCollisionMode(collisionMode boundaryCollisionMode) {
        this.boundaryCollisionMode = boundaryCollisionMode;
    }

    public void setInitialParticleSize(float x, float y) {
        this.initialParticleSize = new PVector(x, y);
    }

    private PVector getInitialParticleSize() {
        if (initialParticleSize == null) initialParticleSize = new PVector(1,1);
        return initialParticleSize;
    }
}
