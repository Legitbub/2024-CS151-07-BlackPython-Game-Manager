/*
package Blackjack;

public class Main {
    public static GameState game = new GameState();
    public static void main(String[] args) {

    }

    //go through one round: each player makes a bet,  everyone recieves their cards, everyone takes their turn
    private void playRound(){
        makeBets();
        deal();
        takeTurns();
        calculateEarnings();
        game.getDeck().newDeck();
    }

    */
/* each player (excluding the dealer) makes a bet
    Note: I'm currently unsure how were going to get the user input. For now as a placeholder I'm just setting the
    bets to a constant value, but this will need to be changed later *//*

    private void makeBets(){
        for(Player p : game.getPlayers()){
            p.setBet(10);
        }
    }

    //called at round start, deal to each person
    private void deal(){
        //in blackjack, each player is dealt one card at a time. unnecessary here but added for realism
        for(int i = 0; i < 2; i++){
            for(Person p : game.getPeople()){
                p.getHand().add(game.getDeck().draw());
            }
        }
    }

    //each person takes their turn
    private void takeTurns(){
        for(Person p : game.getPeople()){
            p.takeTurn();
        }
    }    
    
    private void calculateEarnings(){
        Dealer dealer = game.getDealer();
        for(Player p : game.getPlayers()){
            int playerHandValue = p.calculateHandValue();
            int dealerHandValue = game.getDealer().calculateHandValue();
            if(p.getHand().size() == 2 && playerHandValue == 21){
                //if blackjack, player earns double their bet
                dealer.pay(p, p.getBet() * 2);
            }else if (playerHandValue > dealerHandValue){
                //player's hand is higher than dealer's- player earns their bet amount
                dealer.pay(p, p.getBet());
            }else{
                //player's hand is lower than dealer's- player loses their bet amount
                dealer.charge(p, p.getBet());
            }
        }
    }
    
}
*/
