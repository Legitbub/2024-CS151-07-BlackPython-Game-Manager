/**
 * Handles backend logic for Blackjack game
 */

package Blackjack;

public class BJGame {
    private static GameState game = new GameState();

    // Go through one round: each player makes a bet (handled by constructor),
    // everyone receives their cards, everyone takes their turn
    public static void playRound() {
        for (Person p : game.getPeople()) {
            p.resetHand();
        }
        deal();
    }

    // The round will continue after the user's turn is over (they bust or stay)
    public static void playRestOfRound() {
        takeTurns();
        calculateEarnings();
        game.endRound();
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

    // CPU players go after the User
    // User turn is over when either they bust or Stay button is clicked
    private static void takeTurns() {
        for(Person p : game.getPeople()) {
            if (!(p instanceof User)) {
                while (!p.turnEnd) {
                    p.takeTurn();
                }
            }
            BlackjackUI.displayHand(p);
        }
    }    
    
    private static void calculateEarnings() {
        Dealer dealer = game.getDealer();
        for(Player p : game.getPlayers()) {
            int playerHandValue = p.calculateHandValue();
            int dealerHandValue = dealer.calculateHandValue();
            boolean isBlackjack = (p.getHand().size() == 2 && playerHandValue == 21);
            boolean dBlackjack = (dealer.getHand().size() == 2 && dealerHandValue == 21);
            if (isBlackjack && !dBlackjack) {
                //if blackjack, player earns double their bet
                dealer.pay(p, p.getBet() * 2);
            } else if ((dealer.bust && !(p.bust)) ||
                    (!(dealer.bust) && !(p.bust) &&
                            (playerHandValue > dealerHandValue))) {
                //player's hand is higher than dealer's- player earns their bet amount
                dealer.pay(p, p.getBet());
            } else if (!dealer.bust && p.bust ||
                    !dealer.bust && playerHandValue < dealerHandValue) {
                //player's hand is lower than dealer's- player loses their bet amount
                dealer.charge(p, p.getBet());
            }
        }
    }
    public static GameState getGame() {
        return game;
    }
}
