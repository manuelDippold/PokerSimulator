package com.yotilla.poker.result.evaluator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

import com.yotilla.poker.CardCollector;
import com.yotilla.poker.card.Card;
import com.yotilla.poker.card.CardValue;
import com.yotilla.poker.card.HandOfCards;
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
public class FullHouseEvaluator implements PokerHandEvaluator
{

	/**
	 * Attempts to find a full house in this hand and returns an according result.
	 *
	 * @param hand hand to analyze
	 * @return poker hand or null.
	 */
	@Override
	public PokerHand evaluate(HandOfCards hand)
	{
		if (hand != null)
		{
			CardCollector collector = new CardCollector();
			CardValue tripleValue = null;
			CardValue pairValue = null;

			// load all cards to collector
			collector.addCards(hand.getCards());

			// Loop over entry set, see if there are both a triple and a pair.
			for (Entry<CardValue, List<Card>> entry : collector.entrySet())
			{
				CardValue value = entry.getKey();
				int cards = entry.getValue().size();

				if (cards == 3)
				{
					tripleValue = value;
				}
				if (cards == 2)
				{
					pairValue = value;
				}
			}

			if (tripleValue != null && pairValue != null)
			{
				// we found a triple and a pair => a full house. Build a result.
				return new PokerHand(PokerHandRanking.FULL_HOUSE, Arrays.asList(tripleValue, pairValue),
						Collections.emptyList());
			}
		}

		return null;
	}

}
