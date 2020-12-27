package com.yotilla.poker.card;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Description:
 *
 * <br>
 * Date: 27.12.2020
 *
 * @author Manuel
 *
 */
class CardValueComparatorTest
{
	private final CardValueComparator comp = new CardValueComparator();

	/**
	 * comparatorIsNullSafe
	 */
	@Test
	void comparatorIsNullSafe()
	{
		int result = comp.compare(null, null);
		assertSame(0, result, "Null and null are equal.");

		result = comp.compare(CardValue.TWO, null);
		assertTrue(result > 0, "Two is more than null.");

		result = comp.compare(null, CardValue.FIVE);
		assertTrue(result < 0, "Null is less than five.");
	}

	/**
	 * twoIsLessThanFour
	 */
	@Test
	void twoIsLessThanFour()
	{
		int result = comp.compare(CardValue.TWO, CardValue.FOUR);
		assertTrue(result < 0, "Two is less than four.");
	}

	/**
	 * jackRanksHigherThanTen
	 */
	@Test
	void jackRanksHigherThanTen()
	{
		int result = comp.compare(CardValue.JACK, CardValue.TEN);
		assertTrue(result > 0, "A jack ranks higher than a ten.");
	}

	/**
	 * twoKingsAreEqual
	 */
	@Test
	void twoKingsAreEqual()
	{
		int result = comp.compare(CardValue.KING, CardValue.KING);
		assertSame(0, result, "Two Kings ought to be equal.");
	}
}
