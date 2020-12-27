package com.yotilla.poker.result;

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
public class PokerHandRankingComparator implements NullSafeComparator<PokerHandRanking>
{
	@Override
	public int compareNonNull(PokerHandRanking oneRaking, PokerHandRanking otherRanking)
	{
		return Integer.compare(oneRaking.getScore(), otherRanking.getScore());
	}
}
