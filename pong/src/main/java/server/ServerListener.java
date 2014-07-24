package server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import client.CommandUpdate;
import serialize.HousewarmingPacket;
import pong.Pong;
import utils.Debugger;

public class ServerListener extends Listener {
    private static Debugger debbie = new Debugger(ServerListener.class.getSimpleName());
    private static Map<Long, Pong> games = new ConcurrentHashMap<Long, Pong>();
    private static Map<Integer, Long> players = new HashMap<Integer, Long>();
    private PongServer server;
    private volatile Queue<Connection> playerQueue = new LinkedBlockingQueue<Connection>();


    private int[] acceptedKeys;
    private Random random;
    private boolean isOpen = true;

    public ServerListener(PongServer server, int[] acceptedKeys) {
        this.server = server;
        this.acceptedKeys = acceptedKeys;
        this.random = new Random();
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
        //TODO - handle disconnect in Pong game by pausing
        super.disconnected(connection);
    }

    @Override
    public void received(Connection connection, Object packet) {
        debbie.d("Received packet from connection " + connection.getRemoteAddressUDP().getAddress().toString());
        if (packet instanceof CommandUpdate) {
            CommandUpdate update = (CommandUpdate) packet;
            debbie.i("Package: " + update.getGameId());
            debbie.i("Current Games" + games.toString());
            Player receiver = getInstance(update.getGameId()).getPlayer(update.getPlayerId());
            Log.info(receiver.getId() + ": " + Arrays.toString(update.getKeys()));
            receiver.setKeys(update);
        }
    }


    /**
     * Get Game Instance *
     */
    private Pong getInstance(long id) {
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

    private void sendHousewarmingPacket(Connection connection, long playerId, long gameId) {
        /** Send Housewarming Packet **/
        HousewarmingPacket hp = new HousewarmingPacket(acceptedKeys, playerId, gameId);
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

            long gameId;
            long id1;
            long id2;
            while (isOpen) {
                /** Generate new Ids **/
                gameId = random.nextLong();
                id1 = random.nextLong();
                id2 = random.nextLong();

                /** Wait for both players to connect **/
                p1 = getConnection();
                p2 = getConnection();

                debbie.i("Players found!");

                /** Add the players **/
                player1 = new Player(p1, id1);
                player2 = new Player(p2, id2);

                player1.setWho(Player.LEFT);
                player2.setWho(Player.RIGHT);

                debbie.i("Created Players with ids " + id1 + " " + id2);

                /** Add to a pong game **/
                Pong pong = new Pong("Pong the MOBA", player1, player2, gameId, server);
                pong.addPlayer(player1);
                pong.addPlayer(player2);
                games.put(gameId, pong);

                new Game(pong).start();

                debbie.i("Created game with id " + gameId);

                sendHousewarmingPacket(p1, id1, gameId);
                sendHousewarmingPacket(p2, id2, gameId);

                /** Add to game list **/
                players.put(p1.getID(), gameId);
                players.put(p2.getID(), gameId);

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
