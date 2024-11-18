import java.util.List;

public class Snake {
    
    private List<Segment> segments;
    private Direction direction;

    public Snake(){

    }

    public void setDirection(Direction dir){

    }

    public void moveSnake(){

    }

    public void growSnake(){

    }

    public static class Segment{
        private int x;
        private int y;

        public Segment(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

}
