package Blackjack;

public class Dealer extends Person {
    public Dealer(GameState game){
        super(game);
    }

    //recieve money from another player
    public void charge(Person p, int amount){
        p.adjustBalance(-amount);
        adjustBalance(amount);
    }

    //give money to another player
    public void pay(Person p, int amount){
        p.adjustBalance(amount);
        adjustBalance(-amount);
    }

    @Override
    public void takeTurn() {
        if(calculateHandValue() <= 17){
            hit();
        }
    }
    
}
