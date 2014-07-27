package serialize;

import com.esotericsoftware.kryo.Kryo;

public class KryoRegisterer {
    public static void register(Kryo kryo) {
        kryo.register(HousewarmingPacket.class);
        kryo.register(CommandUpdate.class);
        kryo.register(byte[].class);
        kryo.register(int[].class);
    }
}
