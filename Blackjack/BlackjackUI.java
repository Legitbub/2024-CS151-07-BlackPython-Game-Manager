/**
 * Handles frontend display for Blackjack game
 */

package Blackjack;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.geometry.Pos;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

import Manager.GameManager;

public class BlackjackUI {
    private static Scene ui;
    private static String username;
    private static final String HIGH_SCORE_FILE = "high_scores.txt";
    static GameState game = BJGame.getGame();

    // Ask user for save data or new game at the start
    public static Scene saveDataEntry(double length, double width, String username) {
        BlackjackUI.username = username;
        ToolBar toolBar = makeToolbar();
        BorderPane bp = new BorderPane();
        bp.setTop(toolBar);

        Label saveDataHeader = new Label("Save data:");
        TextField saveData = new TextField();

        Button newGame = new Button("Start new game");
        newGame.setOnAction(e -> {
            BJGame.setGame(new GameState());
            game = BJGame.getGame();
            makeUI(length, width, true);
            GameManager.mainStage.setScene(getUi());
        });

        // Save data string is copied to clipboard when pressing
        // the save button in a previous game
        // Pasting this data string is necessary for the button to work
        Button savedGame = new Button("Start game from save data string");
        savedGame.setOnAction(e -> {
            String data = saveData.getText();
            String[] balance = data.split(",");
            String[] userHand = balance[2].split("F0");
            String[] p1Hand = userHand[1].split("F1");
            String[] p2Hand = p1Hand[1].split("F2");
            String[] dealHand = p2Hand[1].split("F3");
            String[] cards = dealHand[1].split("DCK");
            try {
                BJGame.setGame(new GameState(Integer.parseInt(balance[0]),
                        Integer.parseInt(balance[1]), userHand[0], p1Hand[0],
                        p1Hand[0], dealHand[0], cards));
                game = BJGame.getGame();
                makeUI(length, width, false);
                GameManager.mainStage.setScene(getUi());
            } catch (Exception ex) {

            }
        });

        VBox saveDataBox = new VBox(5, saveDataHeader, saveData);
        HBox buttons = new HBox(30, newGame, savedGame);
        VBox display = new VBox(30, saveDataBox, buttons);
        display.setPadding(new Insets(40));
        bp.setCenter(display);

        Scene scene = new Scene(bp, 400, 300);
        return scene;
    }
    // Initial layout of the Blackjack game
    public static void makeUI(double length, double width, boolean isNewGame) {
        // Screen areas of each player
        HBox dealerHand = new HBox();
        VBox dealerArea = new VBox(dealerHand);
        HBox cpu1Hand = new HBox();
        VBox cpu1Area = new VBox(cpu1Hand);
        HBox cpu2Hand = new HBox();
        VBox cpu2Area = new VBox(cpu2Hand);

        game.getUser().setBalance(loadBalance(username));
        // Bottom of screen for user interaction
        TextField betEntry = new TextField();
        betEntry.setPromptText("$0");
        Button betButton = new Button("Bet");
        Label curBet = new Label("Bet: $" + game.getUser().getBet());
        Label bankroll = new Label("Bankroll: $" + game.getUser().getBalance());
        HBox moneyStats = new HBox(20, curBet, bankroll);
        Button hit = new Button("Hit");
        Button stay = new Button("Stay");
        HBox cardActions = new HBox(50, hit, stay);
        HBox playerHand = new HBox();
        HBox betTop = new HBox(betEntry, betButton);
        VBox bet = new VBox(20, betTop, moneyStats, cardActions);
        HBox player = new HBox(100, playerHand, bet);
        VBox playerArea = new VBox(player);

        // Store regions in a list for easy loop access
        ArrayList<VBox> areas = new ArrayList<>();
        areas.add(playerArea);
        areas.add(cpu1Area);
        areas.add(cpu2Area);
        areas.add(dealerArea);

        // Button to start next round after round finishes
        Button replay = new Button("Play Again");

        // Force the bet field to be numeric only
        // Lambda expression enforces ChangeListener<String> in javafx.beans
        betEntry.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                betEntry.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        // Send the user bet info to the game when button clicked
        betButton.setOnAction(e -> {
            int betAmount = 0;
            try {
                betAmount = Integer.parseInt(betEntry.getText());
            } catch (NumberFormatException ex) {

            }
            if (betAmount <= game.getUser().getBalance()) {
                betEntry.clear();
                game.getUser().setBet(betAmount);
                curBet.setText("Bet: $" + game.getUser().getBet());
            }
        });

        // Generate a new card for user when Hit is pressed
        hit.setOnAction(e -> {
            if (!(game.getUser().turnEnd)) {
                game.getUser().takeTurn();
                displayHand(game.getUser());
                if (game.getUser().turnEnd) {
                    BJGame.playRestOfRound();
                    playerArea.getChildren().add(replay);
                }
                bankroll.setText("Bankroll: $" + game.getUser().getBalance());
            }
        });

        // End the User's turn; let other players take turns
        stay.setOnAction(e -> {
            if (!game.getUser().turnEnd) {
                BJGame.playRestOfRound();
                game.getUser().turnEnd = true;
                bankroll.setText("Bankroll: $" + game.getUser().getBalance());
                playerArea.getChildren().add(replay);
            }
        });

        // Reset everything when replay button is clicked
        replay.setOnAction(e -> {
            playerArea.getChildren().removeLast();
            for (int i = 0; i < game.getPeople().size(); i++) {
                if (game.getPeople().get(i).bust) {
                    if (i == 0) {
                        areas.get(i).getChildren().removeFirst();
                    } else {
                        areas.get(i).getChildren().removeLast();
                    }
                    game.getPeople().get(i).bust = false;
                }
                game.getPeople().get(i).turnEnd = false;
            }
            game.startRound();
            game.getDeck().newDeck();
            BJGame.playRound();
        });

        Font labelFont = new Font("Comic Sans MS", 70);
        Label gameLabel = new Label("Blackjack,\nprobably");
        gameLabel.setFont(labelFont);

        // Set up display positions of each player area
        BorderPane bp = new BorderPane();
        bp.setTop(dealerArea);
        dealerHand.setAlignment(Pos.CENTER);
        dealerArea.setAlignment(Pos.CENTER);
        bp.setLeft(cpu1Area);
        cpu1Area.setAlignment(Pos.CENTER);
        bp.setRight(cpu2Area);
        cpu2Area.setAlignment(Pos.CENTER);
        bp.setBottom(playerArea);
        player.setAlignment(Pos.CENTER);
        playerArea.setAlignment(Pos.CENTER);
        bp.setCenter(gameLabel);

        ToolBar toolBar = makeToolbar();

        // Button to save the state of the game into a String
        // String is copied to clipboard for future pasting
        Button save = new Button("Save");
        save.setOnAction(e -> {
            saveBalance();
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent saveString = new ClipboardContent();
            StringBuilder str = new StringBuilder();
            str.append(game.getUser().getBalance()).append(",");
            str.append(game.getUser().getBet()).append(",");
            for (Card c : game.getUser().getHand()) {
                str.append(c.getValue() + " " + c.getSuit() + "UC");
            }
            str.append("F0");
            for (Card c : game.getP1().getHand()) {
                str.append(c.getValue() + " " + c.getSuit() + "P1");
            }
            str.append("F1");
            for (Card c : game.getP2().getHand()) {
                str.append(c.getValue() + " " + c.getSuit() + "P2");
            }
            str.append("F2");
            for (Card c : game.getDealer().getHand()) {
                str.append(c.getValue() + " " + c.getSuit() + "DL");
            }
            str.append("F3");
            Stack<Card> deckCopy = (Stack<Card>) game.getDeck().getDeck().clone();
            while (!deckCopy.isEmpty()) {
                Card c = deckCopy.pop();
                str.append(c.getValue() + " " + c.getSuit() + "DCK");
            }
            saveString.putString(str.toString());
            clipboard.setContent(saveString);

            // Notify user of successful data save
            toolBar.getItems().add(new Label("Game data copied to clipboard!"));
            /*Timer t = new Timer();
            t.schedule(new TimerTask() {
                @Override
                public void run() {
                    toolBar.getItems().removeLast();
                }
            }, 2000);*/
        });

        toolBar.getItems().add(save);

        BorderPane b = new BorderPane();
        b.setTop(toolBar);
        b.setCenter(bp);
        ui = new Scene(b, length, width);
        if (isNewGame) {
            BJGame.playRound();
        } else {
            for(Person p : game.getPeople()) {
                displayHand(p);
            }
            hideDealerCard();
        }
    }

