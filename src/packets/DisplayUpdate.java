package packets;

import java.util.List;

import client.GamePiece;

public class DisplayUpdate {
	public long timestamp;
	private GamePiece[] pieceArray;
	public DisplayUpdate() {}
	
	public DisplayUpdate(GamePiece[] renderList, long timestamp) {
		this.timestamp = timestamp;
		this.pieceArray = renderList;
	}
	
	public GamePiece[] getRenderList() {
		return pieceArray;
	}
}
