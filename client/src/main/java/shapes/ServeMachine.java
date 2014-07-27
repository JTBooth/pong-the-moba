package shapes;


import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import java.util.ArrayList;
import java.util.List;

import serialization.Packet;
import serialization.Pattern;
import serialize.PongPacket;
import utils.Debugger;
import utils.Settings;

/**
 * Created by rbooth on 7/26/14.
 */
public class ServeMachine extends PongPacket {
    Debugger debbie = new Debugger(ServeMachine.class.getSimpleName());

    public ServeMachine() {}

    @Override
    public void extractData(List<Packet> data, Graphics graphics) {
        float rotation = (Float) data.get(0).data;
        int xPos = Settings.m2p((Float) data.get(1).data);
        int yPos = Settings.m2p((Float) data.get(2).data);

        float xDiff = (float) Math.cos(rotation)*Settings.serveMachineLineLength;
        float yDiff = (float) Math.sin(rotation)*Settings.serveMachineLineLength;
        System.out.println("serveMachine at " + xPos + ", " + yPos);
        graphics.drawGradientLine(xPos-xDiff, yPos-yDiff, new Color(255,0,0), xPos+xDiff, yPos+yDiff, new Color(0,0,255));

    }

    @Override
    public List<Packet> getSerialPattern() {
        return new ArrayList<Packet>(){{
            add(Packet.pattern(Pattern.FLOAT2B, Settings.TWOPI));
            add(Packet.pattern(Pattern.FLOAT2B, Settings.windowMeters[0]));
            add(Packet.pattern(Pattern.FLOAT2B, Settings.windowMeters[1]));
        }};
    }

}
