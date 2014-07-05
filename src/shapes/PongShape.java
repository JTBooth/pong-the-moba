package shapes;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import org.newdawn.slick.geom.Shape;

import pong.Pong;

/**
 * Created by sihrc on 7/4/14.
 */
public abstract class PongShape {
    Body body;

    /** Serialization **/
    public abstract byte[] serialize();
    public abstract Shape deserialize(byte[] cereal);

    /** Absstract Get Methods **/
    public abstract org.jbox2d.collision.shapes.Shape getBoxShape();
    public abstract Shape getSlickShape();
    public Fixture getFixture(){
        return body.getFixtureList();
    }
    public Body getBody() {
        return body;
    }

    /** Remove from world **/
    public void destroy() {
        Pong.pong.removePongShape(this);
        if (body != null) {
           body.getWorld().destroyBody(body);
        }
    }
}
