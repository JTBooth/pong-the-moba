package server;

import java.util.Random;

import pong.Pong;
import client.CommandUpdate;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class ServerListener extends Listener{
	private int[] relevantCharacters;
	private Random random;
	private Pong pong;
	
	public ServerListener(Pong pong, int[] relevantCharacters) {
		this.relevantCharacters=relevantCharacters;
		this.random = new Random();
		this.pong = pong;
	}
	
	@Override
	public void connected(Connection connection) {
		super.connected(connection);
		long id = random.nextLong();
		Player newPlayer = new Player(connection, id);
		HousewarmingPacket hp = new HousewarmingPacket(relevantCharacters, id);
		connection.sendTCP(relevantCharacters);
		connection.setTimeout(0);
		pong.addPlayer(newPlayer);
	}
	
	@Override
	public void disconnected(Connection connection) {
		super.disconnected(connection);
	}
	
	@Override
	public void received(Connection connection, Object packet) {
		if (packet instanceof CommandUpdate) {
			CommandUpdate update = (CommandUpdate) packet;
			Player receiver = pong.getPlayer(update.getPlayerId());
			receiver.setKeys(update);
		}
		
	}
}
