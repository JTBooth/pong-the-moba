package server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class ServerListener extends Listener{
	private int[] relevantCharacters;
	
	public ServerListener(int[] relevantCharacters) {
		this.relevantCharacters=relevantCharacters;
	}
	
	@Override
	public void connected(Connection connection) {
		super.connected(connection);
		connection.sendTCP(relevantCharacters);
		connection.setTimeout(0);
	}
	
	@Override
	public void disconnected(Connection connection) {
		super.disconnected(connection);
	}
	
	@Override
	public void received(Connection connection, Object packet) {
		System.out.println("received");
	}
}
