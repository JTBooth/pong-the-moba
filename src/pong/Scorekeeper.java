package pong;

public class Scorekeeper {
    private int[] goals;
    private int winningScore;
    private String p1name;
    private String p2name;

    public Scorekeeper(int winningScore) {
        this(winningScore, "PLAYER 1", "PLAYER 2");
    }

    public Scorekeeper(int winningScore, String p1name, String p2name) {
        goals = new int[2];
        this.winningScore = winningScore;
        this.p1name = p1name;
        this.p2name = p2name;
    }

    public void playerScore(int player, int score) {
        goals[player] += score;
    }

    public boolean gameOver() {
        if ((goals[0] > winningScore || goals[1] > winningScore) && goals[0] != goals[1]) {
            return true;
        } else {
            return false;
        }
    }

    public String getLeaderName() {
        if (goals[0] > goals[1]) {
            return p1name;
        } else if (goals[1] > goals[0]) {
            return p2name;
        } else {
            return "DRAW";
        }
    }

    public int[] getScore() {
        return goals;
    }

}
