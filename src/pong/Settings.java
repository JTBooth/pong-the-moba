package pong;

import packets.SerializableColor;

public class Settings {
    /** System Settings **/
	public static final int fps = 60;
	public static final int velocityIterations = 10;
	public static final int positionIterations = 10;
	public static final int winningScore = 5;

	/** Gameplay Settings **/
	public static final float ballRadius = 0.2f;
	public static final float serveSpeed = 2f;
	public static final float paddleLength = 2f;
	public static final float paddleSpeed = 2f;
    public static final float maxPaddleRotateAngle = 0.5f;


    /** Display Settings **/
    public static final int[] windowSize = new int[] {800,600};
    public static final float margin = 10f;
    public static final SerializableColor paddleColor = new SerializableColor(255, 0, 0);
    public static final SerializableColor ballColor = new SerializableColor(0, 255, 0);
    public static final float[] scorePositions = new float[] {margin,margin,windowSize[1] - margin - 10, margin};
	

}
