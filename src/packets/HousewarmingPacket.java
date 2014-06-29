package packets;

import java.util.List;

import org.newdawn.slick.geom.Shape;

public class HousewarmingPacket {
    int[] relevantChars;
    long userId;
    List<Shape> shapes;

    public HousewarmingPacket() {
    }

    public HousewarmingPacket(int[] relevantChars, long userId) {
        this.relevantChars = relevantChars;
        this.userId = userId;
    }

    public long getUserId() {
        return userId;
    }

    public int[] getRelevantChars() {
        return relevantChars;
    }
}