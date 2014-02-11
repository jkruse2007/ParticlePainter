/**
 * Created by Frank on 03.02.14.
 */

import processing.core.PApplet;
import processing.core.PVector;


enum collisionMode {NONE,BOUNCE,STICKY,DIE}

public class Particle2D {

    private PApplet parent;
    private int age;

    public PVector position;
    public PVector velocity;
    public PVector acceleration;
    public PVector size;
    public boolean isAlive;
    public boolean isVisible;
    public collisionMode boundaryCollisionMode;
    public Number lifetime; //lifetime of the particle in seconds - particle will live forever if this is null


    Particle2D(PApplet p){
        parent = p;
        isAlive = true;
        isVisible = true;
        size = new PVector(1f,1f);
        age = 0;
        lifetime = null;
        boundaryCollisionMode = collisionMode.DIE;
        position = new PVector(0f,0f);
        velocity = new PVector(0f,0f);
        acceleration = new PVector(0f,0f);
    }


    public void update(){
        if (lifetime != null){
            if (age > PApplet.round(lifetime.floatValue() * parent.frameRate))
                isAlive=false;
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
        parent.fill(0, 0, 0);

        if (lifetime != null)
            // fade with age...
            parent.fill(0, 0, 0, (PApplet.map((lifetime.floatValue() - age / parent.frameRate),
                    0, lifetime.floatValue(),
                    0, 255)));

        parent.ellipse(position.x, position.y, size.x, size.y);
    }
}
