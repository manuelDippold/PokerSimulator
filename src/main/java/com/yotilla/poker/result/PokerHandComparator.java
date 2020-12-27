package com.yotilla.poker.result;

import java.util.Iterator;
import java.util.List;

import com.yotilla.poker.card.CardValue;
import com.yotilla.poker.card.CardValueComparator;
import com.yotilla.poker.util.NullSafeComparator;

/**
 * Description:
 *
 * <br>
 * Date: 27.12.2020
 *
 * @author Manuel
 *
 */
public class PokerHandComparator implements NullSafeComparator<PokerHand>
{
	PokerHandRankingComparator rankingComparator = new PokerHandRankingComparator();
	CardValueComparator cardValueComparator = new CardValueComparator();

	@Override
	public int compareNonNull(PokerHand oneHand, PokerHand otherHand)
	{
		// First tier: Do the rankings differ?
		int result = rankingComparator.compare(oneHand.getRanking(), otherHand.getRanking());

		// If they don't, this ends here.
		if (result != 0)
		{
			return result;
		}

		// If the rankings are equal, their rank cards must serve as a tie breaker.
		// For example: If you hold a pair of kings and a pair of nines,
		// your rank cards are king and nine, in that order.
		int rankCardResult = getHighCardComparisonResult(oneHand.getRankCards(), otherHand.getRankCards());

		if (rankCardResult != 0)
		{
			// We reached a rank result.
			return rankCardResult;
		}

		// At this point, the ranking and the rank cards were equal. Now the highest
		// kicker card decides. That means we must go down the kicker cards of both
		// players until we reach a decision.
		// If we don't, the tie is a decision in itself.
		return getHighCardComparisonResult(oneHand.getKickerCards(), otherHand.getKickerCards());
	}

	/**
	 * Get the result of a high cards comparison. Can be used for rank card values
	 * or kicker card values.
	 *
	 * @param valuesOne Value list one.
	 * @param valuesTwo Value list two.
	 * @return integer as defined by compare()
	 */
	private int getHighCardComparisonResult(final List<CardValue> valuesOne, final List<CardValue> valuesTwo)
	{
		if (valuesOne != null && valuesTwo != null)
		{
			Iterator<CardValue> valueOneIterator = valuesOne.iterator();
			Iterator<CardValue> valueOtherIterator = valuesTwo.iterator();

			while (valueOneIterator.hasNext() && valueOtherIterator.hasNext())
			{
				CardValue oneValue = valueOneIterator.next();
				CardValue otherValue = valueOtherIterator.next();

				int result = cardValueComparator.compare(oneValue, otherValue);

				// We reached a rank result.
				if (result != 0)
				{
					return result;
				}
			}
		}

		// No conclusion reached. Tie.
		return 0;
	}
}
