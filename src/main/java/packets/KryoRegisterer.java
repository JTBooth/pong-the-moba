package packets;

import com.esotericsoftware.kryo.Kryo;

import client.CommandUpdate;

public class KryoRegisterer {
    public static void register(Kryo kryo) {
        kryo.register(HousewarmingPacket.class);
        kryo.register(CommandUpdate.class);
        kryo.register(byte[].class);
        kryo.register(int[].class);
    }
}
