package server;

import com.esotericsoftware.kryonet.Connection;

import client.CommandUpdate;
import shapes.Paddle;

public class Player {
    final public static int LEFT = 0;
    final public static int RIGHT = 1;

    private Connection connection;
    private int who;
    private Paddle paddle;
    private String id;
    private CommandUpdate commands;

    public Player(Connection connection) {
        this.connection = connection;
        this.id = connection.getRemoteAddressUDP().getAddress().toString();
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
        return otherPlayer.getId().equals(id);
    }

    public String getId() {
        return id;
    }
}
