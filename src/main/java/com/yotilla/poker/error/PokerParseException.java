package com.yotilla.poker.error;

import java.io.Serial;

/**
 * Description:
 *
 * <br>
 * Date: 29.12.2020
 *
 * @author Manuel
 *
 */
public class PokerParseException extends Exception {
    @Serial
    private static final long serialVersionUID = 1551204055933202984L;

    public PokerParseException(final String message) {
        super(message);
    }

    public PokerParseException(final String message, Throwable cause) {
        super(message, cause);
    }
}
