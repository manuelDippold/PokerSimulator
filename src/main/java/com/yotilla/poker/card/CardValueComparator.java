package com.yotilla.poker.card;

import javax.swing.SortOrder;

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
	private SortOrder order = SortOrder.ASCENDING;

	@Override
	public int compareNonNull(CardValue one, CardValue other)
	{
		if (order == SortOrder.DESCENDING)
		{
			return Integer.compare(other.getNumericalValue(), one.getNumericalValue());
		}

		return Integer.compare(one.getNumericalValue(), other.getNumericalValue());
	}

	/**
	 * @param argOrder the order to set
	 * @return comparator
	 */
	public CardValueComparator setOrder(SortOrder argOrder)
	{
		order = argOrder;
		return this;
	}

}
