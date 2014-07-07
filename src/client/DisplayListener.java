package client;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import packets.HousewarmingPacket;

public class DisplayListener extends Listener {
	private byte[] currentUpdate;
	private PongClient pongClient;
	private int[] relevantCharacters;
	private String userId;

	public DisplayListener(PongClient pongClient) {
		this.pongClient = pongClient;

		currentUpdate = new byte[0]; // Game Piece Array, scores, timestamp

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
		if (packet instanceof byte[]) {
            currentUpdate = (byte[]) packet;
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

	public byte[] getRenderList() {
		return currentUpdate;
	}

	public int[] getRelevantCharacters() {
		return relevantCharacters;
	}
}