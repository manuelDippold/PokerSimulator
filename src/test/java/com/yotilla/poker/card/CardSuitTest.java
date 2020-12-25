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
class CardSuitTest
{
	/**
	 * capitalCMatchesClubs
	 */
	@Test
	void capitalCMatchesClubs()
	{
		CardSuit suit = CardSuit.getByCode("C");
		assertEquals(CardSuit.CLUBS, suit, "A 'C' should match the clubs.");
	}

	/**
	 * lowerCaseHMatchesHearts()
	 */
	@Test
	void lowerCaseHMatchesHearts()
	{
		CardSuit suit = CardSuit.getByCode("h");
		assertEquals(CardSuit.HEARTS, suit, "A 'h' should match the hearts.");
	}

	/**
	 * theLetterXDoesNotMatchAnySuit
	 */
	@Test
	void theLetterXDoesNotMatchAnySuit()
	{
		CardSuit suit = CardSuit.getByCode("X");
		assertNull(suit, "An 'X' should not match anything.");
	}

	/**
	 * getByCodeIsNullSafe
	 */
	@Test
	void getByCodeIsNullSafe()
	{
		CardSuit suit = CardSuit.getByCode(null);
		assertNull(suit,
				"A null value should not match anything and the method should be able to handle it without an NPE.");
	}
}
