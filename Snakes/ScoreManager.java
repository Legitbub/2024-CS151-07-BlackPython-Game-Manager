package Snakes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ScoreManager {
    
    private int score;
    private int highScore;
    private String username;
    private static final String HIGH_SCORE_FILE = "high_scores.txt";


    public ScoreManager(String username) {
        this.score = 0;
        this.username = username;
        this.highScore = loadHighScore(username);
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

    private int loadHighScore(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(HIGH_SCORE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(","); // Split the row into columns
                if (parts.length == 3 && parts[0].equals(username)) {
                    return Integer.parseInt(parts[2]); // Return the 3rd value (snake high score)
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error loading high score for user " + username + ": " + e.getMessage());
        }
        return 0; // Default high score if the user is not found or file is invalid
    }

    private void saveHighScore() {
    List<String> lines = new ArrayList<>();
    boolean userFound = false;

    try (BufferedReader reader = new BufferedReader(new FileReader(HIGH_SCORE_FILE))) {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length == 3 && parts[0].equals(username)) {
                // Update the snake high score for the user
                parts[2] = String.valueOf(highScore);
                line = String.join(",", parts); // Reconstruct the line
                userFound = true;
            }
            lines.add(line); // Add the line to the list
        }
    } catch (IOException e) {
        System.out.println("Error reading high score file: " + e.getMessage());
    }

    // If the user was not found, add a new row for them
    if (!userFound) {
        lines.add(username + ",0," + highScore);
    }

    // Write the updated contents back to the file
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(HIGH_SCORE_FILE))) {
        for (String updatedLine : lines) {
            writer.write(updatedLine);
            writer.newLine();
        }
    } catch (IOException e) {
        System.out.println("Error saving high score file: " + e.getMessage());
    }
}

}
