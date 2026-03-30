package com.yotilla.poker;

import com.yotilla.poker.card.Card;
import com.yotilla.poker.card.CardSuit;
import com.yotilla.poker.card.CardValue;
import com.yotilla.poker.error.PokerParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CardParserTest {

    private CardParser sut;

    @BeforeEach
    void setUp() {
        sut = new CardParser();
    }

    @Test
    void parseCardsThrowsExceptionOnNullAndEmpty() {
        assertThrows(PokerParseException.class, () -> sut.parseCards(null), "Exception expected on null");
        assertThrows(PokerParseException.class, () -> sut.parseCards(""), "Exception expected on empty string");
    }

    @Test
    void parseCardThrowsExceptionOnInvalidString() {
        assertThrows(PokerParseException.class, () -> sut.parseCard(null), "Exception expected on null");
        assertThrows(PokerParseException.class, () -> sut.parseCard(""), "Exception expected on empty string");
        assertThrows(PokerParseException.class, () -> sut.parseCard("A"), "Exception expected on one character");
        assertThrows(PokerParseException.class, () -> sut.parseCard("AS9"), "Exception expected on three characters");
    }

    @Test
    void parseCardThrowsExceptionOnNonCardString() {
        assertThrows(PokerParseException.class, () -> sut.parseCard("ZZ"), "Exception expected on ZZ");
        assertThrows(PokerParseException.class, () -> sut.parseCard("3Z"), "Exception expected on 3Z");
        assertThrows(PokerParseException.class, () -> sut.parseCard("ZC"), "Exception expected on ZC");
    }

    @Test
    void recognizeAceOfSpades() throws PokerParseException {
        Card aceOfSpades = sut.parseCard("AS");
        assertEquals(new Card(CardSuit.SPADES, CardValue.ACE), aceOfSpades, "AS should match the ace of spades.");
    }

    @Test
    void recognizeHand() throws PokerParseException {
        List<Card> cards = sut.parseCards("2D 3C QH JS AC");

        assertEquals(new Card(CardSuit.DIAMONDS, CardValue.TWO), cards.get(0), "Cards must match after parse.");
        assertEquals(new Card(CardSuit.CLUBS, CardValue.THREE), cards.get(1), "Cards must match after parse.");
        assertEquals(new Card(CardSuit.HEARTS, CardValue.QUEEN), cards.get(2), "Cards must match after parse.");
        assertEquals(new Card(CardSuit.SPADES, CardValue.JACK), cards.get(3), "Cards must match after parse.");
        assertEquals(new Card(CardSuit.CLUBS, CardValue.ACE), cards.get(4), "Cards must match after parse.");
    }
}
