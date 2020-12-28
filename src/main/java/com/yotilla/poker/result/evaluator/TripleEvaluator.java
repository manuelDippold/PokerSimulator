package com.yotilla.poker.result.evaluator;

import com.yotilla.poker.card.HandOfCards;
import com.yotilla.poker.result.PokerHand;

/**
 * Description:
 *
 * <br>
 * Date: 28.12.2020
 *
 * @author Manuel
 *
 */
public class TripleEvaluator extends MultiplesEvaluator
{
	/**
	 * Attempts to find three of a kind in this hand and returns an according result.
	 *
	 * @param hand hand to analyze
	 * @return poker hand
	 */
	@Override
	public PokerHand evaluate(HandOfCards hand)
	{
		return evaluateMultipleHand(hand, 3);
	}
}
