package Blackjack;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;

public class BlackjackUI {
    private Blackjack.GameState game = new Blackjack.GameState();
    private Scene ui;

    public BlackjackUI(double length, double width) {
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

    public void displayHand(Person p) {

    }

    public void displayPot(Person p) {

    }

    public Rectangle displayCard(Card c) {
        Rectangle card = new Rectangle();
        return card;
    }

    public Scene getUi() {
        return ui;
    }
}
