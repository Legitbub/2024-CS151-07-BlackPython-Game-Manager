package Blackjack;

import java.util.Stack;
import java.util.Collections;

public class Deck {
    private Stack<Card> deck;
    
    public Deck(){
        newDeck();
    }

    public Deck(Stack<Card> deck){
        this.deck = deck;
    }

    //getter for deck
    public Stack<Card> getDeck(){
        return deck;
    }

    //setter for deck
    public void setDeck(Stack<Card> deck){
        this.deck = deck;
    }
   
    //set deck attribute to a full shuffled deck 
    public void newDeck() {
        Stack<Card> newDeck = new Stack<>();
        for(Card.Suit s : Card.Suit.values()){
            for(Card.Value v : Card.Value.values())
                newDeck.push(new Card(s, v));
        }
        Collections.shuffle(newDeck);
        deck = newDeck;
    }

    //draws a card from the deck
    public Card draw(){
        return deck.pop();
    }

    @Override
    public String toString(){
        return deck.toString();
    }



}
