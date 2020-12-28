package com.yotilla.poker.result.evaluator;

import java.util.Collections;

import com.yotilla.poker.card.Card;
import com.yotilla.poker.card.CardSuit;
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
public class FlushEvaluator implements PokerHandEvaluator
{

	/**
	 * Attempts to find a flush in this hand and returns an according result.
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
			boolean flush = true;
			CardSuit runningSuit = null;
			for (Card card : hand.getCards())
			{
				CardSuit thisSuit = card.getCardSuit();

				if (runningSuit != null && runningSuit != thisSuit)
				{
					// there are differing suits in this hand. This is not a flush.
					flush = false;
					break;
				}
				runningSuit = thisSuit;
			}

			if (flush)
			{
				// We found a flush, construct an according result.
				// All flush cards serve as rank cards
				return new PokerHand(PokerHandRanking.FLUSH, cardsToSortedCardValues(hand.getCards()),
						Collections.emptyList());
			}
		}

		return null;
	}
}
