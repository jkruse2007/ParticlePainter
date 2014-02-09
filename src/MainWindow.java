/**
 * Created by Frank on 03.02.14.
 */

import processing.core.PApplet;



public class MainWindow extends PApplet {


//    public static void main(String args[]){
//        PApplet.main(new String[] {"--present", "MainWindow"});
//    }


    Emitter2D emitter = new Emitter2D(this);

    public void setup(){
        size(500, 500);
        //emitter.setBoundaryCollisionMode(collisionMode.STICKY);
    }


    public void draw(){
        background(255);
        emitter.emitterPosition.x = mouseX;
        emitter.emitterPosition.y = mouseY;

        emitter.update();
        emitter.draw();
    }
}
