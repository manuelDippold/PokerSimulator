package com.yotilla.poker.result.evaluator;

import java.util.Collections;

import com.yotilla.poker.card.CardValue;
import com.yotilla.poker.card.HandOfCards;
import com.yotilla.poker.error.HandExceededException;
import com.yotilla.poker.result.PokerHand;
import com.yotilla.poker.result.PokerHandRanking;

/**
 * Description:
 *
 * <br>
 * Date: 28.12.2020
 *
 * @author Manuel
 *
 */
public class RoyalFlushEvaluator extends StraightFlushEvaluator
{
	/**
	 * Attempts to a royal flush in this hand and returns an according result.
	 *
	 * @param hand hand to analyze
	 * @return poker hand or null.
	 * @throws HandExceededException in case of an error
	 */
	@Override
	public PokerHand evaluate(HandOfCards hand)
	{
		if (hand != null)
		{
			// A royal flush is a straight flush that leads with an ace.
			PokerHand straightFlush = super.evaluate(hand);

			if (straightFlush != null && straightFlush.getRankCards().get(0) == CardValue.ACE)
			{
				// A straight flush with an Ace leading it. That's called a royal flush.
				return new PokerHand(PokerHandRanking.ROYAL_FLUSH, Collections.emptyList(), Collections.emptyList());
			}

		}

		return null;
	}
}
