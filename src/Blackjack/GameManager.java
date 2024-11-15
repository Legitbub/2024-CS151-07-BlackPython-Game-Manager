package Blackjack;

import javafx.application.Application;
import javafx.stage.Stage;

public class GameManager extends Application {
    private final double DEFAULT_WINDOW_LENGTH = 800;
    private final double DEFAULT_WINDOW_WIDTH = 600;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        BlackjackUI blackjack = new BlackjackUI(DEFAULT_WINDOW_LENGTH, DEFAULT_WINDOW_WIDTH);
        stage.setTitle("Let's play some Games!");
        stage.setScene(blackjack.getUi());
        stage.show();
    }
}
