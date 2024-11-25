package Snakes;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.canvas.Canvas;

public class Game extends Canvas {

    private int width;
    private int height;
    private static final int SEGMENT_SIZE = 25;
    private final Snake snake;
    private final ScoreManager scoreManager;
    private final Food food;

    public Game(int width, int height, Snake snake, ScoreManager scoreManager, Food food) {
        super(width * SEGMENT_SIZE, SEGMENT_SIZE * height);
        this.width = width;
        this.height = height;
        this.snake = snake;
        this.food = food;
        this.scoreManager = scoreManager;
    }

    public void draw() {
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());

        // Draw the snake
        gc.setFill(Color.GREEN);
        for (Segment segment : snake.getSegments()) { 
            if (segment.equals(snake.getSegments().get(0))) {
                gc.setFill(Color.LIMEGREEN);
            } else {
                gc.setFill(Color.GREEN);
            }
            gc.fillRect(segment.getX() * SEGMENT_SIZE, segment.getY() * SEGMENT_SIZE, SEGMENT_SIZE, SEGMENT_SIZE);
        }

        // Draw the food
        gc.setFill(Color.RED);
        gc.fillOval(food.getfoodX() * SEGMENT_SIZE, food.getfoodY() * SEGMENT_SIZE, SEGMENT_SIZE, SEGMENT_SIZE);

        // Draw the score
        gc.setFill(Color.BLACK);
        gc.setFont(new Font("", 30));
        gc.fillText("Score: " + scoreManager.getScore(), 10, 30);
    }

    // Method to check collisions and update game state
    public boolean updateGameState() {
        snake.moveSnake();

        // Check if the snake eats the food
        Segment head = snake.getSegments().get(0); 
        if (head.getX() == food.getfoodX() && head.getY() == food.getfoodY()) {
            snake.growSnake();
            scoreManager.incrementScore(1);
            food.randomNewFoodLocation(snake.getSegments());
        }

        // Check for collisions with walls and return false for game over
        if (head.getX() < 0 || head.getX() >= width || head.getY() < 0 || head.getY() >= height) {
            return false;
        }

        // Check for collisions with itself and return false for game over
        for (int i = 1; i < snake.getSegments().size() - 1; i++) {
            Segment segment = snake.getSegments().get(i); 
            if (head.getX() == segment.getX() && head.getY() == segment.getY()) {
                System.out.println("You hit yourself");
                return false;
            }
        }
        // Continue the game if snake has not hit the wall or itself
        return true;
    }



    public void drawPauseMenu(){
                //For drawing pause overlay
        GraphicsContext gc = getGraphicsContext2D();
        gc.setFill(Color.rgb(0, 0, 0, 0.8)); 
        gc.fillRect(0, 0, getWidth(), getHeight());

        //Display the "Game Paused" text when pressing "ESC"
        //Aligns the text in the center of the x-axis and 1/3 of the y-axis

        Font size = new Font(" ", 60);
        Text pauseText = new Text("Game Paused");
        pauseText.setFont(size);
        //Get the width and height of the text itself in its local bounds 
        //so that it is not affected by any other factor
        //Note: I find that using Text and its related funcitons works best when dealing with aligning texts on javafx.
        double textWidth = pauseText.getBoundsInLocal().getWidth();
        double textHeight = pauseText.getBoundsInLocal().getHeight();
        //Get width and height of the game board to get the x and y cords of where the text should be centered.
        double PauseXCord = (getWidth() - textWidth) / 2;
        double PauseYCord = (getHeight() + textHeight) / 2;

        gc.setFill(Color.WHITE);
        gc.setFont(size);
        gc.fillText(pauseText.getText(), PauseXCord, PauseYCord);
    }
    //Method for drawing the game over screen when the snake dies
    public void drawGameOver() {
        GraphicsContext gc = getGraphicsContext2D();
        
        gc.setFill(Color.rgb(0, 0, 0, 0.8));
        gc.fillRect(0, 0, getWidth(), getHeight());
    
        Font gameOverFont = new Font(" ", 50);
        Font scoreFont = new Font(" ", 30);
    
        Text gameOverText = new Text("Game Over");
        gameOverText.setFont(gameOverFont);
        double gameOverWidth = gameOverText.getBoundsInLocal().getWidth();
        double gameOverHeight = gameOverText.getBoundsInLocal().getHeight();
    
        Text highScoreText = new Text("High Score: " + scoreManager.getHighScore());
        highScoreText.setFont(scoreFont);
        double highScoreWidth = highScoreText.getBoundsInLocal().getWidth();
        double highScoreHeight = highScoreText.getBoundsInLocal().getHeight();
    
        Text scoreText = new Text("Score: " + scoreManager.getScore());
        scoreText.setFont(scoreFont);
        double scoreWidth = scoreText.getBoundsInLocal().getWidth();
        double scoreHeight = scoreText.getBoundsInLocal().getHeight();
    
        // Calculate positions for centering
        double gameOverXCord = (getWidth() - gameOverWidth) / 2;
        double gameOverYCord = (getHeight() / 2) - gameOverHeight;
    
        double highScoreXCord = (getWidth() - highScoreWidth) / 2;
        double highScoreYCord = (getHeight() + highScoreHeight) / 2; // Adjust spacing below "Game Over"
    
        double scoreX = (getWidth() - scoreWidth) / 2;
        double scoreY = (getHeight() / 2) + 10 + scoreHeight + 10; // Add spacing below high score
    
        // Draw "Game Over" text
        gc.setFill(Color.WHITE);
        gc.setFont(gameOverFont);
        gc.fillText(gameOverText.getText(), gameOverXCord, gameOverYCord);
    
        // Draw high score and current score
        gc.setFont(scoreFont);
        gc.fillText(highScoreText.getText(), highScoreXCord, highScoreYCord);
        gc.fillText(scoreText.getText(), scoreX, scoreY);
    }
}
