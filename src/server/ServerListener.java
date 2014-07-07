package server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

import client.CommandUpdate;
import packets.HousewarmingPacket;
import pong.Pong;
import utils.Debugger;

public class ServerListener extends Listener {
    private static Debugger debbie = new Debugger(ServerListener.class.getSimpleName(), Debugger.INFO | Debugger.WARNING | Debugger.ERROR);

    private PongServer server;
    private static Map<Long, Pong> games = new HashMap<Long, Pong>();
    private static Map<Connection, Long> players = new HashMap<Connection, Long>();
    private volatile Queue<Connection> playerQueue = new LinkedBlockingQueue<Connection>();



    private int[] acceptedKeys;
    private Random random;
    private boolean isOpen = true;

    public ServerListener(PongServer server, int[] acceptedKeys) {
        this.server = server;
        this.acceptedKeys = acceptedKeys;
        this.random = new Random();
        new Connect().start();
    }

    @Override
    public void connected(Connection connection) {
        super.connected(connection);
        synchronized (this){
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
        if (packet instanceof CommandUpdate) {
            CommandUpdate update = (CommandUpdate) packet;
            Player receiver = getInstance(connection).getPlayer(update.getPlayerId());
            Log.info(receiver.getId() + ": " + Arrays.toString(update.getKeys()));
            receiver.setKeys(update);
        }
    }


    /** Get Game Instance **/
    public static Pong getInstance(Connection id){
        return games.get(players.get(id));
    }

    /** Get next connection **/
    private Connection getConnection() {
        Connection newConnection;
        while (true){
            //Poll for a new connection
            newConnection = playerQueue.poll();
            if (newConnection != null){
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

    private void sendHousewarmingPacket(Connection connection, String id){
        /** Send Housewarming Packet **/
        HousewarmingPacket hp = new HousewarmingPacket(acceptedKeys, id);
        connection.sendTCP(hp);
        connection.setTimeout(0);
    }

    private class Connect extends Thread{
        @Override
        public void run() {
            Connection p1;
            Connection p2;

            Player player1;
            Player player2;

            Pong pong;
            long id;
            while (isOpen){
                /** Wait for both players to connect **/
                p1 = getConnection();
                p2 = getConnection();

                /** Add the players **/
                player1 = new Player(p1);
                player2 = new Player(p2);

                sendHousewarmingPacket(p1, player1.getId());
                sendHousewarmingPacket(p2, player2.getId());

                /** Add to a pong game **/
                pong = new Pong("Pong the MOBA", player1, player2, server);
                pong.addPlayer(player1);
                pong.addPlayer(player2);
                id = random.nextLong();
                games.put(id, pong);

                /** Add to game list **/
                players.put(p1, id);
                players.put(p2, id);
            }
        }
    }

}
