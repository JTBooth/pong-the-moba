package client;

import java.io.IOException;

import packets.KryoRegisterer;
import packets.Packet;
import server.DisplayUpdate;
import server.HousewarmingPacket;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.minlog.Log;

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
			connect(5000, "127.0.0.1", 54555, 54777);
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
