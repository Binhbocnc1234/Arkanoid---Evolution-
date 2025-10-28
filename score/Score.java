package score;

public class Score {
    private int playerScore;

    public Score() {
        playerScore = 0;
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public void setPlayerScore(int playerScore) {
        this.playerScore = playerScore;
    }

    public void updatePlayerScore(int score) {
        this.playerScore += score;
    }

    public void resetPlayerScore() {
        playerScore = 0;
    }
}
