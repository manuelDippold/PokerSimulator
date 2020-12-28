package com.yotilla.poker.result.evaluator;

import java.util.Arrays;
import java.util.List;

import com.yotilla.poker.CardCollector;
import com.yotilla.poker.card.Card;
import com.yotilla.poker.card.CardValue;
import com.yotilla.poker.card.HandOfCards;
import com.yotilla.poker.result.PokerHand;
import com.yotilla.poker.result.PokerHandRanking;

/**
 * Description:
 * Abstract class to find multiples (pairs, triples, fours).
 * <br>
 * Date: 28.12.2020
 *
 * @author Manuel
 *
 */
public abstract class MultiplesEvaluator implements PokerHandEvaluator
{

	/**
	 * Attempts to find multiples (pairs, triples, fours) in this hand and returns an according result.
	 * Does not work for two pairs, though.
	 *
	 * @param handOfCards     hand of cards to analyze
	 * @param desiredMultiple desired multiple we want. 2, 3 or 4
	 * @return poker hand or null
	 */
	protected PokerHand evaluateMultipleHand(final HandOfCards hand, final int desiredMultiple)
	{
		if (hand != null && desiredMultiple >= 2 && desiredMultiple <= 4)
		{
			CardCollector collector = new CardCollector();
			CardValue multipleCardValue = null;

			for (Card card : hand.getCards())
			{
				collector.addCard(card);

				CardValue val = card.getCardValue();
				if (collector.getAmountBehindKey(val) >= desiredMultiple)
				{
					multipleCardValue = val;
				}
			}

			if (multipleCardValue != null)
			{
				// We found it. Build a result.
				List<Card> remainingCards = getKickerCards(hand, collector.get(multipleCardValue));

				PokerHandRanking ranking;

				switch (desiredMultiple) {
				case 2:
					ranking = PokerHandRanking.ONE_PAIR;
					break;
				case 3:
					ranking = PokerHandRanking.THREE_OF_A_KIND;
					break;
				case 4:
					ranking = PokerHandRanking.FOUR_OF_A_KIND;
					break;
				default: // Impossible, but alas...
					return null;

				}

				return new PokerHand(ranking, Arrays.asList(multipleCardValue),
						cardsToSortedCardValues(remainingCards));
			}
		}

		return null;
	}

}
