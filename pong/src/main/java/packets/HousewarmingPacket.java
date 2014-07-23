package packets;

public class HousewarmingPacket {
    int[] relevantChars;
    long gameId;
    long userId;

    public HousewarmingPacket() {
    }

    public HousewarmingPacket(int[] relevantChars, long userId, long gameId) {
        this.relevantChars = relevantChars;
        this.userId = userId;
        this.gameId = gameId;
    }

    public long getUserId() {
        return userId;
    }

    public int[] getRelevantChars() {
        return relevantChars;
    }

    public long getGameId() {
        return gameId;
    }
}