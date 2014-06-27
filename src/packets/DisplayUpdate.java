package packets;

import java.util.List;

import client.GamePiece;

public class DisplayUpdate {
	public long timestamp;
	private GamePiece[] renderList;
	public DisplayUpdate() {}
	
	public DisplayUpdate(GamePiece[] renderList, long timestamp) {
		this.timestamp = timestamp;
		this.renderList = renderList;
	}
	
	public GamePiece[] getRenderList() {
		return renderList;
	}
}
