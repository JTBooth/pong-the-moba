package client;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Shape;

import packets.SerializableColor;

public class GamePiece {
	private Shape shape;
	private SerializableColor color;
	
	public GamePiece() {}
	
	public GamePiece(Shape shape, SerializableColor color) {
		this.shape=shape;
		this.color=color;
	}
	
	public Shape getShape() {
		return shape;
	}
	
	public SerializableColor getColor() {
		return color;
	}
}
