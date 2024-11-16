package Blackjack;
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
        TWO(2),
        THREE(3),
        FOUR(4),
        FIVE(5),
        SIX(6),
        SEVEN(7),
        EIGHT(8),
        NINE(9),
        TEN(10),
        JACK(11),
        QUEEN(12),
        KING(13),
        ACE(1);

        private final int numericValue;

        Value(int numericValue) {
            this.numericValue = numericValue;
        }

        public int getNumericValue() {
            return numericValue;
        }
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
