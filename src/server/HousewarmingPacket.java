package server;

public class HousewarmingPacket {
	int[] relevantChars;
	long userId;
	
	public HousewarmingPacket() {
		this.relevantChars = new int[0];
		this.userId = 0;
	}
	
	public HousewarmingPacket(int[] relevantChars, long userId) {
		this.relevantChars=relevantChars;
		this.userId = userId;
	}
	
	public long getUserId() {
		return userId;
	}
	
	public int[] getRelevantChars() {
		return relevantChars;
	}
}
