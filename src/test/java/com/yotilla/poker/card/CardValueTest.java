package com.yotilla.poker.card;

import org.junit.jupiter.api.Assertions;
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
		Assertions.assertEquals(CardValue.NINE, value, "The String \"9\" should match the value 9.");
	}

	/**
	 * AMatchesAce. Both lower and upper case
	 */
	@Test
	public void AMatchesAce()
	{
		CardValue value = CardValue.getByCode("A");
		Assertions.assertEquals(CardValue.ACE, value, "The String \"A\" should match the Ace.");

		value = CardValue.getByCode("a");
		Assertions.assertEquals(CardValue.ACE, value, "The String \"a\" should match the Ace.");
	}

	/**
	 * xMatchesNoCardValue
	 */
	@Test
	public void xMatchesNoCardValue()
	{
		CardValue value = CardValue.getByCode("X");
		Assertions.assertNull(value, "The String \"X\" shouldn't match anything.");
	}

	/**
	 * getCardValueByCodeIsNullSafe
	 */
	@Test
	public void getCardValueByCodeIsNullSafe()
	{
		CardValue value = CardValue.getByCode(null);
		Assertions.assertNull(value,
				"Null shouldn't match anything and the function should be able to handle it wihtout NPE.");
	}
}
