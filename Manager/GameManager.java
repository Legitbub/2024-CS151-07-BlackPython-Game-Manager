
package Manager;

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
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.*;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class GameManager extends Application {
    private final double DEFAULT_WINDOW_LENGTH = 1240;
    private final double DEFAULT_WINDOW_WIDTH = 780;
    private final String USERS_FILE = "user_accounts.txt";
    private final String HIGH_SCORES_FILE = "high_scores.txt";
    private final byte[] salt = new byte[]{-97, 45, -104, 72, 18, 104, -121, -29, 30, -115, 82, -60, -121, -4, 71, 34};
    private Map<String, byte[] > userAccounts = new HashMap<>();
    private Map<String, int[]> highScores = new TreeMap<>(Collections.reverseOrder());

    public static Stage mainStage;
    public static Scene mainMenuScene;
    public static Scene loginScene;

    private Scene snakeScene;
    

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

        loginScene = new Scene(loginLayout, 400, 300);
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
        
            if (userAccounts.containsKey(username)) {

                byte[] storedHash = userAccounts.get(username);
                byte[] enteredPasswordHash = hashPassword(password);        
                if (Arrays.equals(storedHash, enteredPasswordHash)) {
                    showMainMenu(username);
                } else {
                    messageLabel.setText("Invalid password!");
                }
            } else {
                messageLabel.setText("Invalid username!");
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
                byte[] hpassword = hashPassword(password);
                userAccounts.put(username, hpassword);
                highScores.put(username, new int[] {0, 0}); 
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

    private  void showMainMenu(String username) {
        BorderPane mainLayout = new BorderPane();
        loadHighScores();
        // Top: Toolbar
        ToolBar toolbar = new ToolBar();
        Button mainMenuButton = new Button("Main Menu");
        Button logout = new Button("Logout");
        logout.setOnAction(e -> showLoginScreen());
        toolbar.getItems().add(mainMenuButton);
        toolbar.getItems().add(logout);
        mainLayout.setTop(toolbar);

        // Left: High Scores
        VBox highScoresBox = new VBox(10);
        highScoresBox.setPadding(new Insets(10));
        Label highScoresLabel = new Label("Top High Scores:");
        ListView<String> highScoresList = new ListView<>();
        
        // Display each user's scores (Blackjack and Snake)
        highScores.entrySet().stream()
                .limit(5) // Limit to top 5 users
                .forEach(entry -> {
                    String username1 = entry.getKey();
                    int[] scores = entry.getValue();
                    highScoresList.getItems().add(username1 + " - Blackjack: " + scores[0] + ", Snake: " + scores[1]);
                });
        
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

        mainMenuScene = new Scene(mainLayout, 600, 400);
        mainStage.setTitle("Game Manager - Main Menu");
        mainStage.setScene(mainMenuScene);
    }

    private void launchBlackjack(String username) {
        // Placeholder for Blackjack integration
        System.out.println("Launching Blackjack for " + username);
        mainStage.setScene(BlackjackUI.saveDataEntry(DEFAULT_WINDOW_LENGTH,
                DEFAULT_WINDOW_WIDTH, username));
    }

    private void launchSnake(String username) {
        // Placeholder for Snake integration
        System.out.println("Launching Snake for " + username);
        snakeScene = SnakeUI.createSnakeGame(mainStage, username);
        mainStage.setScene(snakeScene);
    }

    private void loadUserAccounts() {
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String username = parts[0];
                    byte[] hashedPassword = Base64.getDecoder().decode(parts[1]);
                    userAccounts.put(username, hashedPassword);
                }
            }
        } catch (IOException e) {
            System.out.println("No user accounts file found, starting fresh.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error decoding password from file. Ensure the file format is correct.");
        }
    }

    private void saveUserAccounts() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE))) {
            for (Map.Entry<String, byte[]> entry : userAccounts.entrySet()) {
                String username = entry.getKey();
                String hashedPasswordBase64 = Base64.getEncoder().encodeToString(entry.getValue());
                writer.write(username + "," + hashedPasswordBase64);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving user accounts.");
        }
    }
    private byte[] hashPassword(String password) {
        try {
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = factory.generateSecret(spec).getEncoded();
            return hash;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    
    private void loadHighScores() {
        try (BufferedReader reader = new BufferedReader(new FileReader(HIGH_SCORES_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String username = parts[0];
                    int blackjackHighScore = Integer.parseInt(parts[1]);
                    int snakeHighScore = Integer.parseInt(parts[2]);

                    // Store scores in the map
                    highScores.put(username, new int[] {blackjackHighScore, snakeHighScore});
                }
            }
        } catch (IOException e) {
            System.out.println("No high scores file found, starting fresh.");
        } catch (NumberFormatException e) {
            System.out.println("Error parsing high scores file. Ensure the format is correct.");
        }
    }

    private void saveHighScores() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(HIGH_SCORES_FILE))) {
            for (Map.Entry<String, int[]> entry : highScores.entrySet()) {
                // Convert the int[] to a string
                int[] scores = entry.getValue();
                writer.write(entry.getKey() + "," + scores[0] + "," + scores[1]); // Username, Blackjack, Snake
                writer.newLine(); 
            }
        } catch (IOException e) {
            System.out.println("Error saving high scores.");
        }
    }
}