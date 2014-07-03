package pong;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;

import java.util.Arrays;

public class SolidRect {
    private Body body;
    private PolygonShape shape;
    private float width;
    private float height;


    public SolidRect(float x, float y, float width, float height, BodyType bodyType, World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(x, y);
        bodyDef.type = bodyType;
        Body body = world.createBody(bodyDef);

        PolygonShape polyShape = new PolygonShape();
        polyShape.setAsBox(width / 2, height / 2);
        this.width = width;
        this.height = height;
        shape = polyShape;


        FixtureDef fd = new FixtureDef();
        fd.density = 1.0f;
        fd.friction = 1f;
        fd.shape = polyShape;
        body.createFixture(fd);
        this.body = body;
    }

    public Body getBody() {
        return body;
    }

    public float[] getPointsInPixels() {
        Vec2 center = body.getPosition();
        Vec2[] points = Arrays.copyOf(shape.getVertices(), 4);
        shape.setAsBox(width / 2, height / 2, new Vec2(0, 0), body.getAngle());
        float[] ret = new float[points.length * 2];
        int j = 0;
        for (int i = 0; i < points.length; ++i, j += 2) {
            ret[j] =  Settings.m2p((points[i].x + center.x));
            ret[j + 1] =  Settings.m2p((points[i].y + center.y));
        }
        return ret;

    }


    public PolygonShape getShape() {
        return shape;
    }

    public void destroy() {
        body.getWorld().destroyBody(body);
        Pong.pong.removeSolidPiece(this);
    }
}
