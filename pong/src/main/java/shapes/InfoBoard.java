package shapes;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import java.util.ArrayList;
import java.util.List;

import serialize.Packet;
import serialize.Pattern;
import serialize.PongPacket;
import server.Player;
import utils.Debugger;
import utils.Settings;

import static serialize.Bytes.uByte;

/**
 * Created by sihrc on 6/28/14.
 */
public class InfoBoard extends PongPacket {
    Debugger debbie = new Debugger(InfoBoard.class.getSimpleName());
    /**
     * SCORES *
     */
    private byte left = 0;
    private byte right = 0;
    private int winningScore;

    /**
     * Mana *
     */
    private manaBar leftMana = new manaBar(Settings.maxMana, 0);
    private manaBar rightMana = new manaBar(Settings.maxMana, Settings.windowSize[0] - Settings.manaBarWidth);

    /**
     * Players *
     */
    private String p1name = "Player 1";
    private String p2name = "Player 2";

    /**
     * Constructor *
     */
    public InfoBoard() {
    }


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

    public void playerScored(int player) {
        if (player == Player.LEFT) {
            left++;
        } else {
            right++;
        }
    }

    public void setMana(byte leftMana, byte rightMana) {
        this.leftMana.setCurrentMana(leftMana);
        this.rightMana.setCurrentMana(rightMana);
    }

    @Override
    public List<Object> setSerialData() {
        return new ArrayList<Object>(){{
            add(left);                      //LEFT
            add(right);                     //RIGHT
            add(leftMana.getCurrentMana()); //Left Mana
            add(rightMana.getCurrentMana());//Right Mana
        }};
    }

    @Override
    public List<Packet> getSerialPattern() {
        return new ArrayList<Packet>(){{
            add(new Packet(Pattern.FLOAT1B)); //LEFT
            add(new Packet(Pattern.FLOAT1B)); //RIGHT
            add(new Packet(Pattern.BYTE)); //Left Mana
            add(new Packet(Pattern.BYTE)); //Right Mana
        }};
    }

    @Override
    public void extractData(List<Packet> data, Graphics graphics) {
        int counter = 0;
        graphics.setColor(Settings.colorMap.get('0'));
        graphics.drawString(String.valueOf(data.get(counter++).data), Settings.scorePositions[0], Settings.scorePositions[1]);

        //Draw right score
        graphics.setColor(Settings.colorMap.get('2'));
        graphics.drawString(String.valueOf(data.get(counter++).data), Settings.scorePositions[2], Settings.scorePositions[3]);

        //Draw left mana
        leftMana.setCurrentMana((Byte)data.get(counter++).data);
        leftMana.render(graphics);

        //Draw right mana
        rightMana.setCurrentMana((Byte)data.get(counter++).data);
        rightMana.render(graphics);
    }

    @Override
    public boolean shouldSerialize() {
        return true;
    }

    private class manaBar {
        private byte maxMana;
        private byte currentMana;
        private float currentFraction;
        private int x;

        manaBar(byte maxMana, int x) {
            this.maxMana = maxMana;
            this.currentMana = 0;
            this.x = x;
        }

        byte getCurrentMana() {
            return currentMana;
        }

        void setCurrentMana(byte currentMana) {
            this.currentMana = currentMana;
            this.currentFraction = ((float) uByte(currentMana)) / (uByte(maxMana));
        }

        void render(Graphics graphics) {
            graphics.setColor(Settings.colorMap.get('4'));
            int height = (int) (currentFraction * Settings.windowSize[1]);
            int y = Settings.windowSize[1] / 2 - height / 2;

            debbie.d("mana bar constructed with x: " + x + " y: " + y + " width: " + Settings.manaBarWidth + " height: " + height + " cf: " + currentFraction);
            Rectangle rect = new Rectangle(x, y, Settings.manaBarWidth, height);
            graphics.fill(rect);
        }
    }
}
