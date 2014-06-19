package server;

import java.io.IOException;
import java.util.List;

import packets.KryoRegisterer;
import packets.Packet;
import pong.Pong;
import client.GamePiece;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import com.esotericsoftware.minlog.Log.Logger;

public class PongServer extends Server {
	private Pong pong;
	
	public PongServer(Pong pong, int[] relevantCharacters) throws IOException {
		Log.set(Log.LEVEL_DEBUG);
		this.pong = pong;
		
		bind(54555, 54777);
		addListener(new ServerListener(pong, relevantCharacters));
		
		registerClasses();

		new Thread(this).start();
		
	}
	
	public void sendUpdate(List<GamePiece> renderList) {
		DisplayUpdate update = new DisplayUpdate(renderList, System.nanoTime());
		if (renderList.size() < 1) {
			System.out.println("renderList is empty");
		}
		sendToAllUDP(update);
	}
	
	private void registerClasses() {
		KryoRegisterer.register(getKryo());
	}
}
