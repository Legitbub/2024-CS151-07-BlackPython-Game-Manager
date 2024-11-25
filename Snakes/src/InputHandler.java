import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class InputHandler {
    
    private Snake snake;

    public InputHandler(Snake snake){
        this.snake = snake;
    }

    public void handleKeyPress(KeyEvent event){
        KeyCode keyCode = event.getCode();

        switch(keyCode){
            case UP:
                snake.setDirection(Direction.UP);
                //System.out.println("UP");
                break;
            case DOWN:
                snake.setDirection(Direction.DOWN);
                //System.out.println("DOWN");
                break;
            case LEFT:
                snake.setDirection(Direction.LEFT);
                //System.out.println("LEFT");
                break;
            case RIGHT:
                snake.setDirection(Direction.RIGHT);
                //System.out.println("RIGHT");
                break;
            default:
                break;
        }

    }
}
