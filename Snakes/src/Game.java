package Snakes.src;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {
    public enum Dir {
        left, right, up, down
    }

    private int width;
    private int height;
    private int cornersize = 25;
    private List<Corner> snake;
    private Dir direction;
    private boolean gameOver;
    private int foodX;
    private int foodY;
    private int score;
    private int speed;
    private Random rand;

    public Game(int width, int height) {
        this.width = width;
        this.height = height;
        this.snake = new ArrayList<>();
        this.rand = new Random();
        reset();
    }

    public void reset() {
        snake.clear();
        snake.add(new Corner(width / 2, height / 2));
        direction = Dir.values()[rand.nextInt(4)];
        gameOver = false;
        score = 0;
        speed = 5;
        newFood();
    }

    public Dir getDirection() {
        return direction;
    }

    public void setDirection(Dir direction) {
        this.direction = direction;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public int getSpeed() {
        return speed;
    }

    public void tick(GraphicsContext gc) {
        if (gameOver) {
            showGameOverMessage(gc);
            return;
        }

        // Move the snake
        for (int i = snake.size() - 1; i >= 1; i--) {
            snake.get(i).x = snake.get(i - 1).x;
            snake.get(i).y = snake.get(i - 1).y;
        }

        switch (direction) {
            case up -> snake.get(0).y--;
            case down -> snake.get(0).y++;
            case left -> snake.get(0).x--;
            case right -> snake.get(0).x++;
        }

        // Check for collisions
        if (snake.get(0).x < 0 || snake.get(0).x >= width || snake.get(0).y < 0 || snake.get(0).y >= height) {
            gameOver = true;
        }

        for (int i = 1; i < snake.size(); i++) {
            if (snake.get(0).x == snake.get(i).x && snake.get(0).y == snake.get(i).y) {
                gameOver = true;
            }
        }

        // Eat food
        if (snake.get(0).x == foodX && snake.get(0).y == foodY) {
            snake.add(new Corner(-1, -1));
            newFood();
            score++;
            speed++;
        }

        // Draw the game
        drawGame(gc);
    }

    public void showPausedMessage(GraphicsContext gc) {
        gc.setFill(Color.YELLOW);
        gc.setFont(new Font("", 50));
        gc.fillText("PAUSED", width * cornersize / 2 - 100, height * cornersize / 2);
    }

    private void showGameOverMessage(GraphicsContext gc) {
        gc.setFill(Color.RED);
        gc.setFont(new Font("", 50));
        gc.fillText("GAME OVER", width * cornersize / 2 - 150, height * cornersize / 2 - 50);
        gc.setFont(new Font("", 30));
        gc.fillText("Final Score: " + score, width * cornersize / 2 - 100, height * cornersize / 2);
        gc.fillText("Press 'R' to Restart", width * cornersize / 2 - 150, height * cornersize / 2 + 50);
    }

    private void drawGame(GraphicsContext gc) {
        // Background
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width * cornersize, height * cornersize);

        // Score
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("", 30));
        gc.fillText("Score: " + score, 10, 30);

        // Draw food
        gc.setFill(Color.RED);
        gc.fillOval(foodX * cornersize, foodY * cornersize, cornersize, cornersize);

        // Draw snake
        for (Corner c : snake) {
            gc.setFill(Color.LIGHTGREEN);
            gc.fillRect(c.x * cornersize, c.y * cornersize, cornersize - 1, cornersize - 1);
        }
    }

    private void newFood() {
        while (true) {
            foodX = rand.nextInt(width);
            foodY = rand.nextInt(height);

            boolean onSnake = false;
            for (Corner c : snake) {
                if (c.x == foodX && c.y == foodY) {
                    onSnake = true;
                    break;
                }
            }
            if (!onSnake) {
                break;
            }
        }
    }

    private static class Corner {
        int x, y;

        public Corner(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
