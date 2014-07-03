package packets;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Shape;

import pong.Settings;
import pong.ShapeType;

public class GamePiece {
    public float[] points;
    public char color;
    public ShapeType type;

    public GamePiece() {
    }

    public GamePiece(Shape shape, ShapeType type, char color) {
        this.points = shape.getPoints();
        this.color = color;
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
        return Settings.colorMap.get(color);
    }
}
