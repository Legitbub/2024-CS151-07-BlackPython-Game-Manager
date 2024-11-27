package Snakes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Snake {

    private List<Segment> segments;
    private Direction direction;
    private Random randomDirection;
    private Random randomStartingPoint;
    private static final int SPAWN_LOCATION = 2;

    // Snake constructor with a random starting point for the snake
    public Snake(int gridWidth, int gridHeight) {
        segments = new ArrayList<>();
        this.randomStartingPoint = new Random();
        int centerX = gridWidth / 2;
        int centerY = gridHeight / 2;

        // Set a random starting position around the center of the grid each time a new game starts
        int randomStartX = centerX + randomStartingPoint.nextInt(2 * SPAWN_LOCATION + 1) - SPAWN_LOCATION;
        int randomStartY = centerY + randomStartingPoint.nextInt(2 * SPAWN_LOCATION + 1) - SPAWN_LOCATION;

        segments.add(new Segment(randomStartX, randomStartY));
        this.randomDirection = new Random();
        this.direction = Direction.values()[randomDirection.nextInt(Direction.values().length)];
    }

    // Set direction of snake and make sure that we cannot set new direction
    // of snake in the exact opposite direction that it is currently moving
    public void setDirection(Direction newDirection) {
        if ((this.direction == Direction.UP && newDirection == Direction.DOWN) ||
            (this.direction == Direction.DOWN && newDirection == Direction.UP) ||
            (this.direction == Direction.LEFT && newDirection == Direction.RIGHT) ||
            (this.direction == Direction.RIGHT && newDirection == Direction.LEFT)) {
            return;
        }

        this.direction = newDirection;
    }

    public Direction getDirection() {
        return direction;
    }

    //Move the snake by calculating the new position of the snake's head based on its current direction and adding to its head
    public void moveSnake() {
        Segment head = segments.get(0);
        int newX = head.getX();
        int newY = head.getY();
        switch (direction) {
            case UP:
                newY--;
                break;
            case DOWN:
                newY++;
                break;
            case LEFT:
                newX--;
                break;
            case RIGHT:
                newX++;
                break;
        }

        segments.add(0, new Segment(newX, newY));
        segments.remove(segments.size() - 1);
    }

    // Grow the snake by adding a tail segment to the end of the snake
    public void growSnake() {
        Segment tail = segments.get(segments.size() - 1);
        segments.add(new Segment(tail.getX(), tail.getY()));
    }

    public List<Segment> getSegments() {
        return segments;
    }
}
