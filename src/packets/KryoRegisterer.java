package packets;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Shape;

import server.DisplayUpdate;
import server.HousewarmingPacket;
import client.CommandUpdate;
import client.GamePiece;

import com.esotericsoftware.kryo.Kryo;

public class KryoRegisterer {
	public static void register(Kryo kryo) {
		kryo.register(HousewarmingPacket.class);
		kryo.register(SerializableCircle.class);
		kryo.register(SerializableColor.class);
		kryo.register(DisplayUpdate.class);
		kryo.register(CommandUpdate.class);
		kryo.register(ArrayList.class);
		kryo.register(GamePiece.class);
		kryo.register(Polygon.class);
		kryo.register(float[].class);
		// no, it wasn't an accident
		kryo.register(Shape.class);
		kryo.register(Color.class);
		kryo.register(int[].class);
	}
}
