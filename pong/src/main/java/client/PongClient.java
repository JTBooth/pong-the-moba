package client;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.minlog.Log;

import java.io.IOException;

import serialize.HousewarmingPacket;
import serialize.KryoRegisterer;
import utils.Debugger;

public class PongClient extends Client {
    private static Debugger debbie = new Debugger(PongClient.class.getSimpleName());
    /**
     * Display Classes *
     */
    DisplayListener displayListener;
    PongDisplay pongDisplay;

    /**
     * Info Holders *
     */
    long userId;
    int[] relevantChars;

    public PongClient(PongDisplay pongDisplay) {
        /** Kyro Registering **/
        registerClasses();

        /** Initialize **/
        this.pongDisplay = pongDisplay;
        displayListener = new DisplayListener(this);


        /** Start the server **/
        addListener(displayListener);
        Log.set(Log.LEVEL_ERROR);
        new Thread(this).start();

        try {
            //connect(5000, "127.0.0.1", 54555, 54777);
            connect(5000, IP.testIp, 54555, 54777);
            setTimeout(0);
        } catch (IOException e) {
            System.out.println("Server is not started. Cannot connect.");
            e.printStackTrace();
        }
    }

    private void registerClasses() {
        KryoRegisterer.register(getKryo());
    }

    public DisplayListener getDisplayListener() {
        return displayListener;
    }

    public void submit(int[] keysPressed, long gameId) {
        debbie.i("Sending Update: " + userId + " " + gameId);
        CommandUpdate commandUpdate = new CommandUpdate(keysPressed, userId, gameId);
        sendUDP(commandUpdate);
    }

    public void initialize(HousewarmingPacket hwPacket) {
        userId = hwPacket.getUserId();
        relevantChars = hwPacket.getRelevantChars();
    }
}
