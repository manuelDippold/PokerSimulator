package com.yotilla.poker;

import com.yotilla.poker.card.HandOfCards;
import com.yotilla.poker.result.PokerHand;
import com.yotilla.poker.result.evaluator.*;

import java.util.List;
import java.util.Objects;

/**
 * Description: Determines the best poker hand from a hand of cards
 * by running the evaluator chain from strongest to weakest.
 * <br>
 * Date: 22.03.2026
 *
 * @author Manuel
 */
public class HandEvaluationService {

    private final List<PokerHandEvaluator> evaluators = List.of(
            new RoyalFlushEvaluator(),
            new StraightFlushEvaluator(),
            new FourOfKindEvaluator(),
            new FullHouseEvaluator(),
            new FlushEvaluator(),
            new StraightEvaluator(),
            new TripleEvaluator(),
            new TwoPairsEvaluator(),
            new PairEvaluator(),
            new HighCardEvaluator()
    );

    /**
     * @param hand hand of cards to evaluate
     * @return the best matching PokerHand, or null if hand is null
     */
    public PokerHand evaluate(final HandOfCards hand) {
        return evaluators.stream()
                .map(evaluator -> evaluator.evaluate(hand))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }
}
