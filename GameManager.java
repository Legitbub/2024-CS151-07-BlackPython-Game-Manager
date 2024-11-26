import Blackjack.BlackjackUI;
import Blackjack.BJGame;
import Snakes.SnakeUI;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;

public class GameManager extends Application {
    private final double DEFAULT_WINDOW_LENGTH = 1240;
    private final double DEFAULT_WINDOW_WIDTH = 780;
    private final String USERS_FILE = "user_accounts.txt";
    private final String HIGH_SCORES_FILE = "high_scores.txt";
    private Map<String, String> userAccounts = new HashMap<>();
    private Map<String, Integer> highScores = new TreeMap<>(Collections.reverseOrder());

    private Stage mainStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {

        mainStage = stage;
        loadUserAccounts();
        loadHighScores();
        showLoginScreen();
    }

    private void showLoginScreen() {
        VBox loginLayout = new VBox(10);
        loginLayout.setPadding(new Insets(20));

        Label welcomeLabel = new Label("Welcome to the Game Manager!");
        welcomeLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");

        Button loginButton = new Button("Log In");
        Button createAccountButton = new Button("Create an Account");

        loginButton.setOnAction(e -> showLoginForm());
        createAccountButton.setOnAction(e -> showCreateAccountForm());

        loginLayout.getChildren().addAll(welcomeLabel, loginButton, createAccountButton);

        Scene loginScene = new Scene(loginLayout, 400, 300);
        mainStage.setTitle("Game Manager - Login");
        mainStage.setScene(loginScene);
        mainStage.show();
    }

    private void showLoginForm() {
        VBox formLayout = new VBox(10);
        formLayout.setPadding(new Insets(20));

        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();

        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();

        Button backButton = new Button("Back to Welcome Screen");
        Button submitButton = new Button("Log In");
        Label messageLabel = new Label();
        HBox buttons = new HBox(20, submitButton, backButton);

        submitButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            if (userAccounts.containsKey(username) && userAccounts.get(username).equals(password)) {
                showMainMenu(username);
            } else {
                messageLabel.setText("Invalid username or password!");
            }
        });

        backButton.setOnAction(e -> showLoginScreen());
        formLayout.getChildren().addAll(usernameLabel, usernameField,
                passwordLabel, passwordField, buttons, messageLabel);

        Scene loginFormScene = new Scene(formLayout, 400, 300);
        mainStage.setTitle("Game Manager - Log In");
        mainStage.setScene(loginFormScene);
    }

    private void showCreateAccountForm() {
        VBox formLayout = new VBox(10);
        formLayout.setPadding(new Insets(20));

        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();

        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();

        Button submitButton = new Button("Create Account");
        Label messageLabel = new Label();

        Button backButton = new Button("Back to Welcome Screen");
        backButton.setOnAction(e -> showLoginScreen());

        submitButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            if (userAccounts.containsKey(username)) {
                messageLabel.setText("Username already exists!");
            } else {
                userAccounts.put(username, password);
                highScores.put(username, 0); 
                saveUserAccounts();
                saveHighScores();
                messageLabel.setText("Account created successfully!");
            }
        });

        HBox buttons = new HBox(20, submitButton, backButton);

        formLayout.getChildren().addAll(usernameLabel, usernameField,
                passwordLabel, passwordField, buttons, messageLabel);

        Scene createAccountScene = new Scene(formLayout, 400, 300);
        mainStage.setTitle("Game Manager - Create Account");
        mainStage.setScene(createAccountScene);
    }

    private void showMainMenu(String username) {
        BorderPane mainLayout = new BorderPane();

        // Top: Toolbar
        ToolBar toolbar = new ToolBar();
        Button mainMenuButton = new Button("Main Menu");
        toolbar.getItems().add(mainMenuButton);
        mainLayout.setTop(toolbar);

        // Left: High Scores
        VBox highScoresBox = new VBox(10);
        highScoresBox.setPadding(new Insets(10));
        Label highScoresLabel = new Label("Top High Scores:");
        ListView<String> highScoresList = new ListView<>();
        highScores.entrySet().stream()
                .limit(5)
                .forEach(entry -> highScoresList.getItems().add(entry.getKey() + ": " + entry.getValue()));

        highScoresBox.getChildren().addAll(highScoresLabel, highScoresList);
        mainLayout.setLeft(highScoresBox);

        // Center: Game Options
        VBox gameOptionsBox = new VBox(10);
        gameOptionsBox.setPadding(new Insets(10));
        Button blackjackButton = new Button("Play Blackjack");
        Button snakeButton = new Button("Play Snake Game");

        blackjackButton.setOnAction(e -> launchBlackjack(username));
        snakeButton.setOnAction(e -> launchSnake(username));

        gameOptionsBox.getChildren().addAll(blackjackButton, snakeButton);
        mainLayout.setCenter(gameOptionsBox);

        Scene mainMenuScene = new Scene(mainLayout, 600, 400);
        mainStage.setTitle("Game Manager - Main Menu");
        mainStage.setScene(mainMenuScene);
    }

    private void launchBlackjack(String username) {
        // Placeholder for Blackjack integration
        System.out.println("Launching Blackjack for " + username);
        BlackjackUI.makeUI(DEFAULT_WINDOW_LENGTH,
            DEFAULT_WINDOW_WIDTH);
        BJGame.playRound();
        mainStage.setScene(BlackjackUI.getUi());
    }

    private void launchSnake(String username) {
        // Placeholder for Snake integration
        System.out.println("Launching Snake for " + username);
    }

    private void loadUserAccounts() {
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    userAccounts.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            System.out.println("No user accounts file found, starting fresh.");
        }
    }

    private void saveUserAccounts() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE))) {
            for (Map.Entry<String, String> entry : userAccounts.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving user accounts.");
        }
    }

    private void loadHighScores() {
        try (BufferedReader reader = new BufferedReader(new FileReader(HIGH_SCORES_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    highScores.put(parts[0], Integer.parseInt(parts[1]));
                }
            }
        } catch (IOException e) {
            System.out.println("No high scores file found, starting fresh.");
        }
    }

    private void saveHighScores() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(HIGH_SCORES_FILE))) {
            for (Map.Entry<String, Integer> entry : highScores.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving high scores.");
        }
    }
}
