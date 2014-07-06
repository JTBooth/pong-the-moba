package shapes;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.MathUtils;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;

import java.util.Arrays;

import pong.Settings;
import utils.Bytes;
import utils.Debugger;

public class Paddle extends PongShape {
    private PolygonShape shape;
    private float height;
    private char color;

    public Paddle(){}
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
    public char getId() {
        return ShapeRegistry.PADDLE;
    }

    @Override
    public byte[] serialize() {
        byte[] serialized = new byte[9];
        int pointer = 0;

        byte[] id = Bytes.char2Bytes2(getId());
        System.arraycopy(id, 0, serialized,pointer, pointer+=id.length);
        byte[] rotation = Bytes.float2Byte2(body.getAngle(), MathUtils.TWOPI); // Rotation
        System.arraycopy(rotation, 0, serialized,pointer, rotation.length);
        pointer += 2;

        byte xposition = Bytes.float2Byte(body.getPosition().x, Settings.windowMeters[0]);  // X position
        serialized[pointer++] = xposition;

        byte yposition = Bytes.float2Byte(body.getPosition().y, Settings.windowMeters[1]);  // Y position
        serialized[pointer++] = yposition;

        byte length = Bytes.float2Byte(height, Settings.windowMeters[1]);// Length
        serialized[pointer++] = length;

        byte[] color = Bytes.char2Bytes2(this.color);// Color
        System.arraycopy(color, 0, serialized, pointer, color.length);
        pointer += 2;

        Debugger.debugger.i("Serialized byte array: " + Arrays.toString(serialized));
        return serialized;
    }

    @Override
    public int deserialize(byte[] cereal, int pointer, Graphics graphics) {
        byte[] byteRotation = Arrays.copyOfRange(cereal, pointer, pointer += 2);

        byte byteX = cereal[pointer++];
        byte byteY = cereal[pointer++];
        byte byteHeight = cereal[pointer++];
        byte[] byteColor = Arrays.copyOfRange(cereal, pointer, pointer += 2);

        /** Create a rectangle given position and size **/
        Rectangle rect = new Rectangle(
                Settings.m2p(Bytes.byte2Float(byteX, Settings.windowMeters[0])),
                Settings.m2p(Bytes.byte2Float(byteY, Settings.windowMeters[1])),
                Settings.paddleWidth,
                Settings.m2p(Bytes.byte2Float(byteHeight, Settings.windowMeters[1]/2f))
        );

        Debugger.debugger.i("Rectangle Created " + Arrays.toString(rect.getPoints()));

        /** Get Color **/
        this.color = Bytes.twoBytes2Char(byteColor);
        Debugger.debugger.d("COLOR " + color);

        /** Polygon to rotate**/
        Polygon polygon = new Polygon(rect.getPoints());
        polygon.transform(Transform.createRotateTransform(Bytes.twoByte2Float(byteRotation, MathUtils.TWOPI)));

        graphics.setColor(Settings.colorMap.get(this.color));
        graphics.fill(polygon);
        return pointer;
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
