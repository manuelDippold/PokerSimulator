package com.yotilla.poker.error;

import com.yotilla.poker.card.Card;

/**
 * Description:
 *
 * <br>
 * Date: 26.12.2020
 *
 * @author Manuel
 *
 */
public class DeckException extends Exception {
    private static final long serialVersionUID = 6491164068858261591L;

    private final DeckExceptionCause deckExceptionCause;

    public DeckException(DeckExceptionCause deckExceptionCause) {
        super(deckExceptionCause.getErrorMessage());
        this.deckExceptionCause = deckExceptionCause;
    }

    public DeckException(final DeckExceptionCause deckExceptionCause, final Card culprit) {
        super(deckExceptionCause.getErrorMessage() + ": " + culprit);
        this.deckExceptionCause = deckExceptionCause;
    }

    /**
     * @return the deckExceptionCause
     */
    public DeckExceptionCause getDeckExceptionCause() {
        return deckExceptionCause;
    }

}
