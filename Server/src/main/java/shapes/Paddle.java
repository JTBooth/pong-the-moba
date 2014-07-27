package shapes;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.MathUtils;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pong.Pong;
import serialization.Packet;
import serialization.Pattern;
import utils.Debugger;
import utils.Settings;

public class Paddle extends PongShape {
    Debugger debbie = new Debugger(Paddle.class.getSimpleName());

    PolygonShape shape;
    /**
     * Shape Attributes *
     */
    private float length;
    private byte spriteSheetId;
    private byte spriteSheetFrame;


    /**
     * Physics Variables *
     */
    private float yVelocity = 0f;
    private int rotationReq = 0;

    public Paddle(float x, float y, float length, byte spriteSheetId, World world, Pong pong) {
        super(x, y, false, '0', world, pong);

        shape = new PolygonShape();
        shape.setAsBox(Settings.paddleWidth / 2f, length / 2f);


        fd.friction = 1f;
        fd.shape = shape;
        body.createFixture(fd);

        this.spriteSheetId = spriteSheetId;
        this.spriteSheetFrame = (byte) 0;
        this.length = length;
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
    public boolean shouldSerialize() {
        return true;
    }

    @Override
    public List<Packet> setSerialData() {
        return new ArrayList<Packet>() {{
            add(Packet.data(Pattern.FLOAT2B, getAngle(), MathUtils.TWOPI));                      //ROTATION
            add(Packet.data(Pattern.FLOAT2B, body.getPosition().x, Settings.windowMeters[0]));   // X
            add(Packet.data(Pattern.FLOAT2B, body.getPosition().y, Settings.windowMeters[1]));   // Y
            add(Packet.data(Pattern.FLOAT1B, length, Settings.windowMeters[1]));                 // Length
            add(Packet.data(Pattern.BYTE, spriteSheetId));                                       // Spritesheet ID
            add(Packet.data(Pattern.BYTE, spriteSheetFrame));                                    // Spritesheet frame
        }};
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
