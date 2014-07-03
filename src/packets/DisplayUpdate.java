package packets;

public class DisplayUpdate {
    public long timestamp;
    private GamePiece[] pieceArray;
    private int[] score;

    public DisplayUpdate() {
    }

    public DisplayUpdate(GamePiece[] renderList, int[] score, long timestamp) {
        this.timestamp = timestamp;
        this.pieceArray = renderList;
        this.score = score;
    }


    /**
     * Getters *
     */
    public GamePiece[] getRenderList() {
        return pieceArray;
    }

    public int[] getScore() {
        return score;
    }
}