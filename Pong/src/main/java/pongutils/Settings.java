package pongutils;


public class Settings {
    /**
     * System Settings *
     */
    public static final int fps = 60;
    public static final float timeStep = 1f / ((float) fps);
    public static final double PTM_RATIO = 100.0;
    public static final float TWOPI = 6.28318530718f;

    /**
     * Display Settings *
     */
    public static final int[] windowSize = new int[]{800, 600};
    public static final float[] windowMeters = new float[]{p2m(windowSize[0]), p2m(windowSize[1])};
    public static float paddleWidth = 0.2f;


    public static int m2p(float meter) {
        return (int) (meter * PTM_RATIO);
    }

    public static float p2m(int pixel) {
        return (float) (pixel / PTM_RATIO);
    }

}
