package com.yotilla.poker.card;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

/**
 * Description:
 *
 * <br>
 * Date: 20.12.2020
 *
 * @author Manuel
 *
 */
class CardValueTest
{
	/**
	 * nineMatchesNine
	 */
	@Test
	void nineMatchesNine()
	{
		CardValue value = CardValue.getByCode("9");
		assertEquals(CardValue.NINE, value, "The String \"9\" should match the value 9.");
	}

	/**
	 * AMatchesAce. Both lower and upper case
	 */
	@Test
	void AMatchesAce()
	{
		CardValue value = CardValue.getByCode("A");
		assertEquals(CardValue.ACE, value, "The String \"A\" should match the Ace.");

		value = CardValue.getByCode("a");
		assertEquals(CardValue.ACE, value, "The String \"a\" should match the Ace.");
	}

	/**
	 * xMatchesNoCardValue
	 */
	@Test
	void xMatchesNoCardValue()
	{
		CardValue value = CardValue.getByCode("X");
		assertNull(value, "The String \"X\" shouldn't match anything.");
	}

	/**
	 * getCardValueByCodeIsNullSafe
	 */
	@Test
	void getCardValueByCodeIsNullSafe()
	{
		CardValue value = CardValue.getByCode(null);
		assertNull(value, "Null shouldn't match anything and the function should be able to handle it wihtout NPE.");
	}

	/**
	 * thirteenMatchesKing
	 */
	@Test
	void thirteenMatchesKing()
	{
		CardValue value = CardValue.getByNumericalValue(13);
		assertEquals(CardValue.KING, value, "The value 13 should match the king.");
	}

	/**
	 * zeroMatchesNoCardValue
	 */
	@Test
	void zeroMatchesNoCardValue()
	{
		CardValue value = CardValue.getByNumericalValue(0);
		assertNull(value, "Zero shouldn't match anything.");
	}

	/**
	 * twentyMatchesNoCardValue
	 */
	@Test
	void twentyMatchesNoCardValue()
	{
		CardValue value = CardValue.getByNumericalValue(20);
		assertNull(value, "Twenty shouldn't match anything.");
	}
}
