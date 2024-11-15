import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;

public class BlackjackUI extends Application {

    private final double DEFAULT_WINDOW_LENGTH = 800;
    private final double DEFAULT_WINDOW_WIDTH = 600;
    @Override
    public void start(Stage stage) {
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

        Scene scene = new Scene(bp, DEFAULT_WINDOW_LENGTH, DEFAULT_WINDOW_WIDTH);


        stage.setTitle("Blackjack");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
