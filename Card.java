public class Card {
    private Suit suit;
    private Value value;

    public Suit getSuit() {
        return suit;
    }

    public Value getValue() {
        return value;
    }

    enum Suit {
        HEARTS,
        DIAMONDS,
        CLUBS,
        SPADES
    }

    enum Value {
        TWO,
        THREE,
        FOUR,
        FIVE,
        SIX,
        SEVEN,
        EIGHT,
        NINE,
        TEN,
        JACK,
        QUEEN,
        KING,
        ACE
    }

    public Card(Suit suit, Value value) {
        this.suit = suit;
        this.value = value;
    }

    @Override
    public String toString() {
        return value + "of " + suit;
    }
}
