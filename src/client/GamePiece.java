package client;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Shape;

public class GamePiece {
	private Shape shape;
	private Color color;
	
	public GamePiece(Shape shape, Color color) {
		this.shape=shape;
		this.color=color;
	}
	
	public Shape getShape() {
		return shape;
	}
	
	public Color getColor() {
		return color;
	}
}
