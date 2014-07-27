package shapes;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;

import java.util.ArrayList;
import java.util.List;

import serialize.Packet;
import serialize.Pattern;
import serialize.PongPacket;
import utils.Debugger;
import utils.Registry;
import pongutils.Settings;

public class Paddle extends PongPacket {
    Debugger debbie = new Debugger(Paddle.class.getSimpleName());

    public Paddle() {
    }

    @Override
    public List<Packet> getSerialPattern() {
        return new ArrayList<Packet>() {{
            add(new Packet(Pattern.FLOAT2B, Settings.TWOPI));                      //ROTATION
            add(new Packet(Pattern.FLOAT2B, Settings.windowMeters[0]));            // X
            add(new Packet(Pattern.FLOAT2B, Settings.windowMeters[1]));            // Y
            add(new Packet(Pattern.FLOAT1B, Settings.windowMeters[1]));            // Length
            add(new Packet(Pattern.BYTE));                                          // Spritesheet ID
            add(new Packet(Pattern.BYTE));                                          // Spritesheet frame
        }};
    }

    @Override
    public void extractData(List<Packet> data, Graphics graphics) {
        int x = Settings.m2p((Float) data.get(1).data);
        int y = Settings.m2p((Float) data.get(2).data);
        int width = Settings.m2p(Settings.paddleWidth);
        int length = Settings.m2p((Float) data.get(3).data);

        /** Create a rectangle given position and size **/
        Rectangle rect = new Rectangle(
                x - width / 2,
                y - length / 2,
                width,
                length
        );

        /** Get SpriteSheet **/
        SpriteSheet ss = Registry.getSpriteSheet((Byte) data.get(4).data);
        Image im = ss.getSprite((Byte) data.get(5).data, 0);
        im.setRotation((360.0f / Settings.TWOPI) * ((Float) (data.get(0).data)));
        im.draw(rect.getX(), rect.getY());
        debbie.e(x + " x position, " + y + " y position, " + width + " width, " + length + " length");
    }
}
