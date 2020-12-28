package com.yotilla.poker.result.evaluator;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

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
public class TwoPairsEvaluator implements PokerHandEvaluator
{

	/**
	 * If this hand contains two pairs, this method returns an according result.
	 *
	 * @param hand hand to check
	 * @return poker hand or null
	 */
	@Override
	public PokerHand evaluate(HandOfCards hand)
	{
		if (hand != null)
		{
			Map<CardValue, Card> pairCandidates = new EnumMap<>(CardValue.class);
			List<Card> pairs = new ArrayList<>();

			for (Card card : hand.getCards())
			{
				CardValue value = card.getCardValue();

				// If we encountered this value before, this is at least a pair.
				if (pairCandidates.containsKey(value))
				{
					pairs.add(pairCandidates.get(value));
					pairs.add(card);
				}
				else
				{
					pairCandidates.put(value, card);
				}
			}

			if (pairs.size() >= 4)
			{
				// We found at least two pairs.
				return new PokerHand(PokerHandRanking.TWO_PAIRS, cardsToSortedSetList(pairs),
						cardsToSortedCardValues(getKickerCards(hand, pairs)));
			}
		}
		// No two pairs found. return null.
		return null;
	}
}
