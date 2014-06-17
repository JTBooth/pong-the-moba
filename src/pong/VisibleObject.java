package pong;

import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.dynamics.Body;
import org.newdawn.slick.Graphics;

public interface VisibleObject {
	public Body getBody();
	
}
