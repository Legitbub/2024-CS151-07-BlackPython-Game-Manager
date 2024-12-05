package Blackjack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

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

    // Create a gamestate from the inputted save data string
    public GameState(int balance, int bet, String userHand, String p1Hand,
                     String p2Hand, String dealHand, String[] cards) throws Exception {
        user = new User(this);
        user.setBalance(balance);
        user.setBet(bet);
        ArrayList<Card> userCards = new ArrayList<>();
        String[] uCardStrings = userHand.split("UC");
        for (String s : uCardStrings) {
            Card.Value value = Card.Value.valueOf(s.split(" ")[0]);
            Card.Suit suit = Card.Suit.valueOf(s.split(" ")[1]);
            userCards.add(new Card(suit, value));
        }
        user.setHand(userCards);

        p1 = new AutomatedPlayer(this);
        ArrayList<Card> p1Cards = new ArrayList<>();
        String[] cardStrings1 = p1Hand.split("P\\d");
        for (String s : cardStrings1) {
            Card.Value value = Card.Value.valueOf(s.split(" ")[0]);
            Card.Suit suit = Card.Suit.valueOf(s.split(" ")[1]);
            p1Cards.add(new Card(suit, value));
        }
        p1.setHand(p1Cards);

        p2 = new AutomatedPlayer(this);
        ArrayList<Card> p2Cards = new ArrayList<>();
        String[] cardStrings2 = p2Hand.split("P\\d");
        for (String s : cardStrings2) {
            Card.Value value = Card.Value.valueOf(s.split(" ")[0]);
            Card.Suit suit = Card.Suit.valueOf(s.split(" ")[1]);
            p2Cards.add(new Card(suit, value));
        }
        p2.setHand(p2Cards);

        dealer = new Dealer(this);
        ArrayList<Card> dealerCards = new ArrayList<>();
        String[] dCardStrings = dealHand.split("DL");
        for (String s : dCardStrings) {
            Card.Value value = Card.Value.valueOf(s.split(" ")[0]);
            Card.Suit suit = Card.Suit.valueOf(s.split(" ")[1]);
            dealerCards.add(new Card(suit, value));
        }
        dealer.setHand(dealerCards);

        Stack<Card> remainingCards = new Stack<>();
        for (String s : cards) {
            Card.Value value = Card.Value.valueOf(s.split(" ")[0]);
            Card.Suit suit = Card.Suit.valueOf(s.split(" ")[1]);
            remainingCards.add(new Card(suit, value));
        }
        Deck savedDeck = new Deck();
        savedDeck.setDeck(remainingCards);
        deck = savedDeck;

        people = new ArrayList<>(Arrays.asList(user, p1, p2, dealer));
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
