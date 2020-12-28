package com.yotilla.poker.result.evaluator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
public class StraightEvaluator implements PokerHandEvaluator
{
	/**
	 * Attempts to find a straight in this hand and returns an according result.
	 *
	 * @param hand hand to analyze
	 * @return poker hand or null.
	 */
	@Override
	public PokerHand evaluate(HandOfCards hand)
	{
		if (hand != null)
		{
			// make a working copy of the hand and order it
			List<Card> workingCopy = copyHandOfCards(hand).getCards();
			workingCopy.sort(null);

			// special case: If the hand contains an ace, it can be the starting or the ending card.
			accountForAceBeginningStraight(workingCopy);

			boolean skipped = false;
			int highestvalue = 0;
			int runningCardValue = 0;

			// See if we can step trough the ordered cards without skipping one.
			for (Card card : workingCopy)
			{
				int thisCardValue = card.getCardValue().getNumericalValue();

				if (runningCardValue > 0 && Math.abs(thisCardValue - runningCardValue) > 1)
				{
					// we skipped a card. Break here.
					skipped = true;
					break;
				}
				else
				{
					runningCardValue = thisCardValue;

					if (thisCardValue > highestvalue)
					{
						highestvalue = thisCardValue;
					}
				}
			}

			if (!skipped)
			{
				// We found the straight, construct a result. The ranking card is the highest card of the straight.
				return new PokerHand(PokerHandRanking.STRAIGHT,
						Arrays.asList(CardValue.getByNumericalValue(highestvalue)), Collections.emptyList());
			}
		}

		return null;
	}

	/**
	 * account for the fact that an ace can lead a straight.
	 * Do so by removing it if there is a two.
	 *
	 * @param workingCopy copy of cards to work with
	 */
	private void accountForAceBeginningStraight(final List<Card> workingCopy)
	{
		boolean containsAce = workingCopy.stream().anyMatch(c -> c.getCardValue() == CardValue.ACE);
		boolean containsTwo = workingCopy.stream().anyMatch(c -> c.getCardValue() == CardValue.TWO);

		// if this hand contains an ace, check if there is also a kind or a two, so it can form a straight.
		if (containsAce && containsTwo)
		{
			// If there are an ace and a two, remove the ace and continue.
			// The ace must begin the the straight this way, there is not other possibility.
			workingCopy.removeIf(c -> c.getCardValue() == CardValue.ACE);
		}
	}

}
