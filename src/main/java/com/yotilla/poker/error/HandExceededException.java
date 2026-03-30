package com.yotilla.poker.error;

import java.io.Serial;

/**
 * Description: An exception that is thrown when a player gets more cards than
 * allowed. <br>
 * Date: 20.12.2020
 *
 * @author Manuel
 *
 */
public class HandExceededException extends Exception {

    @Serial
    private static final long serialVersionUID = -7955136988193841987L;

    /**
     *
     * {@inheritDoc}
     */
    public HandExceededException(String message) {
        super(message);
    }
}
