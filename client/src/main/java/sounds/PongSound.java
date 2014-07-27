package sounds;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

import java.util.ArrayList;
import java.util.List;

import serialization.Packet;
import serialization.Pattern;
import serialize.PongPacket;
import utils.Registry;

/**
 * Created by rbooth on 7/10/14.
 */
public abstract class PongSound extends PongPacket {
    Sound sound;

    @Override
    public List<Packet> getSerialPattern() {
        return new ArrayList<Packet>() {{
            add(new Packet(Pattern.FLOAT1B, 1f));
        }};
    }

    @Override
    public void extractData(List<Packet> data, Graphics graphics) {
        sound.play(1f, (Float) data.get(0).data);
    }

    @Override
    public void setup() {
        try {
            sound = new Sound(Registry.SOUND_PATH + getAudioPath());
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    public abstract String getAudioPath();

}
