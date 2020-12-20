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
public class CardValueTest
{
	/**
	 * nineMatchesNine
	 */
	@Test
	public void nineMatchesNine()
	{
		CardValue value = CardValue.getByCode("9");
		assertEquals(CardValue.NINE, value, "The String \"9\" should match the value 9.");
	}

	/**
	 * AMatchesAce. Both lower and upper case
	 */
	@Test
	public void AMatchesAce()
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
	public void xMatchesNoCardValue()
	{
		CardValue value = CardValue.getByCode("X");
		assertNull(value, "The String \"X\" shouldn't match anything.");
	}

	/**
	 * getCardValueByCodeIsNullSafe
	 */
	@Test
	public void getCardValueByCodeIsNullSafe()
	{
		CardValue value = CardValue.getByCode(null);
		assertNull(value, "Null shouldn't match anything and the function should be able to handle it wihtout NPE.");
	}
}
