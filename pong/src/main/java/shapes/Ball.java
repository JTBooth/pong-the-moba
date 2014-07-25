package shapes;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.MathUtils;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Transform;

import java.util.ArrayList;
import java.util.List;

import pong.Pong;
import serialize.Packet;
import serialize.Pattern;
import utils.Debugger;
import utils.Settings;

public class Ball extends PongShape {
    Debugger debbie = new Debugger(Ball.class.getSimpleName());

    CircleShape shape;
    Circle circle = new Circle(0,0,0);

    public Ball() {}

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
    public List<Object> setSerialData() {
        return new ArrayList<Object>(){{
            add(getAngle());                                     //ROTATION
            add(body.getPosition().x);                           // X
            add(body.getPosition().y);                           // Y
            add(shape.getRadius());                              // RADIUS
            add(color);                                          //COLOR
        }};
    }

    @Override
    public List<Packet> getSerialPattern() {
        return new ArrayList<Packet>(){{
            add(new Packet(Pattern.FLOAT2B, MathUtils.TWOPI));                      //ROTATION
            add(new Packet(Pattern.FLOAT2B, Settings.windowMeters[0]));             // X
            add(new Packet(Pattern.FLOAT2B, Settings.windowMeters[1]));             // Y
            add(new Packet(Pattern.FLOAT1B, Settings.windowMeters[1] / 2f));        // RADIUS
            add(new Packet(Pattern.CHAR2B));                                        //COLOR
        }};
    }

    @Override
    public BodyType setBodyType() {
        return BodyType.DYNAMIC;
    }

    @Override
    public void extractData(List<Packet> data, Graphics graphics) {
        graphics.setColor(Settings.colorMap.get((Character) data.get(4).data));

        circle.setCenterX(Settings.m2p((Float) data.get(1).data));
        circle.setCenterY(Settings.m2p((Float) data.get(2).data));
        circle.setRadius(Settings.m2p((Float) data.get(3).data));
        circle.transform(Transform.createRotateTransform((Float) data.get(0).data));
        graphics.fill(circle);
    }

    @Override
    public boolean shouldSerialize() {
        return true;
    }

    public void setPosition(float x, float y) {
        body.setTransform(new Vec2(x, y), 0);
    }
}
