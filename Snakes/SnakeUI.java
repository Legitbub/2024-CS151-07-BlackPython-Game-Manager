package Snakes;

import Manager.GameManager;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class SnakeUI{
    private static final int WIDTH = 20;
    private static final int HEIGHT = 20;
    private static boolean isPaused = false;
    private static boolean isGameOver = false;
    private static AnimationTimer timer;
    private static Scene scene;

    public static Scene createSnakeGame(Stage stage) {
        Snake snake = new Snake(WIDTH, HEIGHT);
        Food food = new Food(WIDTH, HEIGHT);
        ScoreManager scoreManager = new ScoreManager();
        Game gameBoard = new Game(WIDTH, HEIGHT, snake, scoreManager, food);
        InputHandler inputHandler = new InputHandler(snake);

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
        restartButton.setOnAction(event -> restartGame(stage, restartButton, root, gameBoard, snake, food, scoreManager));
        root.getChildren().add(restartButton);
        restartButton.setTranslateY(110);
        
        //set the focus of the button to false so it doesnt interfere with key events to control the snake's direction
        Button mainMenu = new Button("Main Menu");
        mainMenu.setOnAction(e -> {
            stage.setScene(GameManager.mainMenuScene);
        });
        mainMenu.setFocusTraversable(false); 

        Button logout = new Button("logout");
        logout.setOnAction(e -> {
            stage.setScene(GameManager.loginScene);
        });
        logout.setFocusTraversable(false); 

        ToolBar toolBar = new ToolBar(mainMenu, logout);
        toolBar.setFocusTraversable(false); 

        BorderPane b = new BorderPane();
        b.setTop(toolBar);
        b.setCenter(root);

        Scene scene = new Scene(b);
        scene.setOnKeyPressed(event -> {
            if (!isGameOver) {
                if (event.getCode().toString().equals("ESCAPE")) {
                    togglePause(gameBoard);
                } else {
                    inputHandler.handleKeyPress(event);
                }
            }
        });


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

        return scene;
    }

    //Bring up the pause menu when ESP is pressed and go back to game when ESP is pressed again
    private static void togglePause(Game gameBoard) {
        isPaused = !isPaused;
        if (isPaused) {
            gameBoard.drawPauseMenu();
        } else {
            gameBoard.draw();
        }
    }

    //stop the timer and rest game state and reinitialize the game when restart button is clicked
    private static void restartGame(Stage stage, Button restartButton, StackPane root, Game gameBoard, Snake snake, Food food, ScoreManager scoreManager) {
        timer.stop();
        isPaused = false;
        isGameOver = false;
        restartButton.setVisible(false);
        Scene newScene = createSnakeGame(stage);
        stage.setScene(newScene);
    }


}
