package client;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Shape;
import pong.ShapeType;

public class GamePiece {
    private float[] points;
    private int[] color;
    private ShapeType type;

    public GamePiece() {
    }

    public GamePiece(Shape shape, ShapeType type, Color color2) {
        this.points = shape.getPoints();
        this.color = new int[]{color2.getRed(), color2.getGreen(), color2.getBlue()};
        this.type = type;
    }

    public Shape getShape() {
        if (type == ShapeType.CIRCLE) {
            return new Circle(points[0], points[1], 20);
        } else {
            return new Polygon(points);
        }
    }

    public Color getColor() {
        return new Color(color[0], color[1], color[2]);
    }
}
