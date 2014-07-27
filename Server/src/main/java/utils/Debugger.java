package utils;

import java.util.Arrays;
import java.util.List;

import serialize.PongPacket;

/**
 * Created by chris on 6/28/14.
 * sihrc @ Github
 */
public class Debugger extends pongutils.Debugger{
    /**
     * FILTERS *
     */

    public final static int global = DEBUG; // | INFO | WARNING | ERROR;

    /**
     * Allowed classes
     * ADD CLASSES FOR LOGGING
     * *
     */
    public final static List<String> enabled = Arrays.asList(
            PongPacket.class.getSimpleName()
    );


    @Override
    public List<String> getEnabled() {
        return enabled;
    }

    @Override
    public int getGlobal() {
        return global;
    }

    /**
     * Constructor *
     *
     * @param tag
     */
    public Debugger(String tag) {
        super(tag);
    }
}
