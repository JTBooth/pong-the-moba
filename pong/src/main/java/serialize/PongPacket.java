package serialize;

import org.jbox2d.common.MathUtils;
import org.newdawn.slick.Graphics;

import java.util.Arrays;
import java.util.List;

import utils.Registry;
import utils.Debugger;
import utils.IllegalShapeException;

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
     * Serialize based on set Pattern
     */
    public byte[] serialize() throws IllegalShapeException {
        if (!shouldSerialize()) {
            return null;
        }
        if (serialPattern == null) {
            serialPattern = getSerialPattern();
        }
        byte[] serialized = new byte[getBytePatternCount()];
        int pointer = 0;

        byte[] id = Bytes.char2Bytes2(Registry.getPacketId(this.getClass()));
        System.arraycopy(id, 0, serialized, pointer, id.length);
        pointer += id.length;

        for (Packet packet : setData()) {
            switch (packet.pattern) {
                case FLOAT2B:
                    byte[] float2B = Bytes.float2Byte2((Float) packet.data, packet.scale);
                    System.arraycopy(float2B, 0, serialized, pointer, float2B.length);
                    pointer += float2B.length;
                    break;

                case FLOAT1B:
                    serialized[pointer++] = Bytes.float2Byte((Float) packet.data, MathUtils.TWOPI);
                    break;

                case CHAR2B:
                    byte[] col = Bytes.char2Bytes2((Character) packet.data);
                    System.arraycopy(col, 0, serialized, pointer, col.length);
                    pointer += col.length;
                    break;

                case BYTE:
                    serialized[pointer++] = (Byte) packet.data;

                default:
                    break;
            }
        }

        debbie.i("PADDLE Serialized byte array: " + Arrays.toString(serialized));
        return serialized;
    }

    /**
     * Byte Count *
     */
    private int getBytePatternCount() {
        int byteCount = 0;
        for (Packet packet : serialPattern) {
            switch (packet.pattern) {
                case BYTE:
                case FLOAT1B:
                    byteCount++;
                    break;

                case FLOAT2B:
                case CHAR2B:
                    byteCount += 2;
                    break;

                default:
                    break;
            }
        }
        return byteCount + 2; //2 for ID
    }


    /**
     * For Serializing *
     */
    public abstract List<Object> setSerialData();
    public List<Packet> setData() {
        List<Packet> packets = getSerialPattern();
        List<Object> data = setSerialData();

        for (int i = 0; i < packets.size(); i ++) {
            packets.get(i).data = data.get(i);
        }

        return packets;
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
     * Serialize?
     */
    public boolean shouldSerialize() {
        return true;
    }
}
