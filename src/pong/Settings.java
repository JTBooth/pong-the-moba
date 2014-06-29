package pong;

import org.newdawn.slick.Color;

import java.util.HashMap;

import packets.SerializableColor;

public class Settings {
    /** System Settings **/
	public static final int fps = 60;
	public static final int velocityIterations = 10;
	public static final int positionIterations = 10;
	public static final int winningScore = 5;

	/** Gameplay Settings **/
	public static final float ballRadius = 0.5f;
	public static final float laserRadius = 0.25f * ballRadius;
	public static final float laserVelocity = 5f;
	public static final float serveSpeed = 4f;
	public static final float paddleLength = 1f;
	public static final float paddleSpeed = 3f;
    public static final float maxPaddleRotateAngle = 0.8f;


    /** Display Settings **/
    public static final int[] windowSize = new int[] {800,600};
    public static final float margin = 10f;
    public static final SerializableColor paddleColor = new SerializableColor(255, 0, 0);
    public static final SerializableColor ballColor = new SerializableColor(0, 255, 0);
    public static final float[] scorePositions = new float[] {windowSize[0]/4-40,windowSize[1]/2-40,3*windowSize[0]/4-40, windowSize[1]/2-40};
	
    /** Colors **/
    public static final HashMap<Character, Color> colorMap = new HashMap<Character, Color>()
    		{{
    			put('0', new Color(255,0,0)); //red, p1
    			put('1', new Color(0,255,0)); //green, ball
    			put('2', new Color(0,0,255)); //blue, p2
    			put('3', new Color(128, 0, 128)); // purple, terrain
    		}};

    /** Player Settings **/
    public static final int PLAYERL = 0;
    public static final int PLAYERR = 1;
}
