package shapes;

import org.jbox2d.collision.shapes.Shape;
import org.newdawn.slick.Graphics;

import java.util.Arrays;

import pong.Settings;
import server.Player;
import utils.Bytes;
import utils.Debugger;

/**
 * Created by sihrc on 6/28/14.
 */
public class InfoBoard extends PongShape{
    /**
     * SCORES *
     */
    private byte left = 0;
    private byte right = 0;
    private int winningScore;

    /** Players **/
    private String p1name = "Player 1";
    private String p2name = "Player 2";

    /**
     * Constructor *
     */
    public InfoBoard(){}


    public InfoBoard(int winningScore) {
        this(winningScore, "PLAYER 1", "PLAYER 2");
    }
    public InfoBoard(int winningScore, String p1name, String p2name) {
        this.winningScore = winningScore;
        this.p1name = p1name;
        this.p2name = p2name;
    }

    public boolean gameOver() {
        return (left > winningScore || right > winningScore) && left != right;
    }

    public String getLeaderName() {
        if (left > right) {
            return p1name;
        } else if (right > left) {
            return p2name;
        } else {
            return "DRAW";
        }
    }

    public void playerScored(int player){
        if (player == Player.LEFT){
            left++;
        } else {
            right++;
        }
    }

    @Override
    public char getId() {
        return ShapeRegistry.INFO_BOARD;
    }

    @Override
    public byte[] serialize() {
        byte[] serialized = new byte[4];
        int pointer = 0;

        //Shape ID
        byte[] id = Bytes.char2Bytes2(getId());
        System.arraycopy(id, 0, serialized,pointer,id.length);
        pointer += id.length;


        //Left Score
        serialized[pointer++] = left;

        //Right Score
        serialized[pointer++] = right;

        Debugger.debugger.i("INFOBOARD Serialized byte array: " + Arrays.toString(serialized));
        return serialized;
    }

    @Override
    public int deserialize(byte[] cereal, int pointer, Graphics graphics) {
        Debugger.debugger.i("INFOBOARD scores: " + String.valueOf((int) cereal[pointer]) + String.valueOf((int) cereal[pointer + 1]));

        //Draw left score
        graphics.setColor(Settings.colorMap.get('0'));
        graphics.drawString(String.valueOf((int) cereal[pointer++]), Settings.scorePositions[0], Settings.scorePositions[1]);

        //Draw right score
        graphics.setColor(Settings.colorMap.get('2'));
        graphics.drawString(String.valueOf((int) cereal[pointer++]),Settings.scorePositions[2], Settings.scorePositions[3]);
        return pointer;
    }

    @Override
    public boolean visible() {
        return true;
    }

    @Override
    public Shape getBoxShape() {
        return null;
    }

    @Override
    public org.newdawn.slick.geom.Shape getSlickShape() {
        return null;
    }
}
