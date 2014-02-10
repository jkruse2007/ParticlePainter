/**
 * Created by Frank on 03.02.14.
 */

import processing.core.PApplet;
import processing.core.PVector;


enum collisionMode {NONE,BOUNCE,STICKY,DIE}

public class Particle2D {

    private PApplet parent;
    private Number lifetime; //lifetime of the particle in seconds - particle will live forever if this is null
    private collisionMode boundaryCollisionMode;

    private int age;
    private float sizeX = 10;
    private float sizeY = 10;

    public PVector position;
    public PVector velocity;
    public PVector acceleration;
    public boolean isAlive = true;
    public boolean isVisible = true;



    Particle2D(PApplet p){
        parent = p;
        position = new PVector(0f,0f);
        velocity = new PVector(0f,0f);
        acceleration = new PVector(0f,0f);
        age = 0;
    }


    public void update(){
        if (getLifetime() != null){
            if (age > (int)(getLifetime().floatValue() * parent.frameRate)) isAlive=false;
        }

        if (isAlive){
            checkBoundary();
            velocity.add(acceleration);
            position.add(velocity);
            age++;
        }
    }


    public void draw(){
        if (isVisible) drawShape();
    }

    private void checkBoundary(){

        switch (getBoundaryCollisionMode()){

            case NONE:
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


    private void drawShape(){
        parent.noStroke();
        parent.fill(0, 0, 0);
        if (getLifetime() != null)
            parent.fill(0, 0, 0, (PApplet.map((getLifetime().floatValue() - age / parent.frameRate),
                    0, getLifetime().floatValue(),
                    0, 255)));

        parent.ellipse(position.x, position.y, sizeX, sizeY);
    }


    public void setBoundaryCollisionMode(collisionMode boundaryCollisionMode) {
        this.boundaryCollisionMode = boundaryCollisionMode;
    }

    private collisionMode getBoundaryCollisionMode() {
        if (boundaryCollisionMode == null) return collisionMode.DIE;
        return this.boundaryCollisionMode;
    }

    public void setLifetime(Number lifetime) {
        this.lifetime = lifetime;
    }

    public Number getLifetime() {
        return lifetime;
    }
}
