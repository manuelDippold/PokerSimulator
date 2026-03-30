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
public class StraightFlushEvaluator implements PokerHandEvaluator {
    private final StraightEvaluator straightEvaluator;
    private final FlushEvaluator flushEvaluator;

    public StraightFlushEvaluator(StraightEvaluator straightEvaluator, FlushEvaluator flushEvaluator) {
        this.straightEvaluator = straightEvaluator;
        this.flushEvaluator = flushEvaluator;
    }

    /**
     * Attempts to find a straight flush in this hand and returns an according result.
     *
     * @param hand hand to analyze
     * @return poker hand or null.
     */
    @Override
    public PokerHand evaluate(HandOfCards hand) {
        if (hand != null) {
            // A straight flush is a straight that is also a flush. The straight leading card is the rank card.
            PokerHand straight = straightEvaluator.evaluate(hand);

            if (straight != null) {
                PokerHand flush = flushEvaluator.evaluate(hand);

                if (flush != null) {
                    // We found a straight flush. Build an according result.
                    return new PokerHand(PokerHandRanking.STRAIGHT_FLUSH, straight.rankCards(),
                            Collections.emptyList());
                }
            }
        }
        return null;
    }
}
