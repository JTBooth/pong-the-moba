package server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import pong.Player;
import pong.Pong;
import serialization.CommandUpdate;
import serialization.HousewarmingPacket;
import utils.Debugger;

public class ServerListener extends Listener {
    private static Debugger debbie = new Debugger(ServerListener.class.getSimpleName());
    private static Map<Integer, Pong> games = new ConcurrentHashMap<Integer, Pong>();
    private PongServer server;
    private volatile Queue<Connection> playerQueue = new LinkedBlockingQueue<Connection>();


    private int[] acceptedKeys;

    public ServerListener(PongServer server, int[] acceptedKeys) {
        this.server = server;
        this.acceptedKeys = acceptedKeys;
        debbie.i("Starting the connection listener");
        new Connect().start();
    }

    @Override
    public void connected(Connection connection) {
        super.connected(connection);
        debbie.i("Connection discovered " + connection.getRemoteAddressUDP().getAddress().toString());
        synchronized (this) {
            playerQueue.add(connection);
        }
    }

    @Override
    public void disconnected(Connection connection) {
        getInstance(connection.getID()).removeConnection(connection.getID());
        //TODO - handle disconnect in Pong game by pausing
        super.disconnected(connection);
    }

    @Override
    public void received(Connection connection, Object packet) {
        if (packet instanceof CommandUpdate) {
            CommandUpdate update = (CommandUpdate) packet;
            getInstance(connection.getID()).received(connection.getID(), update);
        }
    }

    /**
     * Get Game Instance *
     */
    private Pong getInstance(int id) {
        return games.get(id);
    }

    /**
     * Get next connection *
     */
    private Connection getConnection() {
        Connection newConnection;
        while (true) {
            debbie.d("Waiting for connection");
            //Poll for a new connection
            newConnection = playerQueue.poll();
            if (newConnection != null) {
                return newConnection;
            }
            //Sleep for a bit
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendHousewarmingPacket(Connection connection) {
        /** Send Housewarming Packet **/
        HousewarmingPacket hp = new HousewarmingPacket(acceptedKeys);
        connection.sendTCP(hp);
        connection.setTimeout(0);
    }

    private class Connect extends Thread {
        @Override
        public void run() {
            Connection p1;
            Connection p2;

            Player player1;
            Player player2;

            while (true) {
                /** Wait for both players to connect **/
                p1 = getConnection();
                p2 = getConnection();

                debbie.i("Players found!");

                /** Add the players **/
                player1 = new Player(p1.getID());
                player2 = new Player(p2.getID());

                player1.setWho(Player.LEFT);
                player2.setWho(Player.RIGHT);

                debbie.i("Created Players with ids " + p1.getID() + " " + p2.getID());

                /** Add to a pong game **/
                Pong pong = new Pong(player1, player2, server);
                games.put(p1.getID(), pong);
                games.put(p2.getID(), pong);

                new Game(pong).start();

                sendHousewarmingPacket(p1);
                sendHousewarmingPacket(p2);

                debbie.i("Sending housewarming Packets ");
            }
        }
    }

    private class Game extends Thread {
        Pong pong;

        public Game(Pong pong) {
            this.pong = pong;
        }

        @Override
        public void run() {
            pong.start();
        }
    }
}
