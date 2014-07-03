package client;

import org.newdawn.slick.TrueTypeFont;

import pong.Settings;

/**
 * Created by sihrc on 6/28/14.
 */
public class Scoreboard {
    /**
     * Graphics *
     */
    TrueTypeFont font;
    /**
     * SCORES *
     */
    private int left = 0;
    private int right = 0;

    /**
     * Constructor *
     */
    public Scoreboard(TrueTypeFont font) {
        this.font = font;
    }

    /**
     * Update Scores *
     */
    public void update(int[] scores) {
        left = scores[0];
        right = scores[1];
    }

    /**
     * Render Scores *
     */
    public void render() {
        font.drawString(Settings.scorePositions[0], Settings.scorePositions[1], String.valueOf(left), Settings.colorMap.get('0'));
        font.drawString(Settings.scorePositions[2], Settings.scorePositions[3], String.valueOf(right), Settings.colorMap.get('1'));
    }

    /**
     * Get Font for registering *
     */
    public TrueTypeFont getFont() {
        return font;
    }
}
