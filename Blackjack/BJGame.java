package Blackjack;

public class BJGame {
    private static GameState game = new GameState();

    //go through one round: each player makes a bet, everyone receives their cards, everyone takes their turn
    public static void playRound() {
        for (Person p : game.getPeople()) {
            p.resetHand();
        }
        makeBets();
        deal();
        takeTurns();
        calculateEarnings();
        game.getDeck().newDeck();
    }

/* each player (excluding the dealer) makes a bet
    Note: I'm currently unsure how were going to get the user input. For now as a placeholder I'm just setting the
    bets to a constant value, but this will need to be changed later */

    private static void makeBets() {
        for(Player p : game.getPlayers()) {
            if (!(p instanceof User))
            p.setBet(10);
        }
    }

    //called at round start, deal to each person
    private static void deal() {
        //in blackjack, each player is dealt one card at a time. unnecessary here but added for realism
        for(int i = 0; i < 2; i++){
            for(Person p : game.getPeople()) {
                p.getHand().add(game.getDeck().draw());
            }
        }
        for(Person p : game.getPeople()) {
            BlackjackUI.displayHand(p);
        }
    }

    //each person takes their turn
    private static void takeTurns(){
        for(Person p : game.getPeople()){
            p.takeTurn();
        }
    }    
    
    private static void calculateEarnings(){
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
    public static GameState getGame() {
        return game;
    }
}