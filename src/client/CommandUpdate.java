package client;

public class CommandUpdate implements Comparable<CommandUpdate> {
    private int[] keys;
    private long timestamp;
    private String playerId;

    public CommandUpdate() {
        this.keys = new int[0];
        this.playerId = "";
        this.timestamp = System.nanoTime();
    }

    public CommandUpdate(int[] keys, String playerId) {
        this.keys = keys;
        this.playerId = playerId;
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

    public String getPlayerId() {
        return playerId;
    }

    public int[] getKeys() {
        return keys;
    }
}
