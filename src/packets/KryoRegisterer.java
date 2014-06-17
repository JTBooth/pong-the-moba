package packets;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Shape;

import client.GamePiece;

import com.esotericsoftware.kryo.Kryo;

public class KryoRegisterer {
	public void register(Kryo kryo) {
		kryo.register(ArrayList.class);
		kryo.register(GamePiece.class);
		kryo.register(Shape.class);
		kryo.register(Color.class);
	}
}
