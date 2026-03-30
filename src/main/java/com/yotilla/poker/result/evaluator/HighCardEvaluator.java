package com.yotilla.poker.result.evaluator;

import com.yotilla.poker.card.HandOfCards;
import com.yotilla.poker.result.PokerHand;
import com.yotilla.poker.result.PokerHandRanking;

import java.util.Collections;

/**
 * Description:
 *
 * <br>
 * Date: 28.12.2020
 *
 * @author Manuel
 *
 */
public class HighCardEvaluator implements PokerHandEvaluator {

    @Override
    public PokerHand evaluate(HandOfCards hand) {
        if (hand == null) {
            return null;
        }

        return new PokerHand(PokerHandRanking.HIGH_CARD, cardsToSortedCardValues(hand.getCards()),
                Collections.emptyList());
    }

}
