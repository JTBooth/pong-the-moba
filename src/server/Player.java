package server;

import client.CommandUpdate;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.minlog.Log;

import pong.Settings;
import pong.SolidRect;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Player {
    private Connection connection;
    private SolidRect paddle;
    private long id;
    private CommandUpdate commands;

    public Player(Connection connection, long id) {
        this.connection = connection;
        this.id = id;
        commands = new CommandUpdate();
    }

    public SolidRect getPaddle() {
        return paddle;
    }

    public void setPaddle(SolidRect paddle) {
        this.paddle = paddle;
    }

    public int[] getKeys() {
        return commands.getKeys();
    }

    public void setKeys(CommandUpdate update) {
        commands=update;
    }

    public long getId() {
        return id;
    }
}
