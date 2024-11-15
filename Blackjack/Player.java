package Blackjack;

import java.util.ArrayList;

public abstract class Player extends Person {
    protected float bet;

    public Player(GameState game){
        super(game);
        bet = 0;
    }

    public Player(GameState game, float balance, ArrayList<Card> hand, float bet){
        super(game, balance, hand);
        this.bet = bet;
    }

    //getter for bet
    public float getBet(){
        return bet;
    }

    //setter for bet
    public void setBet(float bet){
        this.bet = bet;
    }
}
