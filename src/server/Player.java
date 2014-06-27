package server;

import client.CommandUpdate;
import com.esotericsoftware.kryonet.Connection;
import pong.SolidRect;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Player {
    private Connection connection;
    private SolidRect paddle;
    private long id;
    private List<CommandUpdate> keys;

    public Player(Connection connection, long id) {
        this.connection = connection;
        this.id = id;
        keys = new LinkedList<CommandUpdate>();
    }

    public SolidRect getPaddle() {
        return paddle;
    }

    public void setPaddle(SolidRect paddle) {
        this.paddle = paddle;
    }

    public int[] getKeys() {
        if (keys.size() > 0) {
            return keys.remove(0).getKeys();
        } else {
            return new int[0];
        }

    }

    public void setKeys(CommandUpdate update) {
        keys.add(update);
        Collections.sort(keys);
    }

    public long getId() {
        return id;
    }
}