    // Show the cards in each player's hand
    public static void displayHand(Person p) {
        BorderPane bp = (BorderPane) ((BorderPane) ui.getRoot()).getCenter();
        HBox curPlayer;
        if (p == game.getDealer()) {
            curPlayer = (HBox) ((VBox) (bp.getTop())).getChildren().get(0);
        } else if (p == game.getUser()) {
            curPlayer = (HBox) ((HBox) ((VBox) (bp.getBottom())).getChildren().getLast())
                    .getChildren().get(0);
        } else if (p == game.getP1()) {
            curPlayer = (HBox) ((VBox) (bp.getLeft())).getChildren().get(0);
        } else {
            curPlayer = (HBox) ((VBox) (bp.getRight())).getChildren().get(0);
        }
        curPlayer.getChildren().clear();
        for (Card c : p.getHand()) {
            StackPane cardImage = displayCard(c);
            curPlayer.getChildren().add(cardImage);
        }
        printBust(p, curPlayer);
    }

    // Add a bust message if player is over 21 and
    // does not already have a bust message
    public static void printBust(Person p, HBox curPlayer) {
        if (p.turnEnd && p.calculateHandValue() > 21) {
            p.bust = true;
            Label bustMessage = new Label("BUSTED!");
            if (p != game.getUser()) {
                VBox box = ((VBox) curPlayer.getParent());
                if (box.getChildren().size() < 2) {
                    box.getChildren().add(bustMessage);
                }
            } else {
                VBox box = ((VBox) (curPlayer.getParent().getParent()));
                if (box.getChildren().size() < 2) {
                    box.getChildren().addFirst(bustMessage);
                }
            }
            curPlayer.setAlignment(Pos.CENTER);
        }
    }

