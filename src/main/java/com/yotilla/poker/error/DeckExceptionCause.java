package com.yotilla.poker.error;

/**
 * Description:
 *
 * <br>
 * Date: 26.12.2020
 *
 * @author Manuel
 *
 */
public enum DeckExceptionCause {
    CARD_ALREADY_DRAWN("A card was already drawn from the deck"),
    DECK_IS_EMPTY("The deck is empty, no more cards can be drawn");

    private final String errorMessage;


    private DeckExceptionCause(String argErrorMessage) {
        errorMessage = argErrorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
