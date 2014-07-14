package shapes;

import org.jbox2d.common.MathUtils;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Shape;

import java.util.Arrays;

import packets.Cereal;
import pong.Pong;
import utils.Bytes;
import utils.Debugger;

/**
 * Created by sihrc on 7/4/14.
 */
public abstract class PongShape implements Cereal {
    Body body;
    Pong pong;
    
    /** Serialization **/
    public byte[] serialize(){
        try {
            return this.serialize();
        } catch (IllegalArgumentException e) {
            return new byte[0];
        }
    }


    /** Absstract Get Methods **/
    public abstract org.jbox2d.collision.shapes.Shape getBoxShape();
    public abstract Shape getSlickShape();

    public Fixture getFixture(){
        return body.getFixtureList();
    }
    public Body getBody() {
        return body;
    }

    public static void render(byte[] bytes, Graphics graphics){
        int pointer = 0;
        char shape;
        Debugger.debugger.i("Entering while loop");
        while (pointer < bytes.length){
            shape = Bytes.twoBytes2Char(Arrays.copyOfRange(bytes, pointer, pointer += 2));
            Debugger.debugger.i(ShapeRegistry.get(shape).getClass().getSimpleName());
            pointer = ShapeRegistry.get(shape).deserialize(bytes, pointer, graphics);
        }
    }

    public float getAngle() {
        float angle = body.getAngle();
        angle = angle % MathUtils.TWOPI;
        if (angle < 0) {
            angle += MathUtils.TWOPI;
        }
        return angle;
    }

    /** Remove from world **/
    public void destroy() {
        pong.removePongShape(this);
        if (body != null) {
           body.getWorld().destroyBody(body);
        }
    }
}
