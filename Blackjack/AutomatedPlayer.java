package Blackjack;

public class AutomatedPlayer extends Player {
    public AutomatedPlayer(GameState game) {
        super(game);
    }

    //when an automated player takes its turn, it hits on less than or equal to 16, and stays otherwise
    @Override
    public void takeTurn() {
        if (calculateHandValue() <= 16) {
            hit();
        } else {
            turnEnd = true;
        }
    }
}
