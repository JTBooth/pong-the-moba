package shapes;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.MathUtils;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Transform;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pong.Pong;
import serialize.Packet;
import serialize.Pattern;
import utils.Debugger;
import utils.Settings;

public class Paddle extends PongShape {
    Debugger debbie = new Debugger(Paddle.class.getSimpleName());

    PolygonShape shape;
    /**
     * Shape Attributes *
     */
    private float length;

    /**
     * Physics Variables *
     */
    private float yVelocity = 0f;
    private int rotationReq = 0;

    public Paddle() {}

    public Paddle(float x, float y, float length, char color, World world, Pong pong) {
        super(x, y, false, color, world, pong);
        shape = new PolygonShape();
        shape.setAsBox(Settings.paddleWidth / 2f, length / 2f);

        fd.friction = 1f;
        fd.shape = shape;
        body.createFixture(fd);

        this.length = Settings.paddleLength;
    }

    /**
     * Set the paddle's vertical velocity *
     */
    public void setYVelocity(float v) {
        yVelocity += v;
    }

    /**
     * Perform rubber band rotation *
     */
    public void rubberBandRotation(float isClockwise) {
        rotationReq += isClockwise;
    }

    /**
     * Execute Paddle Changes *
     */
    public void execute() {
        /** Setting velocity **/
        if (!(body.getPosition().y > Settings.windowMeters[1] - .01f && yVelocity > 0)
                && !(body.getPosition().y < .01f && yVelocity < 0)) {
            if (yVelocity != 0) {
                debbie.d(yVelocity + " velocity desired with " + body.getPosition().y);
            }
            body.setLinearVelocity(new Vec2(0, yVelocity));
        } else {
            debbie.d("DENIED: " + yVelocity + " velocity desired with " + body.getPosition().y);
            body.setLinearVelocity(new Vec2(0, 0));
        }

        yVelocity = 0;

        /** Performing Rubber band **/
        /** Getting current angle of body **/
        float currentAngle = body.getAngle();
        float currentVelocity = body.getAngularVelocity();
        if (currentAngle > 180) {
            currentAngle -= 360;
        }

        /** Create initial force **/
        float force = 0f;

        /** Paddle Push **/
        force += rotationReq * (Settings.maxPaddleRotateAngle * Settings.paddleSpringConstant);

        /** Rubber Band **/
        force += -currentAngle * (Settings.paddleSpringConstant);

        /** Damper Force **/
        force += -currentVelocity * Settings.paddleDampingConstant;

        body.setAngularVelocity(currentVelocity + force);

        rotationReq = 0;

    }

    @Override
    public List<Object> setSerialData() {
        return new ArrayList<Object>(){{
            add(getAngle());                      //ROTATION
            add(body.getPosition().x);            // X
            add(body.getPosition().y);            // Y
            add(length);                          // Length
            add(color);                           //COLOR
        }};
    }

    @Override
    public List<Packet> getSerialPattern() {
        return new ArrayList<Packet>(){{
            add(new Packet(Pattern.FLOAT2B, MathUtils.TWOPI));                      //ROTATION
            add(new Packet(Pattern.FLOAT2B,  Settings.windowMeters[0]));            // X
            add(new Packet(Pattern.FLOAT2B,  Settings.windowMeters[1]));            // Y
            add(new Packet(Pattern.FLOAT1B,  Settings.windowMeters[1]));            // Length
            add(new Packet(Pattern.CHAR2B));                                        //COLOR
        }};
    }

    @Override
    public void extractData(List<Packet> data, Graphics graphics) {
        int x = Settings.m2p((Float) data.get(1).data);
        int y = Settings.m2p((Float) data.get(2).data);
        int width = Settings.m2p(Settings.paddleWidth);
        int length =  Settings.m2p((Float) data.get(3).data);

        /** Create a rectangle given position and size **/
        Rectangle rect = new Rectangle(
                x - width / 2,
                y - length / 2,
                width,
                length
        );

        /** Get Color **/
        this.color = (Character) data.get(4).data;
        graphics.setColor(Settings.colorMap.get(this.color));

        /** Polygon to rotate**/
        Polygon polygon = new Polygon(rect.getPoints());
        graphics.fill(polygon.transform(Transform.createRotateTransform(
                (Float) data.get(0).data,
                polygon.getCenterX(),
                polygon.getCenterY()
        )));

        debbie.i(data.get(1).data + " x position, " + data.get(2).data+ " y position, " + width + " width, " + length + " length");
        debbie.i(Settings.colorMap.get(this.color) + " color");
    }

    @Override
    public boolean shouldSerialize() {
        return true;
    }

    public Body getBody() {
        return body;
    }

    public float[] getPointsInPixels() {
        Vec2 center = body.getPosition();
        Vec2[] points = Arrays.copyOf(shape.getVertices(), 4);
        shape.setAsBox(Settings.paddleWidth / 2, length / 2, new Vec2(0, 0), body.getAngle());
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
}
