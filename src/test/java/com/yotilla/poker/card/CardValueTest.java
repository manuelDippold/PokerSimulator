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
class CardValueTest {
    /**
     * nineMatchesNine
     */
    @Test
    void nineMatchesNine() {
        CardValue value = CardValue.getByCode("9");
        assertEquals(CardValue.NINE, value, "The String \"9\" should match the value 9.");
    }

    /**
     * AMatchesAce. Both lower and upper case
     */
    @Test
    void AMatchesAce() {
        CardValue value = CardValue.getByCode("A");
        assertEquals(CardValue.ACE, value, "The String \"A\" should match the Ace.");

        value = CardValue.getByCode("a");
        assertEquals(CardValue.ACE, value, "The String \"a\" should match the Ace.");
    }

    @Test
    void invalidInputThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> CardValue.getByCode("x"));
        assertThrows(IllegalArgumentException.class, () -> CardValue.getByCode(""));
        assertThrows(IllegalArgumentException.class, () -> CardValue.getByCode("Bandierra rossa"));
    }

    /**
     * getCardValueByCodeIsNullSafe
     */
    @Test
    void getByCodeThrowsOnNull() {
        assertThrows(NullPointerException.class, () -> CardValue.getByCode(null));
    }

    /**
     * thirteenMatchesKing
     */
    @Test
    void thirteenMatchesKing() {
        CardValue value = CardValue.getByNumericalValue(13);
        assertEquals(CardValue.KING, value, "The value 13 should match the king.");
    }

    /**
     * zeroMatchesNoCardValue
     */
    @Test
    void zeroThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> CardValue.getByCode("0"));
    }

    /**
     * twentyMatchesNoCardValue
     */
    @Test
    void twentyThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> CardValue.getByCode("20"));
    }
}
