package utils;

import java.util.Arrays;
import java.util.List;

import shapes.Ball;

/**
 * Created by chris on 6/28/14.
 * sihrc @ Github
 */
public class Debugger {
    /**
     * FILTERS *
     */
    public final static int DEBUG = 1;
    public final static int INFO = 1 << 1;
    public final static int WARNING = 1 << 2;
    public final static int ERROR = 1 << 3;

    public final static int global = ERROR; // | INFO | WARNING | ERROR;

    /**
     * Allowed classes
     * ADD CLASSES FOR LOGGING
     * *
     */
    public final static List<String> enabled = Arrays.asList(
//            Pong.class.getSimpleName()
//            PongShape.class.getSimpleName()
//            Paddle.class.getSimpleName()
            Ball.class.getSimpleName()
//            PongPacket.class.getSimpleName()
//            Serializer.class.getSimpleName(),
   );

    /**
     * TAG INDICATORS *
     */
    private final static String DEBUG_TAG = ":\tDEBUG\t:";
    private final static String INFO_TAG = ":\tINFO\t:";
    private final static String WARNING_TAG = ":\tWARN\t:";
    private final static String ERROR_TAG = ":\tERROR\t:";

    /**
     * Class Specific Debugger *
     */
    private String tag;

    /**
     * Constructor *
     */
    public Debugger(String tag) {
        this.tag = tag;
    }

    /**
     * Logging Methods *
     */
    public void d(String message) { //Debug
        log(message, DEBUG_TAG, DEBUG);
    }

    /**
     * General Log *
     */
    private void log(String message, String type, int filter) {
        if ((filter & global) > 0 && enabled.contains(tag))
            System.out.println(tag + type + message);
    }

    public void i(String message) { //Info
        log(message, INFO_TAG, INFO);
    }

    public void w(String message) { //Warn
        log(message, WARNING_TAG, WARNING);
    }

    public void e(String message) { //Error
        log(message, ERROR_TAG, ERROR);
    }

}
