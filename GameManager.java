import Blackjack.BlackjackUI;
import Blackjack.BJGame;
import javafx.application.Application;
import javafx.stage.Stage;

public class GameManager extends Application {
    private final double DEFAULT_WINDOW_LENGTH = 1240;
    private final double DEFAULT_WINDOW_WIDTH = 780;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        BlackjackUI.makeUI(DEFAULT_WINDOW_LENGTH,
                DEFAULT_WINDOW_WIDTH);
        BJGame.playRound();

        stage.setTitle("Let's play some Games!");
        stage.setScene(BlackjackUI.getUi());
        stage.show();
    }
}
