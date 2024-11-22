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
        HBox hand = new HBox();
        HBox betTop = new HBox(betEntry, betButton);
        VBox bet = new VBox(20, betTop, moneyStats, cardActions);
        HBox player = new HBox(100, hand, bet);
        VBox playerArea = new VBox(player);

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
            curBet.setText("Bet: $" + game.getUser().getBet());
        });

        // Generate a new card for user when Hit is pressed
        hit.setOnAction(e -> {
            if (!game.getUser().turnEnd) {
                game.getUser().takeTurn();
                displayHand(game.getUser());
            }
            if (game.getUser().turnEnd) {
                BJGame.playRestOfRound();
            }
            bankroll.setText("Bankroll: $" + game.getUser().getBalance());
        });

        // End the User's turn; let other players take turns
        stay.setOnAction(e -> {
            if (!game.getUser().turnEnd) {
                BJGame.playRestOfRound();
            }
            game.getUser().turnEnd = true;
            bankroll.setText("Bankroll: $" + game.getUser().getBalance());
        });

        Font labelFont = new Font("Comic Sans MS", 70);
        Label gameLabel = new Label("Blackjack,\nprobably");
        gameLabel.setFont(labelFont);

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
        ui = new Scene(bp, length, width);
    }

    // Show the cards in each player's hand
    public static void displayHand(Person p) {
        BorderPane bp = (BorderPane) ui.getRoot();
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

        // Add a bust message if player is over 21 and
        // does not already have a bust message
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
