package client;

import java.io.IOException;

import packets.KryoRegisterer;
import packets.Packet;
import packets.Packet1Connect;
import server.DisplayUpdate;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.minlog.Log;

public class PongClient {
	Client client;
	Kryo kryo;
	DisplayListener displayListener;

	public PongClient() {
		Client client = new Client();
		kryo = client.getKryo();
		registerClasses();
		displayListener = new DisplayListener();
		client.addListener(displayListener);
		Log.set(Log.LEVEL_DEBUG);
		new Thread(client).start();
		
		
		try {
			client.connect(5000, "127.0.0.1", 54555, 54777);
			client.setTimeout(0);
		} catch (IOException e) {
			System.out.println("Server is not started. Cannot connect.");
			e.printStackTrace();
		}
		

	}
	
	public DisplayListener getDisplayListener() {
		return displayListener;
	}
	
	private void registerClasses() {
		KryoRegisterer.register(kryo);
	}

	
}
