package server;

import com.esotericsoftware.kryonet.Connection;

import client.CommandUpdate;
import pong.Paddle;

public class Player {
    final public static int LEFT = 0;
    final public static int RIGHT = 1;

    private Connection connection;
    private int who;
    private Paddle paddle;
    private long id;
    private CommandUpdate commands;

    public Player(Connection connection, long id) {
        this.connection = connection;
        this.id = id;
        commands = new CommandUpdate();
    }

    /**
     * GETTERS *
     */
    public Paddle getPaddle() {
        return paddle;
    }

    /**
     * SETTERS *
     */
    public void setPaddle(Paddle paddle) {
        this.paddle = paddle;
    }

    public int[] getKeys() {
        return commands.getKeys();
    }

    public void setKeys(CommandUpdate update) {
        commands = update;
    }

    public int who() {
        return who;
    }

    public void setWho(int who) {
        this.who = who;
    }

    /**
     * IDENTITY *
     */
    public boolean isPlayer(Player otherPlayer) {
        return otherPlayer.getId() == id;
    }

    public long getId() {
        return id;
    }
}
