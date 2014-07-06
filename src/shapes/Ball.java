package shapes;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.MathUtils;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Transform;

import java.util.Arrays;

import pong.Settings;
import utils.Bytes;

public class Ball extends PongShape {
    private CircleShape shape;
    private char color;
    private float radius;

    public Ball(){}
    public Ball(float x, float y, float r, World world, boolean isBullet, char color) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(x, y);
        bodyDef.type = BodyType.DYNAMIC;

        Body body = world.createBody(bodyDef);
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
        this.radius = r;
        this.body = body;
        this.color = color;
    }

    @Override
    public char getId() {
        return ShapeRegistry.BALL;
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
    public byte[] serialize() {
        byte[] serialized = new byte[9];
        int pointer = 0;

        byte[] id = Bytes.char2Bytes2(getId());
        System.arraycopy(id, 0, serialized,pointer,id.length);
        pointer += 2;

        byte[] rotation = Bytes.float2Byte2(body.getAngle(), MathUtils.TWOPI); // Rotation
        System.arraycopy(rotation, 0, serialized,pointer,rotation.length);
        pointer+=2;

        byte xposition = Bytes.float2Byte(body.getPosition().x, Settings.windowMeters[0]);  // X position
        serialized[pointer++] = xposition;

        byte yposition = Bytes.float2Byte(body.getPosition().y, Settings.windowMeters[1]);  // Y position
        serialized[pointer++] = yposition;

        byte radius = Bytes.float2Byte(shape.getRadius(), Settings.windowMeters[1] / 2f);// Radius
        serialized[pointer++] = radius;

        byte[] color = Bytes.char2Bytes2(this.color);// Color
        System.arraycopy(color, 0, serialized, pointer, color.length);

        return serialized;
    }

    @Override
    public int deserialize(byte[] cereal, int pointer, Graphics graphics) {
        byte[] rotation = Arrays.copyOfRange(cereal, pointer, pointer+=2);

        byte xposition = cereal[pointer++];
        byte yposition = cereal[pointer++];
        byte radius = cereal[pointer++];

        graphics.setColor(Settings.colorMap.get(Arrays.copyOfRange(cereal, pointer, pointer+=2)));
        Circle circle = new Circle(
                Settings.m2p(Bytes.byte2Float(xposition, Settings.windowMeters[0])),
                Settings.m2p(Bytes.byte2Float(yposition, Settings.windowMeters[1])),
                Settings.m2p(Bytes.byte2Float(radius, Settings.windowMeters[1]/2f))
        );
        circle.transform(Transform.createRotateTransform(Bytes.twoByte2Float(rotation, MathUtils.TWOPI)));
        graphics.fill(circle);

        return pointer;
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

	@Override
	public boolean visible() {
		return true;
	}
}
