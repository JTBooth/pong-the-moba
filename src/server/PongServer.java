package server;

import java.io.IOException;
import java.util.List;

import packets.DisplayUpdate;
import packets.KryoRegisterer;
import pong.Pong;
import client.GamePiece;

import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;

public class PongServer extends Server {
	private Pong pong;
	
	public PongServer(Pong pong, int[] relevantCharacters) throws IOException {
		Log.set(Log.LEVEL_INFO);
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
}
