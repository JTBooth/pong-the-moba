package utils;


import org.newdawn.slick.Color;

import java.util.HashMap;

public class Settings {
    /**
     * System Settings *
     */
    public static final int fps = 60;
    public static final double PTM_RATIO = 100.0;
    public static final float TWOPI = 6.28318530718f;

    /**
     * Display Settings *
     */
    public static final int[] windowSize = new int[]{800, 600};
    public static final float[] scorePositions = new float[]{windowSize[0] / 4 - 40, windowSize[1] / 2 - 40, 3 * windowSize[0] / 4 - 40, windowSize[1] / 2 - 40};
    public static final float[] windowMeters = new float[]{p2m(windowSize[0]), p2m(windowSize[1])};
    public static final int manaBarWidth = 25;
    /**
     * Colors *
     */
    public static final HashMap<Character, Color> colorMap = new HashMap<Character, Color>() {{
        put(Character.MAX_VALUE, null); // do not render this
        put('0', new Color(255, 0, 0)); //red, p1
        put('1', new Color(0, 255, 0)); //green, ball
        put('2', new Color(0, 0, 255)); //blue, p2
        put('3', new Color(128, 0, 128)); // purple, terrain+lasers
        put('4', new Color(90, 245, 245)); // light blue, mana bars
    }};

    /**
     * Gameplay Settings *
     */
    public static float paddleWidth = 0.2f;
    public static int ticksPerManaGain = 60;
    public static byte maxMana = 100;



    public static int m2p(float meter) {
        return (int) (meter * PTM_RATIO);
    }

    public static float p2m(int pixel) {
        return (float) (pixel / PTM_RATIO);
    }

}
