package shapes;

import org.jbox2d.common.MathUtils;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import pong.Pong;
import serialize.PongPacket;
import utils.Debugger;
import utils.Registry;

/**
 * Created by sihrc on 7/4/14.
 */
public abstract class PongShape extends PongPacket {
    World world;
    Pong pong;
    Body body;
    FixtureDef fd;
    char color;
    private Debugger debbie = new Debugger(PongShape.class.getSimpleName());

    public PongShape() {
    }

    public PongShape(float x, float y, boolean isBullet, char color, World world, Pong pong) {
        this.world = world;
        this.pong = pong;
        this.color = color;

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(x, y);
        bodyDef.type = setBodyType();

        body = world.createBody(bodyDef);
        body.setBullet(isBullet);

        fd = new FixtureDef();
        debbie.i(this.getClass().getSimpleName());
        fd.userData = Registry.getPacketId(this.getClass());
    }

    public BodyType setBodyType() {
        return BodyType.KINEMATIC;
    }

    /**
     * Get Methods *
     */
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
