
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UI extends Application {
    private static final int WIDTH = 20;
    private static final int HEIGHT = 20;
    private static final int CORNER_SIZE = 25;
    private Game game;
    private boolean paused = false;

    @Override
    public void start(Stage primaryStage) {
        game = new Game(WIDTH, HEIGHT);

        VBox root = new VBox();
        Canvas canvas = new Canvas(WIDTH * CORNER_SIZE, HEIGHT * CORNER_SIZE);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);

        new AnimationTimer() {
            long lastTick = 0;

            @Override
            public void handle(long now) {
                if (lastTick == 0) {
                    lastTick = now;
                    game.tick(gc);
                    return;
                }

                if (now - lastTick > 1000000000 / game.getSpeed()) {
                    lastTick = now;
                    if (!paused && !game.isGameOver()) {
                        game.tick(gc);
                    } else if (paused) {
                        game.showPausedMessage(gc);
                    }
                }
            }
        }.start();

        Scene scene = new Scene(root, WIDTH * CORNER_SIZE, HEIGHT * CORNER_SIZE);

        // Keyboard controls
        scene.setOnKeyPressed(this::handleKeyPress);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Snake Game");
        primaryStage.show();
    }

    private void handleKeyPress(KeyEvent event) {
        KeyCode keyCode = event.getCode();

        if (keyCode == KeyCode.UP && game.getDirection() != Game.Dir.down) {
            game.setDirection(Game.Dir.up);
        } else if (keyCode == KeyCode.DOWN && game.getDirection() != Game.Dir.up) {
            game.setDirection(Game.Dir.down);
        } else if (keyCode == KeyCode.LEFT && game.getDirection() != Game.Dir.right) {
            game.setDirection(Game.Dir.left);
        } else if (keyCode == KeyCode.RIGHT && game.getDirection() != Game.Dir.left) {
            game.setDirection(Game.Dir.right);
        } else if (keyCode == KeyCode.ESCAPE) {
            paused = !paused;
        } else if (game.isGameOver() && keyCode == KeyCode.R) {
            game.reset();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
