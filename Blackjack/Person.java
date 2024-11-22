package Blackjack;

import Blackjack.Card.Value;
import java.util.ArrayList;
import java.util.Collections;

public abstract class Person {
    private int balance;
    private ArrayList<Card> hand;
    GameState game;
    protected boolean turnEnd = false;
    protected boolean bust = false;

    public Person(GameState game) {
        this.game = game;
        balance = 0;
        hand = new ArrayList<>();
    }

    public Person(GameState game, int balance, ArrayList<Card> hand) {
        this.game = game;
        this.balance = balance;
        this.hand = hand;
    }

    //getters and setters
    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public void setHand (ArrayList<Card> hand) {
        this.hand = hand;
    }

    // Called in between rounds to start a new round of blackjack
    public void resetHand() {
        hand.clear();
    }

    //method for player to take their turn
    public abstract void takeTurn();
    
    //make changes to player's balance
    public void adjustBalance(int amount) {
        balance += amount;
    }

    //player hits, drawing a card
    public void hit() {
        hand.add(game.getDeck().draw());
    }

    //sum values of cards in hand
    public int calculateHandValue() {
        int value = 0;
        for(Card c : hand) {
            if (c.getValue() != Value.ACE) {
                value += c.getValue().getNumericValue();
            } else {
                //for ace- 11 if the value would still be less than 21, 1 otherwise
                if (value < 10) {
                    value += 11;
                } else {
                    value++;
                }
            }
        }
        return value;
    }



}
