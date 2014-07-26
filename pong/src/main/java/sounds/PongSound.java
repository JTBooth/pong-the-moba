package sounds;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

import java.util.ArrayList;
import java.util.List;

import serialize.Packet;
import serialize.Pattern;
import serialize.PongPacket;
import utils.Debugger;
import utils.Registry;

/**
 * Created by rbooth on 7/10/14.
 */
public abstract class PongSound extends PongPacket{
    Debugger debbie = new Debugger(PongSound.class.getSimpleName());
    Sound sound;

    public abstract String getAudioPath();
    public abstract float getVolume();

    @Override
    public List<Object> setSerialData() {
        return new ArrayList<Object>(){{
            add(getVolume());
        }};
    }

    @Override
    public List<Packet> getSerialPattern() {
        return new ArrayList<Packet>(){{
            add(new Packet(Pattern.FLOAT1B, 1f));
        }};
    }

    @Override
    public void setup() {
        try {
            sound = new Sound(Registry.SOUND_PATH + getAudioPath());
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void extractData(List<Packet> data, Graphics graphics) {
        debbie.i("Playing " + this.getClass().getSimpleName() + " at " + getVolume());
        sound.play(1f, (Float) data.get(0).data);
    }

}
