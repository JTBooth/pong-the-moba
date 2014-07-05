package shapes;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.MathUtils;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;

import pong.Settings;

import java.util.Arrays;

import utils.Bytes;

public class Paddle extends PongShape {
    private Body body;
    private PolygonShape shape;
    private float height;
    private char color;


    public Paddle(float x, float y, float length, float width, char color, World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(x, y);
        bodyDef.type = BodyType.KINEMATIC;
        Body body = world.createBody(bodyDef);
        

        PolygonShape polyShape = new PolygonShape();
        polyShape.setAsBox(Settings.paddleWidth / 2, length / 2);
        this.height = length;
        shape = polyShape;

        FixtureDef fd = new FixtureDef();
        fd.friction = 1f;
        fd.shape = polyShape;
        body.createFixture(fd);
        this.color = color;
        this.body = body;
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

        byte length = Bytes.float2Byte(height, Settings.windowMeters[1]);// Length
        serialized[4] = length;

        byte[] color = Bytes.char2Bytes2(this.color);// Color
        System.arraycopy(color, 5, serialized, 0, 2);

        return serialized;
    }

    @Override
    public Shape deserialize(byte[] cereal) {
        byte[] byteRotation = Arrays.copyOfRange(cereal, 0, 2);
        byte byteX = cereal[2];
        byte byteY = cereal[3];
        byte byteHeight = cereal[4];
        byte[] byteColor = Arrays.copyOfRange(cereal, 5, 7);

        /** Create a rectangle given position and size **/
        Rectangle rect = new Rectangle(
                Settings.m2p(Bytes.byte2Float(byteX, Settings.windowMeters[0])),
                Settings.m2p(Bytes.byte2Float(byteY, Settings.windowMeters[1])),
                Settings.paddleWidth,
                Settings.m2p(Bytes.byte2Float(byteHeight, Settings.windowMeters[1]/2f))
        );

        /** Get Color **/
        this.color = Bytes.twoBytes2Char(byteColor);

        /** Polygon to rotate**/
        Polygon polygon = new Polygon(rect.getPoints());
        polygon.transform(Transform.createRotateTransform(Bytes.twoByte2Float(byteRotation, MathUtils.TWOPI)));

        return polygon;
    }

    @Override
    public org.jbox2d.collision.shapes.Shape getBoxShape() {
        return null;
    }

    @Override
    public Shape getSlickShape() {
        return null;
    }

    public Body getBody() {
        return body;
    }

    public float[] getPointsInPixels() {
        Vec2 center = body.getPosition();
        Vec2[] points = Arrays.copyOf(shape.getVertices(), 4);
        shape.setAsBox(Settings.paddleWidth / 2, height / 2, new Vec2(0, 0), body.getAngle());
        float[] ret = new float[points.length * 2];
        int j = 0;
        for (int i = 0; i < points.length; ++i, j += 2) {
            ret[j] = Settings.m2p((points[i].x + center.x));
            ret[j + 1] = Settings.m2p((points[i].y + center.y));
        }
        return ret;

    }


    public PolygonShape getShape() {
        return shape;
    }

	@Override
	public boolean visible() {
		return true;
	}
}
