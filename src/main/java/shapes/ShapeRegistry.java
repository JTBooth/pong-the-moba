package shapes;

import utils.Debugger;

/**
 * Created by sihrc on 7/5/14.
 */
public class ShapeRegistry {
    static Debugger debbie = new Debugger(ShapeRegistry.class.getSimpleName(), Debugger.DEBUG);

    final static public char BALL = '1';
    final static public char LASER = '2';
    final static public char PADDLE = '3';
    final static public char WALL = '4';
    final static public char INFO_BOARD = '5';

    public static PongShape get(char key) {
        switch (key){
            case BALL:
                return new Ball();
            case LASER:
                return new Laser();
            case PADDLE:
                return new Paddle();
            case WALL:
                return new Wall();
            case INFO_BOARD:
                return new InfoBoard();
            default:
                debbie.e("ShapeRegistry received null Id");
                return null;
        }
    }
}
