package Blackjack;

public class User extends Player {
    public User(GameState game){
        super(game);
    }
    
    //not taking user input for now- implementation is the same as automated players
    @Override
    public void takeTurn(){
        if(calculateHandValue() <= 16){
            hit();
        }
    }
}
