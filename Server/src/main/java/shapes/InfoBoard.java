package shapes;

import java.util.ArrayList;
import java.util.List;

import pong.Player;
import serialization.Packet;
import serialization.Pattern;
import serialize.PongPacket;
import utils.Debugger;
import utils.Settings;

import static serialization.Bytes.uByte;

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
    private ManaBar leftMana = new ManaBar(Settings.maxMana, 0);
    private ManaBar rightMana = new ManaBar(Settings.maxMana, Settings.windowSize[0] - Settings.manaBarWidth);

    /**
     * Players *
     */
    private String p1name = "Player 1";
    private String p2name = "Player 2";

    /**
     * Constructor *
     */
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
    public boolean shouldSerialize() {
        return true;
    }

    @Override
    public List<Packet> setSerialData() {
        debbie.e(leftMana.getCurrentMana() + "");
        return new ArrayList<Packet>() {{
            add(Packet.data(Pattern.BYTE, left));                      //LEFT
            add(Packet.data(Pattern.BYTE, right));                     //RIGHT
            add(Packet.data(Pattern.BYTE, leftMana.getCurrentMana())); //Left Mana
            add(Packet.data(Pattern.BYTE, rightMana.getCurrentMana()));//Right Mana
        }};
    }

    private class ManaBar {
        private byte maxMana;
        private byte currentMana;
        private float currentFraction;
        private int x;

        ManaBar(byte maxMana, int x) {
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
    }
}
