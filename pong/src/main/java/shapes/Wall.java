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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pong.Pong;
import serialize.Packet;
import serialize.Pattern;
import utils.Debugger;
import utils.Settings;

public class Wall extends PongShape {
    Debugger debbie = new Debugger(Wall.class.getSimpleName());
    private Body body;
    private PolygonShape shape;
    private float height;
    private char color;
    private boolean visible;

    public Wall() {
    }

    public Wall(float x, float y, float height, float width, float angle,
                boolean visible, char color, World world, Pong pong) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(x, y);
        bodyDef.type = BodyType.KINEMATIC;
        bodyDef.angle = angle;
        Body body = world.createBody(bodyDef);

        PolygonShape polyShape = new PolygonShape();
        polyShape.setAsBox(width / 2, height / 2);
        this.height = height;
        shape = polyShape;

        FixtureDef fd = new FixtureDef();
        fd.friction = 0f;
        fd.shape = polyShape;
        body.createFixture(fd);
        this.color = color;
        this.body = body;
        this.visible = visible;
        this.pong = pong;
    }

    @Override
    public List<Object> setSerialData() {
        return new ArrayList<Object>(){{
            add(getAngle());                      //ROTATION
            add(body.getPosition().x);            // X
            add(body.getPosition().y);            // Y
            add(height);                          // Length
            add(color);                           //COLOR
        }};
    }

    @Override
    public List<Packet> getSerialPattern() {
        return new ArrayList<Packet>(){{
            add(new Packet(Pattern.FLOAT2B,  MathUtils.TWOPI));                      //ROTATION
            add(new Packet(Pattern.FLOAT2B,  Settings.windowMeters[0]));             // X
            add(new Packet(Pattern.FLOAT2B,  Settings.windowMeters[1]));             // Y
            add(new Packet(Pattern.FLOAT1B,  Settings.windowMeters[1] / 2f));        // Length
            add(new Packet(Pattern.CHAR2B));                                         // COLOR
        }};
    }

    @Override
    public void extractData(List<Packet> data, Graphics graphics) {
        /** Create a rectangle given position and size **/
        Rectangle rect =
                new Rectangle(Settings.m2p((Float) data.get(1).data),
                              Settings.m2p((Float) data.get(2).data),
                              Settings.paddleWidth, //FIXME - well.. because.. lol
                              Settings.m2p((Float) data.get(3).data));

        /** Get Color **/
        this.color = (Character) data.get(4).data;

        /** Polygon to rotate **/
        Polygon polygon = new Polygon(rect.getPoints());
        polygon.transform(Transform.createRotateTransform((Float) data.get(0).data));

        debbie.i("WALL points deserialized: " + Arrays.toString(polygon.getPoints()));
        graphics.setColor(Settings.colorMap.get(color));
        graphics.fill(polygon);
    }

    @Override
    public boolean shouldSerialize() {
        return visible;
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
        shape.setAsBox(Settings.paddleWidth / 2, height / 2, new Vec2(0, 0),
                body.getAngle());
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
