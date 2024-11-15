package Blackjack;

import Blackjack.Card.Value;
import java.util.ArrayList;

public abstract class Person{
    protected float balance;
    protected ArrayList<Card> hand;
    protected GameState game;

    public Person(GameState game){
        this.game = game;
        balance = 0;
        hand = new ArrayList<>();
    }

    public Person(GameState game, float balance, ArrayList<Card> hand){
        this.game = game;
        this.balance = balance;
        this.hand = hand;
    }

    //getters and setters
    public float getBalance(){
        return balance;
    }

    public void setBalance(float balance){
        this.balance = balance;
    }

    public ArrayList<Card> getHand(){
        return hand;
    }

    public void setHand (ArrayList<Card> hand){
        this.hand = hand;
    }

    //method for player to take their turn
    public abstract void takeTurn();
    
    //make changes to player's balance
    public void adjustBalance(float amount){
        balance += amount;
    }

    //player hits, drawing a card
    public void hit(){
        hand.add(game.getDeck().draw());
    }

    //sum values of cards in hand
    public int calculateHandValue(){
        int value = 0;
        for(Card c : hand){
            if(c.getValue() != Value.ACE){
                value += c.getValue().getNumericValue();
            }else{
                //for ace- 11 if the value would still be less than 21, 1 otherwise
                if(value < 10){
                    value += 11;
                }else{
                    value++;
                }
            }
        }
        return value;
    }



}
