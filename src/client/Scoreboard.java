package client;

import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

import pong.Settings;

/**
 * Created by sihrc on 6/28/14.
 */
public class Scoreboard {
    /** SCORES **/
    private int left = 0;
    private int right = 0;

    /** Graphics **/
    TrueTypeFont font;

    /** Constructor **/
    public Scoreboard(TrueTypeFont font){
        this.font = font;
    }

    /** Update Scores **/
    public void update(int[] scores){
        left = scores[0];
        right = scores[1];
    }

    /** Render Scores **/
    public void render(){
        font.drawString (Settings.scorePositions[0], Settings.scorePositions[1], String.valueOf(left), Color.lightGray);
        font.drawString (Settings.scorePositions[2], Settings.scorePositions[3], String.valueOf(right), Color.lightGray);
    }

    /** Get Font for registering **/
    public TrueTypeFont getFont(){
        return font;
    }
}
