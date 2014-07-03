package utils;

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
    /**
     * STATIC DEBUGGER *
     */
    public static Debugger debugger = new Debugger("CHOSEN ONE DECREES", DEBUG | INFO | WARNING | ERROR) {{
        enable();
    }};
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
    private int filter;
    private boolean isEnabled = true;

    /**
     * Constructor *
     */
    public Debugger(String tag, int filter) {
        this.tag = tag;
        this.filter = filter;
        this.isEnabled = true;
    }

    /**
     * Turning on and Shutting up *
     */
    public void enable() {
        isEnabled = true;
    }

    public void disable() {
        isEnabled = false;
    }

    /**
     * Filter that *
     */
    public void setFilter(int mask) {
        this.filter = mask;
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
        if ((this.filter & filter) > 0 && isEnabled)
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
