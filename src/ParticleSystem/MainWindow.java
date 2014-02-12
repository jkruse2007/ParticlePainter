package ParticleSystem;

import processing.core.PApplet;

/**
 * Created by Frank on 03.02.14.
 */


public class MainWindow extends PApplet {

//    public static void main(String args[]){
//        PApplet.main(new String[] {"--present", "ParticleSystem.MainWindow"});
//    }

    private Emitter2D emitter;

    public void setup(){
        frameRate(24);
        size(500, 500);
        emitter = new Emitter2D(this);

        //emitter.setBoundaryCollisionMode(ParticleSystem.collisionMode.STICKY);
        emitter.setParticleLifetime(3);
        emitter.setEmissionRate(1);
        emitter.setInitialParticleSize(10,10);
        emitter.showEmitter = true;
    }


    public void draw(){
        background(255);
        //emitter.emitterPosition.x = mouseX;
        //emitter.emitterPosition.y = mouseY;

        emitter.update();
        emitter.draw();
    }
}
