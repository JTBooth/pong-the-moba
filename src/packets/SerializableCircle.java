package packets;

import org.newdawn.slick.geom.Circle;

public class SerializableCircle extends Circle {

	public SerializableCircle(float centerPointX, float centerPointY,
			float radius) {
		super(centerPointX, centerPointY, radius);
	}

	public SerializableCircle() {
		super(0,0,0);
	}

}
