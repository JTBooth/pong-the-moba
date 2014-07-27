package serialize;

public class CommandUpdate implements Comparable<CommandUpdate> {
    private int[] keys;
    private long timestamp;
    private long playerId;
    private long gameId;

    public CommandUpdate() {
        this.keys = new int[0];
        this.playerId = 0;
        this.gameId = 0;
        this.timestamp = System.nanoTime();
    }

    public CommandUpdate(int[] keys, long playerId, long gameId) {
        this.keys = keys;
        this.playerId = playerId;
        this.gameId = gameId;
        this.timestamp = System.nanoTime();
    }

    @Override
    public int compareTo(CommandUpdate other) {
        if (timestamp < other.getTimestamp()) {
            return -1;
        } else if (timestamp == other.getTimestamp()) {
            return 0;
        } else {
            return 1;
        }
    }

    public long getTimestamp() {
        return timestamp;
    }

    public long getGameId() {
        return gameId;
    }

    public long getPlayerId() {
        return playerId;
    }

    public int[] getKeys() {
        return keys;
    }
}
