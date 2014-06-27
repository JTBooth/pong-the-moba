package client;

import java.util.ArrayList;
import java.util.List;

import packets.DisplayUpdate;
import packets.HousewarmingPacket;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class DisplayListener extends Listener {
	private DisplayUpdate currentUpdate;
	private PongClient pongClient;
	private int[] relevantCharacters;
	private long userId;
	
	public DisplayListener(PongClient pongClient) {
		this.pongClient = pongClient;
		currentUpdate = new DisplayUpdate(new GamePiece[0], 0);
	}
	
	@Override
	public void received(Connection con, Object packet) {
		System.out.println("currentUpdate has " + currentUpdate.getRenderList().length + " pieces in it");
		if (packet instanceof DisplayUpdate) {
			DisplayUpdate displayUpdate = (DisplayUpdate) packet;
			if (displayUpdate.timestamp > currentUpdate.timestamp) {
				currentUpdate = displayUpdate;
			}
			
			
		} else if (packet instanceof HousewarmingPacket) {
			HousewarmingPacket housewarmingPacket = (HousewarmingPacket) packet;
			pongClient.initialize(housewarmingPacket);
			relevantCharacters = housewarmingPacket.getRelevantChars();
			userId = housewarmingPacket.getUserId();
		}
	}
	
	@Override
	public void connected(Connection connection) {
		super.connected(connection);
		connection.setTimeout(0);
	}
	
	public GamePiece[] getRenderList() {
		return currentUpdate.getRenderList();
	}
	
	public int[] getRelevantCharacters() {
		return relevantCharacters;
	}
}
