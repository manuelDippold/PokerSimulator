package com.yotilla.poker.result.evaluator;

import com.yotilla.poker.card.HandOfCards;
import com.yotilla.poker.error.HandExceededException;
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
public class FourOfKindEvaluator extends MultiplesEvaluator
{

	/**
	 * Attempts to find four of a kind in this hand and returns an according result.
	 *
	 * @param hand hand to analyze
	 * @return poker hand or null.
	 * @throws HandExceededException in case of an error
	 */
	@Override
	public PokerHand evaluate(HandOfCards hand)
	{
		return evaluateMultipleHand(hand, 4);
	}
}
