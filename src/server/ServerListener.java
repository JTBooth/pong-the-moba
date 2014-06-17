package server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class ServerListener extends Listener{
	@Override
	public void connected(Connection connection) {
		super.connected(connection);
		connection.setTimeout(0);
	}
	
	@Override
	public void disconnected(Connection arg0) {
		super.disconnected(arg0);
	}
	
	@Override
	public void received(Connection arg0, Object arg1) {
		System.out.println("received");
	}
}
