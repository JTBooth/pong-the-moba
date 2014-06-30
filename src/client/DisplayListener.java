package client;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import packets.DisplayUpdate;
import packets.GamePiece;
import packets.HousewarmingPacket;

public class DisplayListener extends Listener {
    private DisplayUpdate currentUpdate;
    private PongClient pongClient;
    private int[] relevantCharacters;
    private long userId;

    public DisplayListener(PongClient pongClient) {
        this.pongClient = pongClient;

        currentUpdate = new DisplayUpdate(new GamePiece[0], new int[]{0,0}, 0); //Game Piece Array, scores, timestamp

    }

    @Override
    public void received(Connection con, Object packet) {
//        System.out.println("currentUpdate has " + currentUpdate.getRenderList().length + " pieces in it");
        if (packet instanceof DisplayUpdate) {
            DisplayUpdate displayUpdate = (DisplayUpdate) packet;
            if (displayUpdate.timestamp > currentUpdate.timestamp) {
                currentUpdate = displayUpdate;
            }


        } else if (packet instanceof HousewarmingPacket) {
            HousewarmingPacket housewarmingPacket = (HousewarmingPacket) packet;
            pongClient.initialize(housewarmingPacket);
            relevantCharacters = housewarmingPacket.getRelevantChars();
            userId = housewarmingPacket.getUserId();
        }
    }

    @Override
    public void connected(Connection connection) {
        super.connected(connection);
        connection.setTimeout(0);
    }

    /** Getters **/

    public GamePiece[] getRenderList() {
        return currentUpdate.getRenderList();
    }

    public int[] getRelevantCharacters() {
        return relevantCharacters;
    }

    public int[] getScores(){
        return currentUpdate.getScore();
    }
}