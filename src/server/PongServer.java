package server;

import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;

import org.jbox2d.common.Settings;

import java.io.IOException;

import packets.KryoRegisterer;
import pong.Pong;

public class PongServer extends Server {

    public PongServer(Pong pong, int[] relevantCharacters) throws IOException {
        //Setup settings for the physics engine
        setupEngine();
        Log.set(Log.LEVEL_NONE);

        bind(54555, 54777);
        addListener(new ServerListener(pong, relevantCharacters));

        registerClasses();

        new Thread(this).start();

    }

    /**
     * Changes settings in box2d engine *
     */
    private void setupEngine() {
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