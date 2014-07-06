package pong;


import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;

import utils.Debugger;

public class Settings {
    /**
     * System Settings *
     */
    public static final int fps = 60;
    public static final int velocityIterations = 10;
    public static final int positionIterations = 10;
    public static final int winningScore = 5; // unused
    public static final int[] relevantChars =
            {
                    Keyboard.KEY_LEFT,
                    Keyboard.KEY_RIGHT,
                    Keyboard.KEY_UP,
                    Keyboard.KEY_DOWN,    // movement
                    Keyboard.KEY_SPACE,   // casts laser
                    Keyboard.KEY_Q,		  // casts restitution boost
                    Keyboard.KEY_0        // resets game with new settings
            };
    public static final int minFramesBeforeReset = 180;
    public static final double PTM_RATIO = 100.0;
    public static final String title = "Pong the Moba";
    public static final float laserRadius = 0.15f;
    /**
     * Display Settings *
     */
    public static final int[] windowSize = new int[]{800, 600};
    public static final float[] scorePositions = new float[]{windowSize[0] / 4 - 40, windowSize[1] / 2 - 40, 3 * windowSize[0] / 4 - 40, windowSize[1] / 2 - 40};
    public static final float[] windowMeters = new float[]{p2m(800), p2m(600)};
    public static final float margin = 10f;
    public static final int manaBarWidth = 25;
    /**
     * Colors *
     */
    public static final HashMap<Character, Color>  colorMap = new HashMap<Character, Color>() {{
    	put(Character.MAX_VALUE, null); // do not render this
        put('0', new Color(255, 0, 0)); //red, p1
        put('1', new Color(0, 255, 0)); //green, ball
        put('2', new Color(0, 0, 255)); //blue, p2
        put('3', new Color(128, 0, 128)); // purple, terrain+lasers
        put('4', new Color(90, 245, 245)); // light blue, mana bars
    }};
    /**
     * Player Settings *
     */
    public static final int PLAYERL = 0;
    public static final int PLAYERR = 1;
    /**
     * Gameplay Settings *
     */
    public static float ballRadius = 0.2f;
    public static float laserDensity = 2000f;
    public static float laserVelocity = 15f;
    public static float serveSpeed = 3f;
    public static float paddleLength = 2f;
    public static float paddleWidth = 0.2f;
    public static float paddleDistance = .5f;
    public static float paddleSpeed = 2f;
    public static float maxPaddleRotateAngle = 0.5f;
    public static float paddleSpringConstant = 2f;
    public static float paddleDampingConstant = .2f;
    public static int ticksPerManaGain = 60;
    public static byte maxMana = 100;
    
    /*
     * Serialization Settings
     */
    public static byte SCALABLE_CIRCLE = 0;
    public static byte SCALABLE_BASIC_PADDLE=1;
    

    public static void refreshSettings() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL url = classLoader.getResource("res/gameplaySettingsSpec");
        Debugger.debugger.d(url.toString());
        File gameplaySettingsSpec = null;
        try {
            gameplaySettingsSpec = new File(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        if (gameplaySettingsSpec == null)
            return;
        try {
            BufferedReader br = new BufferedReader(new FileReader(gameplaySettingsSpec));
            Settings.ballRadius = Float.parseFloat(br.readLine().split(" ")[0]);
            Settings.laserDensity = Float.parseFloat(br.readLine().split(" ")[0]);
            Settings.laserVelocity = Float.parseFloat(br.readLine().split(" ")[0]);
            Settings.serveSpeed = Float.parseFloat(br.readLine().split(" ")[0]);
            Settings.paddleLength = Float.parseFloat(br.readLine().split(" ")[0]);
            Settings.paddleSpeed = Float.parseFloat(br.readLine().split(" ")[0]);
            Settings.maxPaddleRotateAngle = Float.parseFloat(br.readLine().split(" ")[0]);
            Settings.maxMana = Byte.parseByte(br.readLine().split(" ")[0]);
            br.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static int m2p(float meter) {
        return (int) (meter * PTM_RATIO);
    }

    public static float p2m(int pixel) {
        return (float) (pixel / PTM_RATIO);
    }

}
