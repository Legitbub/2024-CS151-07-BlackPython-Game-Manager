import Blackjack.BlackjackUI;
import Blackjack.BJGame;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class GameManager extends Application {
    private final double DEFAULT_WINDOW_LENGTH = 1240;
    private final double DEFAULT_WINDOW_WIDTH = 780;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Button blackjackLaunch = new Button("Blackjack");
        Button snakeLaunch = new Button("Snake");
        HBox gameButtons = new HBox(blackjackLaunch, snakeLaunch);
        StackPane gameSelect = new StackPane(gameButtons);
        gameButtons.setAlignment(Pos.CENTER);

        Scene mainMenu = new Scene(gameSelect, DEFAULT_WINDOW_LENGTH, DEFAULT_WINDOW_WIDTH);
        stage.setTitle("Let's play some Games!");
        stage.setScene(mainMenu);
        stage.show();

        blackjackLaunch.setOnAction(e -> {
            BlackjackUI.makeUI(DEFAULT_WINDOW_LENGTH,
                    DEFAULT_WINDOW_WIDTH);
            BJGame.playRound();
            stage.setScene(BlackjackUI.getUi());
        });

        snakeLaunch.setOnAction(e -> {

        });
    }
}
