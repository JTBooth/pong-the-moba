package server;

import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;

import org.jbox2d.common.Settings;

import java.io.IOException;
import java.util.Arrays;

import packets.KryoRegisterer;
import utils.Debugger;

public class PongServer extends Server {
    private static Debugger debbie = new Debugger(PongServer.class.getSimpleName());

    public static void main(String[] args) {
        /** Setup Server Physics Engine Settings **/
        setupEngine();

        /** Setup Logging **/
        Log.set(Log.LEVEL_NONE);

        try {
            debbie.i("Attaching Server Listener");
            PongServer server = new PongServer();
            server.addListener(new ServerListener(server, utils.Settings.relevantChars));

            debbie.i("Binding to port");
            server.bind(54555, 54777);

            /** Packet Serialization via Kryo **/
            server.registerClasses();

            debbie.i("Listening");
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
        debbie.d("Sending Update " + Arrays.toString(renderList));
        if (renderList.length < 1) {
            System.out.println("renderList is empty");
        }
        sendToAllUDP(renderList);
    }
}