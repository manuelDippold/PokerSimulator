package com.yotilla.poker;

import com.yotilla.poker.card.HandOfCards;
import com.yotilla.poker.result.PokerHand;

/**
 * Description:
 * A poker playder
 * <br>
 * Date: 29.12.2020
 *
 * @author Manuel
 *
 */
public class Player {
    private HandOfCards hand;
    private PokerHand pokerHand;
    private final String name;

    /**
     * @param name
     */
    public Player(String name) {
        super();
        this.name = name;
    }

    /**
     * Deal the player a hand of cards.
     *
     * @param hand hand player will hold
     */
    public void dealHand(final HandOfCards hand) {
        if (this.hand != null) {
            throw new IllegalArgumentException("Player " + name + " already holds a hand!");
        }

        this.hand = hand;
    }

    /**
     * @return the hand
     */
    public HandOfCards getHand() {
        return hand;
    }

    /**
     * @return the pokerHand
     */
    public PokerHand getPokerHand() {
        return pokerHand;
    }

    /**
     * @param pokerHand the pokerHand to set
     */
    public void setPokerHand(PokerHand pokerHand) {
        this.pokerHand = pokerHand;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
}
