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
public class CardSuitTest
{
	/**
	 * capitalCMatchesClubs
	 */
	@Test
	public void capitalCMatchesClubs()
	{
		CardSuit suit = CardSuit.getByCode("C");
		Assertions.assertEquals(CardSuit.CLUBS, suit, "A 'C' should match the clubs.");
	}

	/**
	 * lowerCaseHMatchesHearts()
	 */
	@Test
	public void lowerCaseHMatchesHearts()
	{
		CardSuit suit = CardSuit.getByCode("h");
		Assertions.assertEquals(CardSuit.HEARTS, suit, "A 'h' should match the hearts.");
	}

	/**
	 * theLetterXDoesNotMatchAnySuit
	 */
	@Test
	public void theLetterXDoesNotMatchAnySuit()
	{
		CardSuit suit = CardSuit.getByCode("X");
		Assertions.assertNull(suit, "An 'X' should not match anything.");
	}

	/**
	 * getByCodeIsNullSafe
	 */
	@Test
	public void getByCodeIsNullSafe()
	{
		CardSuit suit = CardSuit.getByCode(null);
		Assertions.assertNull(suit,
				"A null value should not match anything and the method should be able to handle it without an NPE.");
	}
}
