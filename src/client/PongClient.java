package client;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.minlog.Log;

import java.io.IOException;

import packets.HousewarmingPacket;
import packets.KryoRegisterer;

public class PongClient extends Client {
    DisplayListener displayListener;
    PongDisplay pongDisplay;
    long userId;
    int[] relevantChars;

    public PongClient(PongDisplay pongDisplay) {
        registerClasses();
        this.pongDisplay = pongDisplay;
        displayListener = new DisplayListener(this);
        addListener(displayListener);
        Log.set(Log.LEVEL_DEBUG);
        new Thread(this).start();


        try {
//            connect(5000, "127.0.0.1", 54555, 54777);
            connect(5000, "192.168.1.66", 54555, 54777);
            setTimeout(0);
        } catch (IOException e) {
            System.out.println("Server is not started. Cannot connect.");
            e.printStackTrace();
        }
    }

    public DisplayListener getDisplayListener() {
        return displayListener;
    }

    private void registerClasses() {
        KryoRegisterer.register(getKryo());
    }

    public void submit(int[] keysPressed) {
        CommandUpdate commandUpdate = new CommandUpdate(keysPressed, userId);
        sendUDP(commandUpdate);
    }

    public void initialize(HousewarmingPacket hwPacket) {
        userId = hwPacket.getUserId();
        relevantChars = hwPacket.getRelevantChars();
    }

}
