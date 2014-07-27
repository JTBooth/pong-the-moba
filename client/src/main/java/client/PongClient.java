package client;

import com.esotericsoftware.kryonet.Client;

import java.io.IOException;

import utils.Debugger;
import serialization.CommandUpdate;
import serialization.HousewarmingPacket;
import serialization.KryoRegisterer;

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
    int[] relevantChars;

    public PongClient(PongDisplay pongDisplay) {
        /** Kyro Registering **/
        registerClasses();

        /** Initialize **/
        this.pongDisplay = pongDisplay;
        displayListener = new DisplayListener(this);

        /** Start the server **/
        addListener(displayListener);
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

    public void submit(int[] keysPressed) {
        CommandUpdate commandUpdate = new CommandUpdate(keysPressed);
        sendUDP(commandUpdate);
    }

    public void initialize(HousewarmingPacket hwPacket) {
        relevantChars = hwPacket.getRelevantChars();
    }
}
