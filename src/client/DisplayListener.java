package client;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import packets.HousewarmingPacket;

public class DisplayListener extends Listener {
	private Object[] currentUpdate;
	private PongClient pongClient;
	private int[] relevantCharacters;
	private long userId;

	public DisplayListener(PongClient pongClient) {
		this.pongClient = pongClient;

		currentUpdate = new Object[0]; // Game Piece Array, scores, timestamp

	}

	@Override
	public void connected(Connection connection) {
		super.connected(connection);
		connection.setTimeout(0);
	}

	@Override
	public void received(Connection con, Object packet) {
		// System.out.println("currentUpdate has " +
		// currentUpdate.getRenderList().length + " pieces in it");
		if (packet instanceof Object[]) {
			Object[] displayUpdate = (Object[]) packet;
			currentUpdate = displayUpdate;

		} else if (packet instanceof HousewarmingPacket) {
			HousewarmingPacket housewarmingPacket = (HousewarmingPacket) packet;
			pongClient.initialize(housewarmingPacket);
			relevantCharacters = housewarmingPacket.getRelevantChars();
			userId = housewarmingPacket.getUserId();
		}
	}

	/**
	 * Getters *
	 */

	public Object[] getRenderList() {
		return currentUpdate;
	}

	public int[] getRelevantCharacters() {
		return relevantCharacters;
	}
}