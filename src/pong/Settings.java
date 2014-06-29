package pong;

public class Settings {
    /** System Settings **/
	public static final int fps = 60;

    /** Display Settings **/
    public static final int[] windowSize = new int[] {800,600};
    public static final float margin = 10f;
    public static final float[] scorePositions = new float[] {margin,margin,windowSize[1] - margin - 10, margin};
}