    // Renders a basic card representation
    public static StackPane displayCard(Card c) {
        Rectangle border = new Rectangle(100, 130);
        border.setStyle("-fx-fill: white; -fx-stroke: black; -fx-stroke-width: 3;");
        Label cardName = new Label(c.toString());
        if (c.getSuit() == Card.Suit.DIAMONDS || c.getSuit() == Card.Suit.HEARTS) {
            cardName.setTextFill(Color.RED);
        }
        return new StackPane(border, cardName);
    }

    public static void hideDealerCard() {
        BorderPane bp = (BorderPane) ((BorderPane) ui.getRoot()).getCenter();
        HBox dealerHand = (HBox) ((VBox) bp.getTop()).getChildren().getFirst();
        StackPane card = (StackPane) dealerHand.getChildren().getLast();
        ((Label) card.getChildren().getLast()).setText("HIDDEN CARD");
        ((Label) card.getChildren().getLast()).setTextFill(Color.BLACK);
    }

    public static Scene getUi() {
        return ui;
    }

    // Create the basic toolbar for the menus
    public static ToolBar makeToolbar() {
        Button mainMenu = new Button("Main Menu");
        mainMenu.setOnAction(e -> {
            saveBalance();
            GameManager.mainStage.setScene(GameManager.mainMenuScene);
        });
        Button logout = new Button("Logout");
        logout.setOnAction(e -> {
            saveBalance();
            GameManager.mainStage.setScene(GameManager.loginScene);
        });
        ToolBar toolBar = new ToolBar(mainMenu);
        toolBar.getItems().add(logout);

        return toolBar;
    }
    private static void saveBalance() {
    List<String> lines = new ArrayList<>();
    boolean userFound = false;
    int balance = game.getUser().getBalance();

    try (BufferedReader reader = new BufferedReader(new FileReader(HIGH_SCORE_FILE))) {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length == 3 && parts[0].equals(username)) {
                // Update the snake high score for the user
                parts[1] = String.valueOf(balance);
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
        lines.add(username + ","+ balance+ "," + "0");
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

    private static int loadBalance(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(HIGH_SCORE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(","); // Split the row into columns
                if (parts.length == 3 && parts[0].equals(username)) {
                    return Integer.parseInt(parts[1]);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error loading high score for user " + username + ": " + e.getMessage());
        }
        return 100; // Default
    }
}
