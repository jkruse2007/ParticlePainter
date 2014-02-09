import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.ListIterator;


/**
 * Created by Frank on 04.02.14.
 */


public class Emitter2D {

    private PApplet parent;
    private ArrayList<Particle2D> particles = new ArrayList<Particle2D>();

    public PVector emitterPosition;
    public boolean showEmitter;

    // initial per particle attributes
    private PVector initialParticlePosition;
    private PVector initialParticleVelocity;
    private PVector initialParticleAcceleration;
    private collisionMode boundaryCollisionMode;

    Emitter2D(PApplet p){
        parent = p;
        showEmitter = true;
        emitterPosition = new PVector(0f,0f);
    }

    public collisionMode getBoundaryCollisionMode() {
        if (boundaryCollisionMode != null) return boundaryCollisionMode;
        else return collisionMode.DIE;
    }

    public void setBoundaryCollisionMode(collisionMode boundaryCollisionMode) {
        this.boundaryCollisionMode = boundaryCollisionMode;
    }


    public void addNewParticlesToArray(){
        int numParticles = 1;
        for (int i=0; i<numParticles; i++){
            // initialize a new particle...
            Particle2D particle = new Particle2D(this, parent);
            particle.setBoundaryCollisionMode(getBoundaryCollisionMode());

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
        parent.ellipse(this.emitterPosition.x, this.emitterPosition.y, 20, 20);
    }

    public void update(){
        addNewParticlesToArray();
        for (Particle2D particle: this.particles) particle.update();
        removeDeadParticles();
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
}
