package com.yotilla.poker.card;

/**
 * Description: Card suits assigned with a code <br>
 * Date: 20.12.2020
 *
 * @author Manuel
 *
 */

import java.util.Arrays;
import java.util.Objects;

public enum CardSuit {
    CLUBS("C"),
    DIAMONDS("D"),
    HEARTS("H"),
    SPADES("S");

    private final String code;

    /**
     * Constructor.
     *
     * @param code code to set
     */
    private CardSuit(String code) {
        this.code = code;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * Finds and returns the suit matching the provided code, if there is one.
     * Case-insensitive, i.e. both 'c' and 'C' match Clubs.
     *
     * @param codeToMatch code to match
     * @return CardSuit, if found. Null, if not.
     */
    public static CardSuit getByCode(final String codeToMatch) {
        Objects.requireNonNull(codeToMatch, "Card suit code must not be null.");

        return Arrays.stream(values())
                .filter(suit -> suit.getCode().equalsIgnoreCase(codeToMatch))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("No card suit found for code: %s", codeToMatch)));
    }
}
