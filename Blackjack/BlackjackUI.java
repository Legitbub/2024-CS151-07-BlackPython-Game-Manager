/**
 * Handles frontend display for Blackjack game
 */

package Blackjack;

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
import java.util.ArrayList;
import java.util.Stack;

import Manager.GameManager;

public class BlackjackUI {
    private static Scene ui;
    private static GameState game = BJGame.getGame();

    // Initial layout of the Blackjack game
    public static void makeUI(double length, double width) {
        // Screen areas of each player
        HBox dealerHand = new HBox();
        VBox dealerArea = new VBox(dealerHand);
        HBox cpu1Hand = new HBox();
        VBox cpu1Area = new VBox(cpu1Hand);
        HBox cpu2Hand = new HBox();
        VBox cpu2Area = new VBox(cpu2Hand);

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
            if (betAmount > game.getUser().getBalance()) {

            }
            betEntry.clear();
            game.getUser().setBet(betAmount);
            curBet.setText("Bet: $" + game.getUser().getBet());
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

        // Button to save the state of the game into a string
        Button save = new Button("Save");
        save.setOnAction(e -> {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent saveString = new ClipboardContent();
            StringBuilder str = new StringBuilder();
            str.append(game.getUser().getBalance()).append(",");
            str.append(game.getUser().getBet()).append("UC");
            for (Card c : game.getUser().getHand()) {
                str.append(c.getValue() + " " + c.getSuit() + "UC");
            }
            str.append("P1");
            for (Card c : game.getP1().getHand()) {
                str.append(c.getValue() + " " + c.getSuit() + "P1");
            }
            str.append("P2");
            for (Card c : game.getP2().getHand()) {
                str.append(c.getValue() + " " + c.getSuit() + "P2");
            }
            str.append("DL");
            for (Card c : game.getDealer().getHand()) {
                str.append(c.getValue() + " " + c.getSuit() + "DL");
            }
            str.append("DCK");
            Stack<Card> deckCopy = (Stack<Card>) game.getDeck().getDeck().clone();
            while (!deckCopy.isEmpty()) {
                Card c = deckCopy.pop();
                str.append(c.getValue() + " " + c.getSuit() + "DCK");
            }
            saveString.putString(str.toString());
            clipboard.setContent(saveString);
        });

        Button mainMenu = new Button("Main Menu");
        mainMenu.setOnAction(e -> {
            GameManager.mainStage.setScene(GameManager.mainMenuScene);
        });
        Button logout = new Button("logout");
        logout.setOnAction(e -> {
            GameManager.mainStage.setScene(GameManager.loginScene);
        });
        ToolBar toolBar = new ToolBar(mainMenu);
        toolBar.getItems().add(logout);
        toolBar.getItems().add(save);

        BorderPane b = new BorderPane();
        b.setTop(toolBar);
        b.setCenter(bp);
        ui = new Scene(b, length, width);
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
}
