package shapes;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Transform;

import java.util.ArrayList;
import java.util.List;

import serialize.Packet;
import serialize.Pattern;
import serialize.PongPacket;
import utils.Debugger;
import utils.Settings;

public class Ball extends PongPacket {
    Debugger debbie = new Debugger(Ball.class.getSimpleName());

    Circle circle = new Circle(0, 0, 0);

    public Ball() {
    }

    @Override
    public List<Packet> getSerialPattern() {
        return new ArrayList<Packet>() {{
            add(new Packet(Pattern.FLOAT2B, 6.28f));                      //ROTATION
            add(new Packet(Pattern.FLOAT2B, Settings.windowMeters[0]));             // X
            add(new Packet(Pattern.FLOAT2B, Settings.windowMeters[1]));             // Y
            add(new Packet(Pattern.FLOAT2B, Settings.windowMeters[1] / 2f));        // RADIUS
            add(new Packet(Pattern.CHAR2B));                                        //COLOR
        }};
    }

    @Override
    public void extractData(List<Packet> data, Graphics graphics) {
        graphics.setColor(Settings.colorMap.get((Character) data.get(4).data));
        circle.setCenterX(Settings.m2p((Float) data.get(1).data));
        circle.setCenterY(Settings.m2p((Float) data.get(2).data));
        circle.setRadius(Settings.m2p((Float) data.get(3).data));
        circle.transform(Transform.createRotateTransform((Float) data.get(0).data));
        graphics.fill(circle);
    }

}
