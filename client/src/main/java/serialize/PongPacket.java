package serialize;

import org.newdawn.slick.Graphics;

import java.util.Arrays;
import java.util.List;

import utils.Debugger;

/**
 * Created by rbooth on 7/13/14.
 */
public abstract class PongPacket {
    List<Packet> serialPattern;
    Debugger debbie = new Debugger(PongPacket.class.getSimpleName());

    /**
     * Deserialize based on Pattern *
     */
    public int deserialize(byte[] cereal, int pointer, Graphics graphics) {
        if (serialPattern == null) {
            serialPattern = getSerialPattern();
        }

        for (Packet packet : serialPattern) {
            switch (packet.pattern) {
                case FLOAT2B:
                    packet.data = Bytes.twoByte2Float(Arrays.copyOfRange(cereal, pointer, pointer += 2), packet.scale);
                    break;

                case CHAR2B:
                    packet.data = Bytes.twoBytes2Char(Arrays.copyOfRange(cereal, pointer, pointer += 2));
                    break;

                case FLOAT1B:
                    packet.data = Bytes.byte2Float(cereal[pointer++], packet.scale);
                    break;

                case BYTE:
                    packet.data = cereal[pointer++];
                    break;

                default:
                    break;
            }
        }

        extractData(serialPattern, graphics);
        return pointer;
    }

    /**
     * For Deserializing *
     */
    public abstract List<Packet> getSerialPattern();

    /**
     * For Extracting and Doing stuff with the Data *
     */
    public abstract void extractData(List<Packet> data, Graphics graphics);

    /**
     * Client side setup
     */
    public void setup() {
    }
}
