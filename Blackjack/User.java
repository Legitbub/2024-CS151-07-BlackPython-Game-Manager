package Blackjack;

public class User extends Player {
    public User(GameState game) {
        super(game);
    }
    
    // Called when User clicks the hit button
    @Override
    public void takeTurn() {
        hit();
        if (calculateHandValue() > 21) {
            turnEnd = true;
            bust = true;
        }
    }
}
