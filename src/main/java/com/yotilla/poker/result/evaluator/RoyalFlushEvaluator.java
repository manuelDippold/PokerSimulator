package com.yotilla.poker.result.evaluator;

import com.yotilla.poker.card.CardValue;
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
public class RoyalFlushEvaluator extends StraightFlushEvaluator {
    public RoyalFlushEvaluator(StraightEvaluator straightEvaluator, FlushEvaluator flushEvaluator) {
        super(straightEvaluator, flushEvaluator);
    }

    @Override
    public PokerHand evaluate(HandOfCards hand) {
        if (hand != null) {
            // A royal flush is a straight flush that leads with an ace.
            PokerHand straightFlush = super.evaluate(hand);

            if (straightFlush != null && straightFlush.rankCards().getFirst() == CardValue.ACE) {
                // A straight flush with an Ace leading it. That's called a royal flush.
                return new PokerHand(PokerHandRanking.ROYAL_FLUSH, Collections.emptyList(), Collections.emptyList());
            }

        }

        return null;
    }
}
