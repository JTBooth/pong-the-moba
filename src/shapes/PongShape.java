package shapes;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Shape;

import java.util.Arrays;

import pong.Pong;
import utils.Bytes;

/**
 * Created by sihrc on 7/4/14.
 */
public abstract class PongShape {
    Body body;
    
    /** Serialization **/
    public abstract byte[] serialize();
    public abstract int deserialize(byte[] cereal, int pointer, Graphics graphics);
    public abstract boolean visible();

    /** Absstract Get Methods **/
    public abstract org.jbox2d.collision.shapes.Shape getBoxShape();
    public abstract Shape getSlickShape();
    public abstract char getId();
    public Fixture getFixture(){
        return body.getFixtureList();
    }
    public Body getBody() {
        return body;
    }

    public static void render(byte[] bytes, Graphics graphics){
        int pointer = 0;
        char shape;
        while (pointer < bytes.length){
            shape = Bytes.twoBytes2Char(Arrays.copyOfRange(bytes, pointer+=2, 2));
            pointer = ShapeRegistry.get(shape).deserialize(bytes, pointer, graphics);
        }
    }

    /** Remove from world **/
    public void destroy() {
        Pong.pong.removePongShape(this);
        if (body != null) {
           body.getWorld().destroyBody(body);
        }
    }
}
