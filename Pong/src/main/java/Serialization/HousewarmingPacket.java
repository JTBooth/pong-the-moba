package serialization;

public class HousewarmingPacket {
    int[] relevantChars;

    public HousewarmingPacket() {
    }

    public HousewarmingPacket(int[] relevantChars) {
        this.relevantChars = relevantChars;
    }

    public int[] getRelevantChars() {
        return relevantChars;
    }
}