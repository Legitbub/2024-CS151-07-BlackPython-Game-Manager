package Snakes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ScoreManager {
    
    private int score;
    private int highScore;
    private static final String HIGH_SCORE_FILE = "highscore.txt";


    public ScoreManager(){
        this.score = 0;
        this.highScore = loadHighScore();
    }
    //increment score for every food eaten
    public void incrementScore(int amount){
        score += amount;
    }
    //update new high score if current score is higher
    //save high score to the txt file to store the high score for future games.
    public void updateHighScore() {
        if (score > highScore) {
            highScore = score;
            saveHighScore();
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

    private int loadHighScore() {
        try (BufferedReader reader = new BufferedReader(new FileReader(HIGH_SCORE_FILE))) {
            return Integer.parseInt(reader.readLine());
        } catch (IOException | NumberFormatException e) {
            return 0; // Default high score if file is missing or invalid
        }
    }

    private void saveHighScore() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(HIGH_SCORE_FILE))) {
            writer.write(String.valueOf(highScore));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
