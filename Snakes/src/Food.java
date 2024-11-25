import java.util.List;
import java.util.Random;

public class Food {

    private int foodX;
    private int foodY;
    private int gridWith;
    private int gridHeight;
    private Random randomLocation;

    public Food(int gridWith, int gridHeight){
        this.gridWith = gridWith;
        this.gridHeight = gridHeight;
        this.randomLocation = new Random();
    }

    // Generate food at random location
    // Takes in List of Segment from snake to ensure that the food does not spawn directly on its body
    public void randomNewFoodLocation(List<Segment> snakeSegments) {
        boolean isOnSnake;
        do {
            // Generate new random coordinates for food
            foodX = randomLocation.nextInt(gridWith);
            foodY = randomLocation.nextInt(gridHeight);

            // Check if the new food position overlaps with any snake segment and generate new one if it does
            isOnSnake = false;
            for (Segment segment : snakeSegments) { 
                if (segment.getX() == foodX && segment.getY() == foodY) {
                    isOnSnake = true;
                    break; 
                }
            }
        } while (isOnSnake); 
    }

    public int getfoodX(){
        return foodX;
    }

    public int getfoodY(){
        return foodY;
    }
}
