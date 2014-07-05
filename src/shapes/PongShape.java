package shapes;

import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import pong.Pong;

/**
 * Created by sihrc on 7/4/14.
 */
public abstract class PongShape {
    private Body body;

    public void construct(World world) {
        this.body = world.createBody(getBodyDef());
        body.createFixture(getFixtureDef());
    }

    /** Serialization **/
    public abstract byte[] serialize();
    public abstract Shape deserialize(byte[] cereal);

    /** Absstract Get Methods **/
    public abstract BodyDef getBodyDef();
    public abstract FixtureDef getFixtureDef();
    public abstract Shape getShape();
    public Fixture getFixture(){
        return body.getFixtureList();
    }
    public Body getBody() {
        return body;
    }

    /** Remove from world **/
    public void destroy() {
        body.getWorld().destroyBody(body);
        Pong.pong.removePongShape(this);
    }
}
