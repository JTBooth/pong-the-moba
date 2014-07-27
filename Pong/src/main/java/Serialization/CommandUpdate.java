package serialization;

public class CommandUpdate implements Comparable<CommandUpdate> {
    private int[] keys;
    private long timestamp;

    public CommandUpdate() {
        this.keys = new int[0];
        this.timestamp = System.nanoTime();
    }

    public CommandUpdate(int[] keys) {
        this.keys = keys;
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

    public int[] getKeys() {
        return keys;
    }
}
