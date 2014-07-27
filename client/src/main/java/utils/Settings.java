package utils;


import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;

import java.util.HashMap;

public class Settings extends pongutils.Settings {
    /**
     * System Settings *
     */
    public static final int[] relevantChars =
            {
                    Keyboard.KEY_LEFT,
                    Keyboard.KEY_RIGHT,
                    Keyboard.KEY_UP,
                    Keyboard.KEY_DOWN,    // movement
                    Keyboard.KEY_SPACE,   // casts laser
                    Keyboard.KEY_Q,          // casts restitution boost
                    Keyboard.KEY_0        // resets game with new settings
            };
    /**
     *
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
     * Display Settings *
     */
    public static final int manaBarWidth = 25;
    public static final float[] scorePositions = new float[]{windowSize[0] / 4 - 40, windowSize[1] / 2 - 40, 3 * windowSize[0] / 4 - 40, windowSize[1] / 2 - 40};


    /**
     * Gameplay Settings *
     */
    public static float paddleWidth = 0.2f;
    public static byte maxMana = 100;
}
