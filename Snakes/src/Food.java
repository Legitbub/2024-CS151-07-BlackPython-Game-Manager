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
    public void randomNewFoodLocation(){
        this.foodX = randomLocation.nextInt(gridWith);
        this.foodY = randomLocation.nextInt(gridHeight);
    }

    public int getfoodX(){
        return foodX;
    }
    public int getfoodY(){
        return foodY;
    }

    

}
