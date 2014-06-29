package server;

import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;

import org.jbox2d.common.Settings;

import java.io.IOException;

import client.GamePiece;
import packets.DisplayUpdate;
import packets.KryoRegisterer;
import pong.Pong;

public class PongServer extends Server {
    private Pong pong;

    public PongServer(Pong pong, int[] relevantCharacters) throws IOException {
        //Setup settings for the physics engine
        setupEngine();
        Log.set(Log.LEVEL_DEBUG);
        this.pong = pong;

        bind(54555, 54777);
        addListener(new ServerListener(pong, relevantCharacters));

        registerClasses();

        new Thread(this).start();

    }

    public void sendUpdate(GamePiece[] renderList) {
        DisplayUpdate update = new DisplayUpdate(renderList, System.nanoTime());
        if (renderList.length < 1) {
            System.out.println("renderList is empty");
        }
        sendToAllUDP(update);
    }

    private void registerClasses() {
        KryoRegisterer.register(getKryo());
    }

    /** Changes settings in box2d engine **/
    private void setupEngine(){
        Settings.velocityThreshold = 0f;
    }
}