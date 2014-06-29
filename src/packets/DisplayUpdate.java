package packets;

import client.GamePiece;

public class DisplayUpdate {
    public long timestamp;
    private GamePiece[] pieceArray;
    private int playerL, playerR;

    public DisplayUpdate() {
    }

    public DisplayUpdate(GamePiece[] renderList, long timestamp, int playerL, int playerR) {
        this.timestamp = timestamp;
        this.pieceArray = renderList;
        this.playerL = playerL;
        this.playerR = playerR;
    }

    /** Getters **/
    public GamePiece[] getRenderList() {
        return pieceArray;
    }

    public int getScoreL(){return playerL;} //Left player score
    public int getScoreR(){return playerR;} //Right player score
}