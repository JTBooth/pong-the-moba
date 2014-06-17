package server;

import java.io.IOException;
import java.util.List;

import packets.Packet;
import packets.Packet1Connect;
import client.GamePiece;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import com.esotericsoftware.minlog.Log.Logger;

public class PongServer {
	private Server server; 
	private Kryo kryo;
	
	public PongServer() throws IOException {
		server = new Server();
		Log.set(Log.LEVEL_DEBUG);

		server.start();
		server.bind(54555, 54777);
		server.addListener(new ServerListener());
		kryo=server.getKryo();
		kryo.register(DisplayUpdate.class);
	}
	
	public void sendUpdate(List<GamePiece> renderList) {
		DisplayUpdate update = new DisplayUpdate(renderList, System.nanoTime());
		if (renderList.size() < 1) {
			System.out.println("renderList is empty");
		}
		server.sendToAllUDP(update);
	}
}
