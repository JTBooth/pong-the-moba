package sounds;

import java.util.ArrayList;
import java.util.List;

import serialization.Packet;
import serialization.Pattern;
import serialize.PongPacket;
import utils.Debugger;

/**
 * Created by rbooth on 7/10/14.
 */
public abstract class PongSound extends PongPacket {
    Debugger debbie = new Debugger(PongSound.class.getSimpleName());

    @Override
    public List<Packet> setSerialData() {
        return new ArrayList<Packet>() {{
            add(Packet.data(Pattern.FLOAT1B, getVolume(), 1f));
        }};
    }

    public abstract float getVolume();


}
