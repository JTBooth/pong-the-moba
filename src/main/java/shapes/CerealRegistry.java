package shapes;

import packets.Cereal;
import utils.Debugger;

/**
 * Created by sihrc on 7/5/14.
 */
public class CerealRegistry {
    static Debugger debbie = new Debugger(CerealRegistry.class.getSimpleName());

    final static public char BALL = '1';
    final static public char LASER = '2';
    final static public char PADDLE = '3';
    final static public char WALL = '4';
    final static public char INFO_BOARD = '5';

    public static Cereal get(char key) {
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
                debbie.e("CerealRegistry received null Id");
                return null;
        }
    }
}
