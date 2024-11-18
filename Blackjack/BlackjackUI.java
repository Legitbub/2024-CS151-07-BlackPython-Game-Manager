package Blackjack;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;

public class BlackjackUI {
    private static Scene ui;

    public static void makeUI(double length, double width) {
        HBox dealer = new HBox();
        VBox cpu1 = new VBox();
        VBox cpu2 = new VBox();

        // Bottom of screen for user interaction
        TextField betEntry = new TextField();
        betEntry.setPromptText("$0");
        Button betButton = new Button("Bet");
        HBox hand = new HBox();
        HBox bet = new HBox(betEntry, betButton);
        HBox player = new HBox(240, hand, bet);

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
        });

        Font labelFont = new Font("Comic Sans MS", 80);
        Label gameLabel = new Label("Blackjack,\nprobably");
        gameLabel.setFont(labelFont);

        BorderPane bp = new BorderPane();
        bp.setTop(dealer);
        bp.setLeft(cpu1);
        bp.setRight(cpu2);
        bp.setBottom(player);
        bp.setCenter(gameLabel);
        ui = new Scene(bp, length, width);
    }

    public static void displayHand(Person p) {
        for (Card c : p.hand) {
            StackPane cardImage = displayCard(c);
            if (p == BJGame.getGame().getDealer()) {
                ((HBox)((BorderPane)(ui.getRoot())).getTop()).getChildren().add(cardImage);
            } else if (p == BJGame.getGame().getUser()) {
                ((HBox)((BorderPane)(ui.getRoot())).getBottom()).getChildren().add(cardImage);
            } else if (p == BJGame.getGame().getP1()) {
                ((VBox)((BorderPane)(ui.getRoot())).getLeft()).getChildren().add(cardImage);
            } else {
                ((VBox)((BorderPane)(ui.getRoot())).getRight()).getChildren().add(cardImage);
            }
        }
    }

    public static void displayPot(Person p) {

    }

    // Renders a basic card representation
    public static StackPane displayCard(Card c) {
        Rectangle border = new Rectangle(100, 130);
        border.setStyle("-fx-fill: white; -fx-stroke: black; -fx-stroke-width: 3;");
        Label cardName = new Label(c.toString());
        StackPane card = new StackPane(border, cardName);
        return card;
    }

    public static Scene getUi() {
        return ui;
    }
}
