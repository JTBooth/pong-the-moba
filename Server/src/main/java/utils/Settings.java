package utils;


public class Settings extends pongutils.Settings {
    /**
     * System Settings *
     */
    public static final int fps = 60;
    public static final float timeStep = 1f / ((float) fps);
    public static final int velocityIterations = 10;
    public static final int positionIterations = 10;
    public static final int winningScore = 5; // unused
    public static final int REQUIRED_PLAYERS = 2;

    public static final int minFramesBeforeReset = 180;
    public static final double PTM_RATIO = 100.0;
    public static final float laserRadius = 0.15f;
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
     * Display Settings *
     */
    public static final int[] windowSize = new int[]{800, 600};
    public static final float[] windowMeters = new float[]{p2m(windowSize[0]), p2m(windowSize[1])};
    public static final int manaBarWidth = 25;

    /**
     * Gameplay Settings *
     */
    final public static float ballRadius = 0.2f;
    public static float laserDensity = 1000f;
    public static float laserVelocity = 11f;
    public static float serveSpeed = 3f;
    public static float paddleLength = 2f;
    public static float paddleSpeed = 4f;
    public static float maxPaddleRotateAngle = 0.4f;
    public static float paddleSpringConstant = 2f;
    public static float paddleDampingConstant = .2f;
    public static int ticksPerManaGain = 60;
    public static byte maxMana = 20;
    public static float serveMachineSpeed = 0.05f;
    public static float jetpackForce = 0.1f*serveMachineSpeed;
    public static float serveMachineMaxDist = 0.4f/serveMachineSpeed;
    public static float serveMachineStretch = 3.5f;
    public static float serveMachineMaxRotation = 0.4f;


//    public static void refreshSettings() {
//        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
//        URL url = classLoader.getResource("src/main/res/gameplaySettingsSpec");
//        File gameplaySettingsSpec = null;
//        try {
//            gameplaySettingsSpec = new File(url.toURI());
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//        if (gameplaySettingsSpec == null)
//            return;
//        try {
//            BufferedReader br = new BufferedReader(new FileReader(gameplaySettingsSpec));
//            Settings.ballRadius = Float.parseFloat(br.readLine().split(" ")[0]);
//            Settings.laserDensity = Float.parseFloat(br.readLine().split(" ")[0]);
//            Settings.laserVelocity = Float.parseFloat(br.readLine().split(" ")[0]);
//            Settings.serveSpeed = Float.parseFloat(br.readLine().split(" ")[0]);
//            Settings.paddleLength = Float.parseFloat(br.readLine().split(" ")[0]);
//            Settings.paddleSpeed = Float.parseFloat(br.readLine().split(" ")[0]);
//            Settings.maxPaddleRotateAngle = Float.parseFloat(br.readLine().split(" ")[0]);
//            Settings.maxMana = Byte.parseByte(br.readLine().split(" ")[0]);
//            br.close();
//        } catch (FileNotFoundException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (NumberFormatException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }

    public static int m2p(float meter) {
        return (int) (meter * PTM_RATIO);
    }

    public static float p2m(int pixel) {
        return (float) (pixel / PTM_RATIO);
    }

}
