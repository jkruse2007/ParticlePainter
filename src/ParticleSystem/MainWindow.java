package ParticleSystem;

import processing.core.PApplet;
import processing.core.PVector;

/**
 * Created by Frank on 03.02.14.
 */


public class MainWindow extends PApplet {

//    public static void main(String args[]){
//        PApplet.main(new String[] {"--present", "ParticleSystem.MainWindow"});
//    }

    private Emitter2D emitter;
    private float t = 0;

    public void setup(){
        frameRate(48);
        size(500, 500);
        emitter = new Emitter2D(this);
        emitter.showEmitter = true;

        emitter.setParticleLifetime(3);
        emitter.setEmissionRate(500);
        //emitter.setEmissionDirection(angle2Vector(0));
        emitter.setEmissionSpeed(0.5f);
        emitter.setEmissionSpread(30);
        emitter.setInitialParticleSize(6,6);
        emitter.setBoundaryCollisionMode(collisionMode.DIE);

    }


    public void draw(){
        background(255);
        emitter.setEmitterPosition(mouseX, mouseY);
        emitter.setEmissionDirection(new PVector(PApplet.sin(t), PApplet.cos(t)));
        t+=0.2;

        emitter.update();
        emitter.draw();
    }


    private PVector angle2Vector(float angle){
        angle = angle+90;
        PVector dir = new PVector(0,0);
        dir.x = PApplet.sin(PApplet.radians(angle));
        dir.y = PApplet.cos(PApplet.radians(angle));
        dir.normalize();
        return dir;
    }
}
