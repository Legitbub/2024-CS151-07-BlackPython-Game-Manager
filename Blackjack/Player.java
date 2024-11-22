package Blackjack;

import java.util.ArrayList;

public abstract class Player extends Person {
    private int bet;

    public Player(GameState game) {
        super(game);
        bet = 0;
    }

    public Player(GameState game, int balance, ArrayList<Card> hand, int bet) {
        super(game, balance, hand);
        this.bet = bet;
    }

    //getter for bet
    public int getBet() {
        return bet;
    }

    //setter for bet
    public void setBet(int bet) {
        this.bet = bet;
    }
}
