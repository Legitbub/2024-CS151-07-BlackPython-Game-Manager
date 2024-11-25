package Blackjack;

import java.util.ArrayList;
import java.util.Arrays;

public class GameState {
    private Deck deck;
    private User user;
    private Dealer dealer;
    private AutomatedPlayer p1;
    private AutomatedPlayer p2;
    private ArrayList<Person> people;
    private int currentPlayer;
    private boolean roundEnd = false;

    //default start to new game. All players have balances of 100 and no cards. User will always go first.
    public GameState() {
        deck = new Deck();
        user = new User(this);
        dealer = new Dealer(this);
        p1 = new AutomatedPlayer(this);
        p2 = new AutomatedPlayer(this);
        people = new ArrayList<>(Arrays.asList(user, p1, p2, dealer));
        //currentPlayer = 0;
    }

    //getters and setters
    public Deck getDeck() {
        return deck;
    }

    public User getUser() {
        return user;
    }

    public Dealer getDealer() {
        return dealer;
    }

    public AutomatedPlayer getP1() {
        return p1;
    }

    public AutomatedPlayer getP2() {
        return p2;
    }

    public ArrayList<Person> getPeople() {
        return people;
    }

    public ArrayList<Player> getPlayers() {
        return new ArrayList<>(Arrays.asList((Player) people.get(0),(Player) people.get(1),
            (Player) people.get(2)));
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public boolean isRoundEnd() {
        return roundEnd;
    }

    public void endRound() {
        roundEnd = true;
    }

    public void startRound() {
        roundEnd = false;
    }
}
