package shapes;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.MathUtils;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;

import java.util.ArrayList;
import java.util.List;

import pong.Pong;
import serialization.Packet;
import serialization.Pattern;
import utils.Debugger;
import utils.Settings;

public class Wall extends PongShape {
    Debugger debbie = new Debugger(shapes.Wall.class.getSimpleName());

    PolygonShape shape;

    private float height;
    private boolean visible;

    public Wall(float x, float y, float height, float width,
                boolean visible, char color, World world, Pong pong) {
        super(x, y, false, color, world, pong);

        shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2);

        fd.friction = 0f;
        fd.shape = shape;
        body.createFixture(fd);

        this.height = height;
        this.visible = visible;
    }

    @Override
    public boolean shouldSerialize() {
        return visible;
    }

    @Override
    public List<Packet> setSerialData() {
        return new ArrayList<Packet>() {{
            add(Packet.data(Pattern.FLOAT2B, getAngle(), MathUtils.TWOPI));                      //ROTATION
            add(Packet.data(Pattern.FLOAT2B, body.getPosition().x, Settings.windowMeters[0]));   // X
            add(Packet.data(Pattern.FLOAT2B, body.getPosition().y, Settings.windowMeters[1]));   // Y
            add(Packet.data(Pattern.FLOAT1B, height, Settings.windowMeters[1] / 2f));            // Length
            add(Packet.data(Pattern.CHAR2B, color));                                            // COLOR
        }};
    }

    public Body getBody() {
        return body;
    }
}
