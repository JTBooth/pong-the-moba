package server;

import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;

import org.jbox2d.common.Settings;

import java.io.IOException;

import packets.KryoRegisterer;

public class PongServer extends Server {
    public static void main (String[] args){
        /** Setup Server Physics Engine Settings **/
        setupEngine();

        /** Setup Logging **/
        Log.set(Log.LEVEL_NONE);

        try {
            PongServer server = new PongServer();
            server.addListener(new ServerListener(server, pong.Settings.relevantChars));
            server.bind(54555, 54777);

            /** Packet Serialization via Kryo **/
            server.registerClasses();

            new Thread(server).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Changes settings in box2d engine *
     */
    private static void setupEngine() {
        Settings.velocityThreshold = 0f;
    }

    private void registerClasses() {
        KryoRegisterer.register(getKryo());
    }

    public void sendUpdate(byte[] renderList) {
        if (renderList.length < 1) {
            System.out.println("renderList is empty");
        }
        sendToAllUDP(renderList);
    }
}