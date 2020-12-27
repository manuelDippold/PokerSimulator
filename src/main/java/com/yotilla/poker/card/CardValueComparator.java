package com.yotilla.poker.card;

import com.yotilla.poker.util.NullSafeComparator;

/**
 * Description: Comparator for two card values
 *
 * <br>
 * Date: 27.12.2020
 *
 * @author Manuel
 *
 */
public class CardValueComparator implements NullSafeComparator<CardValue>
{
	@Override
	public int compareNonNull(CardValue one, CardValue other)
	{
		return Integer.compare(one.getNumericalValue(), other.getNumericalValue());
	}

}
