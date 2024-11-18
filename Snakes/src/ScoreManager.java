public class ScoreManager {
    
    private int score;
    private int highScore;

    public ScoreManager(){
        this.score = 0;
        this.highScore = 0;
    }
    //increment score for every food eaten
    //update new high score if current score is higher
    public void incrementScore(int amount){
        score += amount;
        if(score > highScore){
            highScore = score;
        }
    }

    //resetting score for a new game
    public void resetScore(){
        score = 0;
    }

    public int getScore(){
        return score;
    }

    public int getHighScore(){
        return highScore;
    }
}
