package packets;

import client.GamePiece;

public class DisplayUpdate {
    public long timestamp;
    private GamePiece[] pieceArray;
    private int scores[];

    public DisplayUpdate() {
    }

    public DisplayUpdate(GamePiece[] renderList, long timestamp, int[] scores) {
        this.timestamp = timestamp;
        this.pieceArray = renderList;
        this.scores = scores;
    }

    /** Getters **/
    public GamePiece[] getRenderList() {
        return pieceArray;
    }

    public int[] getScore(){return scores;} //Left player score
}