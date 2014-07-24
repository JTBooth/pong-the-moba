package shapes;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.MathUtils;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
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
    private CircleShape shape;
    private char color;

    public Ball() {
    }

    public Ball(float x, float y, float r, World world, boolean isBullet, char color, Pong pong) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(x, y);
        bodyDef.type = BodyType.DYNAMIC;

        body = world.createBody(bodyDef);
        body.setBullet(isBullet);
        CircleShape circleShape = new CircleShape();
        circleShape.m_radius = r;
        shape = circleShape;

        FixtureDef fd = new FixtureDef();
        fd.density = 1.0f;
        fd.friction = 1f;
        fd.restitution = 1.0f;
        fd.shape = shape;
        body.createFixture(fd);
        this.color = color;
        this.pong = pong;
    }

    public float getX() {
        return Settings.m2p(body.getPosition().x);
    }

    public float getY() {
        return Settings.m2p(body.getPosition().y);
    }

    public float getRadius() {
        return Settings.m2p(shape.m_radius);
    }

    public char getColor() {
        return color;
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
            add(new Packet(Pattern.FLOAT2B, Settings.windowMeters[0]));   // X
            add(new Packet(Pattern.FLOAT2B, Settings.windowMeters[1]));   // Y
            add(new Packet(Pattern.FLOAT1B, Settings.windowMeters[1] / 2f)); // RADIUS
            add(new Packet(Pattern.CHAR2B));                                          //COLOR
        }};
    }

    @Override
    public void extractData(List<Packet> data, Graphics graphics) {
        graphics.setColor(Settings.colorMap.get((Character) data.get(4).data));

        Circle circle = new Circle(
                Settings.m2p((Float) data.get(1).data),
                Settings.m2p((Float) data.get(2).data),
                Settings.m2p((Float) data.get(3).data)
        );
        circle.transform(Transform.createRotateTransform((Float) data.get(0).data));
        graphics.fill(circle);
    }

    @Override
    public boolean shouldSerialize() {
        return true;
    }


    @Override
    public Shape getBoxShape() {
        return null;
    }

    @Override
    public org.newdawn.slick.geom.Shape getSlickShape() {
        return null;
    }

    public void setPosition(float x, float y) {
        body.setTransform(new Vec2(x, y), 0);
    }
}
