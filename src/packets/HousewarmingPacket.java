package packets;

public class HousewarmingPacket {
    int[] relevantChars;
    long gameId;
    String userId;

    public HousewarmingPacket() {
    }

    public HousewarmingPacket(int[] relevantChars, String userId, long gameId) {
        this.relevantChars = relevantChars;
        this.userId = userId;
        this.gameId = gameId;
    }

    public String getUserId() {
        return userId;
    }

    public int[] getRelevantChars() {
        return relevantChars;
    }

    public long getGameId() {
        return gameId;
    }
}