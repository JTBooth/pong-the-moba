package packets;

import org.newdawn.slick.geom.Shape;

import java.util.List;

public class HousewarmingPacket {
    int[] relevantChars;
    String userId;
    List<Shape> shapes;

    public HousewarmingPacket() {
    }

    public HousewarmingPacket(int[] relevantChars, String userId) {
        this.relevantChars = relevantChars;
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public int[] getRelevantChars() {
        return relevantChars;
    }
}