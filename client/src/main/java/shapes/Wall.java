package shapes;


import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Transform;

import java.util.ArrayList;
import java.util.List;

import serialization.Packet;
import serialization.Pattern;
import serialize.PongPacket;
import utils.Debugger;
import utils.Settings;

public class Wall extends PongPacket {
    Debugger debbie = new Debugger(Wall.class.getSimpleName());

    public Wall() {
    }

    @Override
    public List<Packet> getSerialPattern() {
        return new ArrayList<Packet>() {{
            add(Packet.pattern(Pattern.FLOAT2B, Settings.TWOPI));                      //ROTATION
            add(Packet.pattern(Pattern.FLOAT2B, Settings.windowMeters[0]));             // X
            add(Packet.pattern(Pattern.FLOAT2B, Settings.windowMeters[1]));             // Y
            add(Packet.pattern(Pattern.FLOAT1B, Settings.windowMeters[1] / 2f));        // Length
            add(Packet.pattern(Pattern.CHAR2B));                                         // COLOR
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

        /** Polygon to rotate **/
        Polygon polygon = new Polygon(rect.getPoints());
        polygon.transform(Transform.createRotateTransform((Float) data.get(0).data));

        /** Get Color **/
        graphics.setColor(Settings.colorMap.get((Character) data.get(4).data));
        graphics.fill(polygon);
    }
}
