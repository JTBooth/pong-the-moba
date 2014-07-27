package serialize;

import java.util.List;

import pongutils.IllegalShapeException;
import serialization.Bytes;
import serialization.Packet;
import utils.Debugger;
import utils.Registry;

/**
 * Created by rbooth on 7/13/14.
 */
public abstract class PongPacket {
    Debugger debbie = new Debugger(PongPacket.class.getSimpleName());

    /**
     * Serialize based on set Pattern
     */
    public byte[] serialize() throws IllegalShapeException {
        if (!shouldSerialize()) {
            return null;
        }

        List<Packet> packets = setSerialData();

        byte[] serialized = new byte[getBytePatternCount(packets)];
        int pointer = 0;

        debbie.d(this.getClass().getSimpleName());

        byte[] id = Bytes.char2Bytes2(Registry.getPacketId(this.getClass()));
        System.arraycopy(id, 0, serialized, pointer, id.length);
        pointer += id.length;

        for (Packet packet : packets) {
            debbie.d(this.getClass().getSimpleName() + " Serializing");

            switch (packet.pattern) {
                case FLOAT2B:
                    byte[] float2B = Bytes.float2Byte2((Float) packet.data, packet.scale);
                    System.arraycopy(float2B, 0, serialized, pointer, float2B.length);
                    pointer += float2B.length;
                    break;

                case FLOAT1B:
                    serialized[pointer++] = Bytes.float2Byte((Float) packet.data, packet.scale);
                    break;

                case CHAR2B:
                    byte[] col = Bytes.char2Bytes2((Character) packet.data);
                    System.arraycopy(col, 0, serialized, pointer, col.length);
                    pointer += col.length;
                    break;

                case BYTE:
                    serialized[pointer++] = (Byte) packet.data;
                    break;
                default:
                    break;
            }
        }

        return serialized;
    }

    /**
     * Serialize?
     */
    public boolean shouldSerialize() {
        return true;
    }

    /**
     * Byte Count *
     */
    private int getBytePatternCount(List<Packet> packets) {
        int byteCount = 0;
        for (Packet packet : packets) {
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
    public abstract List<Packet> setSerialData();

}
