package server;

import java.util.List;

import client.GamePiece;

public class DisplayUpdate {
	public long timestamp;
	private List<GamePiece> renderList;
	public DisplayUpdate() {}
	
	public DisplayUpdate(List<GamePiece> renderList, long timestamp) {
		this.timestamp = timestamp;
		this.renderList = renderList;
	}
	
	public List<GamePiece> getRenderList() {
		return renderList;
	}
}
