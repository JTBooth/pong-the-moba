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
import org.newdawn.slick.geom.Circle;

import pong.Pong;
import pong.Settings;

import java.util.Arrays;

import utils.Bytes;

public class Ball extends PongShape {
    private CircleShape shape;
    private Body body;
    private char color;
    private float radius;

    public Ball(float x, float y, float r, World world, boolean isBullet, char color) {
        System.out.println("CALLED CONSTRUCTOR");
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
        byte[] serialized = new byte[7];

        byte[] rotation = Bytes.float2Byte2(body.getAngle(), MathUtils.TWOPI); // Rotation
        System.arraycopy(rotation, 0, serialized,0,2);

        byte xposition = Bytes.float2Byte(body.getPosition().x, Settings.windowMeters[0]);  // X position
        serialized[2] = xposition;

        byte yposition = Bytes.float2Byte(body.getPosition().y, Settings.windowMeters[1]);  // Y position
        serialized[3] = yposition;

        byte radius = Bytes.float2Byte(shape.getRadius(), Settings.windowMeters[1] / 2f);// Radius
        serialized[4] = radius;

        byte[] color = Bytes.char2Bytes2(this.color);// Color
        System.arraycopy(color, 5, serialized, 0, 2);

        return serialized;
    }

    @Override
    public org.newdawn.slick.geom.Shape deserialize(byte[] cereal) {
        byte[] rotation = Arrays.copyOfRange(cereal, 0, 2);
        byte xposition = cereal[2];
        byte yposition = cereal[3];
        byte radius = cereal[4];
        byte[] color = Arrays.copyOfRange(cereal, 5, 7);

        return new Circle(
                Settings.m2p(Bytes.byte2Float(xposition, Settings.windowMeters[0])),
                Settings.m2p(Bytes.byte2Float(yposition, Settings.windowMeters[1])),
                Settings.m2p(Bytes.byte2Float(radius, Settings.windowMeters[1]/2f))
        );
    }

    @Override
    public Shape getBoxShape() {
        return null;
    }

    @Override
    public org.newdawn.slick.geom.Shape getSlickShape() {
        return null;
    }


    public Body getBody() {
        return body;
    }

    public void destroy() {
        body.getWorld().destroyBody(body);
        Pong.pong.removePongShape(this);
    }

    public void setPosition(float x, float y) {
        body.setTransform(new Vec2(x, y), 0);
    }

	@Override
	public boolean visible() {
		return true;
	}
}
