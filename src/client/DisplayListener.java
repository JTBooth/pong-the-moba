package client;

import java.util.ArrayList;
import java.util.List;

import server.DisplayUpdate;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class DisplayListener extends Listener {
	private DisplayUpdate currentUpdate;
	
	public DisplayListener() {
		currentUpdate = new DisplayUpdate(new ArrayList<GamePiece>(), 0);
	}
	
	@Override
	public void received(Connection con, Object packet) {
		System.out.println("package received");
		System.out.println("currentUpdate has " + currentUpdate.getRenderList().size() + " pieces in it");
		if (packet instanceof DisplayUpdate) {
			DisplayUpdate displayUpdate = (DisplayUpdate) packet;
			if (displayUpdate.timestamp > currentUpdate.timestamp) {
				currentUpdate = displayUpdate;
			}
			
			
		}
	}
	
	@Override
	public void connected(Connection connection) {
		super.connected(connection);
		connection.setTimeout(0);
	}
	
	public List<GamePiece> getRenderList() {
		return currentUpdate.getRenderList();
	}
}
