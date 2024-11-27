package Snakes;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class SnakeUI extends Application {
    private static final int WIDTH = 20;  
    private static final int HEIGHT = 20; 
    private boolean isPaused = false;
    private boolean isGameOver = false;
    private AnimationTimer timer;

    private Snake snake;
    private Food food;
    private ScoreManager scoreManager;
    private Game gameBoard;
    private InputHandler inputHandler;

    //Start the game by first initializing everything
    @Override
    public void start(Stage primaryStage) {
        initializeGame(primaryStage);
    }

    private void initializeGame(Stage primaryStage) {
        // Initialize the game objects
        snake = new Snake(WIDTH, HEIGHT);
        food = new Food(WIDTH, HEIGHT);
        scoreManager = new ScoreManager();
        gameBoard = new Game(WIDTH, HEIGHT, snake, scoreManager, food);
        inputHandler = new InputHandler(snake);

        // Generate the initial food location
        food.randomNewFoodLocation(snake.getSegments());

        StackPane root = new StackPane();
        root.getChildren().add(gameBoard);

        
        //Create the restart button and set the set visibility to false until game is over
        Button restartButton = new Button("Restart");
        //Some CSS styling
        //Note: Should prob use this for the BlackJackUI too
        restartButton.setStyle("-fx-font-size: 20px; -fx-background-color: #4CAF50; -fx-text-fill: white;");
        restartButton.setVisible(false);
        restartButton.setOnAction(event -> restartGame(primaryStage, restartButton));
        root.getChildren().add(restartButton);
        restartButton.setTranslateY(110);

        Scene scene = new Scene(root);
        scene.setOnKeyPressed(event -> {
            if (!isGameOver) {
                if (event.getCode().toString().equals("ESCAPE")) {
                    togglePause(gameBoard);
                } else {
                    inputHandler.handleKeyPress(event);
                }
            }
        });

        primaryStage.setTitle("Snake Game");
        primaryStage.setScene(scene);
        primaryStage.show();

        // timer for looping the game each time the gama board is updating its game state
        timer = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long currentTime) {
                //if the game is not paused or over then update the game state every specified interval
                if (!isPaused && !isGameOver) {
                    if (currentTime - lastUpdate >= 115_000_000) { // Update every 115ms
                        boolean gameIsRunning = gameBoard.updateGameState();
                        gameBoard.draw();
                        if (!gameIsRunning) {
                            stop();
                            isGameOver = true;
                            scoreManager.updateHighScore();
                            gameBoard.drawGameOver();
                            restartButton.setVisible(true); 
                        }
                        lastUpdate = currentTime;
                    }
                }
            }
        };
        timer.start();
    }

    //Bring up the pause menu when ESP is pressed and go back to game when ESP is pressed again
    private void togglePause(Game gameBoard) {
        isPaused = !isPaused;

        if (isPaused) {
            gameBoard.drawPauseMenu();
        } else {
            gameBoard.draw();
        }
    }

    //stop the timer and rest game state and reinitialize the game when restart button is clicked
    private void restartGame(Stage primaryStage, Button restartButton) { 
        if (timer != null) {
            timer.stop();
        }
        isGameOver = false;
        isPaused = false;
        restartButton.setVisible(false);
        initializeGame(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
