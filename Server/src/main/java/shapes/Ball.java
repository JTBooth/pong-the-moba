package shapes;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.MathUtils;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;

import java.util.ArrayList;
import java.util.List;

import pong.Pong;
import serialization.Packet;
import serialization.Pattern;
import utils.Debugger;
import utils.Settings;

public class Ball extends PongShape {
    Debugger debbie = new Debugger(Ball.class.getSimpleName());

    CircleShape shape;

    public Ball(float x, float y, float r, World world, boolean isBullet, char color, Pong pong) {
        super(x, y, isBullet, color, world, pong);

        shape = new CircleShape();
        shape.m_radius = r;

        fd.density = 1.0f;
        fd.friction = 1f;
        fd.restitution = 1.0f;
        fd.shape = shape;

        body.createFixture(fd);
    }

    public float getX() {
        return Settings.m2p(body.getPosition().x);
    }

    public float getY() {
        return Settings.m2p(body.getPosition().y);
    }

    @Override
    public BodyType setBodyType() {
        return BodyType.DYNAMIC;
    }

    @Override
    public boolean shouldSerialize() {
        return true;
    }

    @Override
    public List<Packet> setSerialData() {
        return new ArrayList<Packet>() {{
            add(Packet.data(Pattern.FLOAT2B, getAngle(), MathUtils.TWOPI));                              //ROTATION
            add(Packet.data(Pattern.FLOAT2B, body.getPosition().x, Settings.windowMeters[0]));           // X
            add(Packet.data(Pattern.FLOAT2B, body.getPosition().y, Settings.windowMeters[1]));           // Y
            add(Packet.data(Pattern.FLOAT2B, shape.getRadius(), Settings.windowMeters[1] / 2f));         // RADIUS
            add(Packet.data(Pattern.CHAR2B, color, 0));                                                  //Color
        }};
    }

    public void setPosition(float x, float y) {
        body.setTransform(new Vec2(x, y), 0);
    }
}
