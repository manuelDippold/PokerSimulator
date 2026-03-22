package com.yotilla.poker.card;

import java.util.Objects;

/**
 * Description: One card of the French deck.
 *
 * <br>
 * Date: 20.12.2020
 *
 * @author Manuel
 *
 */
public record Card(CardSuit cardSuit, CardValue cardValue) implements Comparable<Card> {

    /**
     * Compares two cards by numerical value. Suit is irrelevant for ordering.
     *
     * @param otherCard Card to compare this one to
     * @throws NullPointerException if otherCard is null
     */
    @Override
    public int compareTo(Card otherCard) {
        Objects.requireNonNull(otherCard, "Cannot compare a card to null.");
        return Integer.compare(cardValue.getNumericalValue(), otherCard.cardValue().getNumericalValue());
    }

    @Override
    public String toString() {
        String value = cardValue != null ? cardValue.name() : "?";
        String suit = cardSuit != null ? cardSuit.name() : "?";
        return value + " of " + suit;
    }
}
