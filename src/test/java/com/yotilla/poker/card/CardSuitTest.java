package com.yotilla.poker.card;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Description:
 *
 * <br>
 * Date: 20.12.2020
 *
 * @author Manuel
 *
 */
class CardSuitTest {
    /**
     * capitalCMatchesClubs
     */
    @Test
    void capitalCMatchesClubs() {
        CardSuit suit = CardSuit.getByCode("C");
        assertEquals(CardSuit.CLUBS, suit, "A 'C' should match the clubs.");
    }

    /**
     * lowerCaseHMatchesHearts()
     */
    @Test
    void lowerCaseHMatchesHearts() {
        CardSuit suit = CardSuit.getByCode("h");
        assertEquals(CardSuit.HEARTS, suit, "A 'h' should match the hearts.");
    }

    @Test
    void invalidInputThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> CardSuit.getByCode("x"));
        assertThrows(IllegalArgumentException.class, () -> CardSuit.getByCode(""));
        assertThrows(IllegalArgumentException.class, () -> CardSuit.getByCode("Bandierra rossa"));
    }


    @Test
    void getByCodeThrowsOnNull() {
        assertThrows(NullPointerException.class, () -> CardSuit.getByCode(null));
    }
}
