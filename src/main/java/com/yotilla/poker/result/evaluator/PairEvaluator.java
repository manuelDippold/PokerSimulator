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
public class PairEvaluator extends MultiplesEvaluator
{

	/**
	 * Determines whether or not this hand of cards contains at least one Pair. If
	 * it does, returns the card value of the pair. Null, if not.
	 *
	 * @param hand hand of cards we wish to check
	 * @return Card value or null
	 */
	@Override
	public PokerHand evaluate(HandOfCards hand)
	{
		return evaluateMultipleHand(hand, 2);
	}

}
