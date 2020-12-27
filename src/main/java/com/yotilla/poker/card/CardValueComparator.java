package com.yotilla.poker.card;

import java.util.Comparator;

/**
 * Description: Comparator for two card values
 *
 * <br>
 * Date: 27.12.2020
 *
 * @author Manuel
 *
 */
public class CardValueComparator implements Comparator<CardValue>
{

	@Override
	public int compare(CardValue one, CardValue other)
	{
		if (one == null && other == null)
		{
			return 0;
		}
		else if (one != null && other == null)
		{
			return 1;
		}
		else if (one == null)
		{
			return -1;
		}

		return Integer.compare(one.getNumericalValue(), other.getNumericalValue());
	}

}
