/**
 * Created by Frank on 03.02.14.
 */

import processing.core.PApplet;
import processing.core.PVector;


enum collisionMode {NONE,BOUNCE,STICKY,DIE}

public class Particle2D {

    private PApplet parent;
    private Emitter2D emitter;
    private int age;
    private collisionMode boundaryCollisionMode;
    private float sizeX = 10;
    private float sizeY = 10;

    public PVector position;
    public PVector velocity;
    public PVector acceleration;
    public boolean isAlive = true;
    public boolean isVisible = true;



    Particle2D(Emitter2D e, PApplet p){
        parent = p;
        emitter = e;
        position = new PVector(0f,0f);
        velocity = new PVector(0f,0f);
        acceleration = new PVector(0f,0f);
        age = 0;
        setBoundaryCollisionMode(emitter.getBoundaryCollisionMode());
    }


    public void update(){
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

        switch (boundaryCollisionMode){

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
        parent.fill(0);
        parent.ellipse(position.x, position.y, sizeX, sizeY);
    }

    public Emitter2D getEmitter() {
        return emitter;
    }

    public void setBoundaryCollisionMode(collisionMode boundaryCollisionMode) {
        this.boundaryCollisionMode = boundaryCollisionMode;
    }
}
