package shapes;

import org.jbox2d.common.MathUtils;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import org.newdawn.slick.geom.Shape;

import packets.Cereal;
import pong.Pong;

/**
 * Created by sihrc on 7/4/14.
 */
public abstract class PongShape implements Cereal {
    Body body;
    Pong pong;

    /**
     * Absstract Get Methods *
     */
    public abstract org.jbox2d.collision.shapes.Shape getBoxShape();

    public abstract Shape getSlickShape();

    public Fixture getFixture() {
        return body.getFixtureList();
    }

    public Body getBody() {
        return body;
    }


    public float getAngle() {
        float angle = body.getAngle();
        angle = angle % MathUtils.TWOPI;
        if (angle < 0) {
            angle += MathUtils.TWOPI;
        }
        return angle;
    }

    /**
     * Remove from world *
     */
    public void destroy() {
        pong.removePongShape(this);
        if (body != null) {
            body.getWorld().destroyBody(body);
        }
    }
}
