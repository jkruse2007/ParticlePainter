package ParticleSystem;
/**
 * Created by Frank on 03.02.14.
 */

import processing.core.PApplet;
import processing.core.PVector;





/**
 * A basic Particle class with position, velocity and acceleration attributes.
 * It also provides attributes for setting visibility state and lifetime as
 * well as some control over boundary collisions.
 */
class Particle2D {

    private static PApplet parent;
    private int age;
    private float lifetime;

    public PVector position;
    public PVector velocity;
    public PVector acceleration;
    public boolean isAlive;
    public boolean isVisible;
    public collisionMode boundaryCollisionMode;
    public PVector size;


    Particle2D(PApplet p){
        parent = p;
        isAlive = true;
        isVisible = true;
        size = new PVector(1f,1f);
        age = 0;
        setLifetime(-1); // lives infinitely
        boundaryCollisionMode = collisionMode.DIE;
        position = new PVector(0f,0f);
        velocity = new PVector(0f,0f);
        acceleration = new PVector(0f,0f);
    }

    /**
     * Update the particle state
     */
    public void update(){
        if (lifetime >= 0){
            if (age > PApplet.round(lifetime * parent.frameRate))
                isAlive=false;
        }

        if (isAlive){
            checkBoundary();
            velocity.add(acceleration);
            position.add(velocity);
            age++;
        }
    }

    /**
     * Display the particle
     */
    public void draw(){
        if (isVisible) drawShape();
    }


    // Handle the collision with the boundary based on the current collisionMode.
    private void checkBoundary(){
        if (boundaryCollisionMode == collisionMode.NONE) return;

        switch (boundaryCollisionMode){

            case WRAP:
                if (position.x > parent.width) position.x = 0;
                if (position.x < 0) position.x = parent.width;
                if (position.y > parent.height)  position.y = 0;
                if (position.y < 0) position.y = parent.height;
                break;

            case BOUNCE:
                if ((position.x >= parent.width) || (position.x <= 0))velocity.x *= -1;
                if ((position.y >= parent.height) || (position.y <= 0))velocity.y *= -1;
                break;

            case STICKY:
                if (position.x >= parent.width){
                    position.x = parent.width;
                    velocity.x = 0;
                    velocity.y = 0;
                }
                if (position.x <= 0){
                    position.x = 0;
                    velocity.x = 0;
                    velocity.y = 0;
                }
                if (position.y >= parent.height){
                    position.y = parent.height;
                    velocity.x = 0;
                    velocity.y = 0;
                }
                if (position.y <= 0){
                    position.y = 0;
                    velocity.x = 0;
                    velocity.y = 0;
                }
                break;

            default: // DIE
                if (position.x < 0 || position.x > parent.width
                        || position.y < 0 || position.y > parent.height)
                    isAlive = false;

        }
    }


    // Define a shape for the particle
    private void drawShape(){
        parent.noStroke();
        parent.fill(0, 0, 0);

        if (lifetime >= 0)
            // fade with age...
            parent.fill(0, 0, 0, (PApplet.map((lifetime - age / parent.frameRate),
                    0, lifetime,
                    0, 255)));

        parent.ellipse(position.x, position.y, size.x, size.y);
    }

    /**
     * Set the lifetime of the particle.
     * @param lifetime The particle's lifetime in seconds.
     *                 lifetime < 0 means the particle will live forever.
     */
    public void setLifetime(float lifetime) {
        this.lifetime = lifetime;
    }
}
