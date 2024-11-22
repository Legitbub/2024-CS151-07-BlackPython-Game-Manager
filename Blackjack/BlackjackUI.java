/**
 * Handles frontend display for Blackjack game
 */

package Blackjack;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.geometry.Pos;

public class BlackjackUI {
    private static Scene ui;
    private static GameState game = BJGame.getGame();

    // Initial layout of the Blackjack game
    public static void makeUI(double length, double width) {
        HBox dealer = new HBox();
        HBox cpu1 = new HBox();
        HBox cpu2 = new HBox();

        // Bottom of screen for user interaction
        TextField betEntry = new TextField();
        betEntry.setPromptText("$0");
        Button betButton = new Button("Bet");
        Label curBet = new Label("Bet: $" + game.getUser().bet);
        Button hit = new Button("Hit");
        Button stay = new Button("Stay");
        HBox cardActions = new HBox(50, hit, stay);
        HBox hand = new HBox();
        HBox betTop = new HBox(betEntry, betButton);
        VBox bet = new VBox(20, betTop, curBet, cardActions);
        HBox player = new HBox(hand, bet);

        // Force the field to be numeric only
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
            betEntry.clear();
            BJGame.getGame().getUser().setBet(betAmount);
            curBet.setText("Bet: $" + game.getUser().bet);
        });

        // Generate a new card for user when Hit is pressed
        hit.setOnAction(e -> {
            if (!game.getUser().turnEnd) {
                game.getUser().takeTurn();
                hand.getChildren().clear();
                displayHand(game.getUser());
            }
            if (game.getUser().turnEnd) {
                BJGame.playRestOfRound();
            }
        });

        stay.setOnAction(e -> {
            if (!game.getUser().turnEnd) {
                BJGame.playRestOfRound();
            }
            game.getUser().turnEnd = true;
        });

        Font labelFont = new Font("Comic Sans MS", 80);
        Label gameLabel = new Label("Blackjack,\nprobably");
        gameLabel.setFont(labelFont);

        BorderPane bp = new BorderPane();
        bp.setTop(dealer);
        dealer.setAlignment(Pos.CENTER);
        bp.setLeft(cpu1);
        bp.setRight(cpu2);
        bp.setBottom(player);
        player.setAlignment(Pos.CENTER);
        bp.setCenter(gameLabel);
        ui = new Scene(bp, length, width);
    }

    // Show the cards in each player's hand
    public static void displayHand(Person p) {
        for (Card c : p.hand) {
            StackPane cardImage = displayCard(c);
            BorderPane bp = (BorderPane) ui.getRoot();
            HBox curPlayer;
            if (p == game.getDealer()) {
                curPlayer = (HBox) (bp.getTop());
            } else if (p == game.getUser()) {
                curPlayer = (HBox) ((HBox) (bp.getBottom())).getChildren().get(0);
            } else if (p == game.getP1()) {
                curPlayer = (HBox) (bp.getLeft());
            } else {
                curPlayer = (HBox) (bp.getRight());
            }
            curPlayer.getChildren().add(cardImage);
        }
    }

    public static void displayPot(Person p) {

    }

    // Renders a basic card representation
    public static StackPane displayCard(Card c) {
        Rectangle border = new Rectangle(100, 130);
        border.setStyle("-fx-fill: white; -fx-stroke: black; -fx-stroke-width: 3;");
        Label cardName = new Label(c.toString());
        return new StackPane(border, cardName);
    }

    public static Scene getUi() {
        return ui;
    }
}
