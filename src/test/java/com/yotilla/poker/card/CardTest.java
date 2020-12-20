package com.yotilla.poker.card;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Description: Test card comparison to one another. Needs to work for the high
 * card mechanism. <br>
 * Date: 20.12.2020
 *
 * @author Manuel
 *
 */
public class CardTest
{
	/**
	 * aceRanksHigherThanKing
	 */
	@Test
	public void aceRanksHigherThanKing()
	{
		Card ace = new Card(CardSuit.HEARTS, CardValue.ACE);
		Card king = new Card(CardSuit.HEARTS, CardValue.KING);

		Assertions.assertTrue(ace.compareTo(king) > 0, "An ace should rank higher than a king.");
	}

	/**
	 * cardCompareIsNullSafe
	 */
	@Test
	public void cardCompareIsNullSafe()
	{
		Card twoOfSpades = new Card(CardSuit.SPADES, CardValue.TWO);

		Assertions.assertTrue(twoOfSpades.compareTo(null) > 0,
				"Any card ought to rank higher than no card. And compare shouldn't throw a NPE.");

		// Mock a card that has a suit, but null as a value.
		Card nothingOfSpades = Mockito.mock(Card.class);
		Mockito.when(nothingOfSpades.getCardSuit()).thenReturn(CardSuit.SPADES);

		Assertions.assertTrue(twoOfSpades.compareTo(nothingOfSpades) > 0,
				"Any card ought to rank higher than a card withour a value set. And compare shouldn't throw a NPE.");
	}

	/**
	 * twoIsLessThanFour
	 */
	@Test
	public void twoIsLessThanFour()
	{
		Card twoOfDiamonds = new Card(CardSuit.DIAMONDS, CardValue.TWO);
		Card fourOfHearts = new Card(CardSuit.HEARTS, CardValue.FOUR);

		Assertions.assertTrue(twoOfDiamonds.compareTo(fourOfHearts) < 0,
				"A two ranks below a four, independent of suit.");
	}
}
