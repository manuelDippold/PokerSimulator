package com.yotilla.poker;

import com.yotilla.poker.card.HandOfCards;
import com.yotilla.poker.result.PokerHand;

/**
 * Description:
 * A poker player
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

    public Player(String name) {
        super();
        this.name = name;
    }

    public void dealHand(final HandOfCards hand) {
        if (this.hand != null) {
            throw new IllegalStateException("Player " + name + " already holds a hand!");
        }

        this.hand = hand;
    }

    public HandOfCards getHand() {
        return hand;
    }


    public PokerHand getPokerHand() {
        return pokerHand;
    }


    public void setPokerHand(PokerHand pokerHand) {
        this.pokerHand = pokerHand;
    }


    public String getName() {
        return name;
    }
}
