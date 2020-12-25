package com.yotilla.poker.card;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
class CardTest
{
	/**
	 * aceRanksHigherThanKing
	 */
	@Test
	void aceRanksHigherThanKing()
	{
		Card ace = new Card(CardSuit.HEARTS, CardValue.ACE);
		Card king = new Card(CardSuit.HEARTS, CardValue.KING);

		assertTrue(ace.compareTo(king) > 0, "An ace should rank higher than a king.");
	}

	/**
	 * cardCompareIsNullSafe
	 */
	@Test
	void cardCompareIsNullSafe()
	{
		Card twoOfSpades = new Card(CardSuit.SPADES, CardValue.TWO);

		assertTrue(twoOfSpades.compareTo(null) > 0,
				"Any card ought to rank higher than no card. And compare shouldn't throw a NPE.");

		// Mock a card that has a suit, but null as a value.
		Card nothingOfSpades = Mockito.mock(Card.class);
		Mockito.when(nothingOfSpades.getCardSuit()).thenReturn(CardSuit.SPADES);

		assertTrue(twoOfSpades.compareTo(nothingOfSpades) > 0,
				"Any card ought to rank higher than a card withour a value set. And compare shouldn't throw a NPE.");
	}

	/**
	 * twoIsLessThanFour
	 */
	@Test
	void twoIsLessThanFour()
	{
		Card twoOfDiamonds = new Card(CardSuit.DIAMONDS, CardValue.TWO);
		Card fourOfHearts = new Card(CardSuit.HEARTS, CardValue.FOUR);

		assertTrue(twoOfDiamonds.compareTo(fourOfHearts) < 0, "A two ranks below a four, independent of suit.");
	}

	/**
	 * twoQueensAreEqual
	 */
	@Test
	void twoQueensAreEqual()
	{
		Card queenOfClubs = new Card(CardSuit.CLUBS, CardValue.QUEEN);
		Card queenOfDiamonds = new Card(CardSuit.DIAMONDS, CardValue.QUEEN);

		assertEquals(0, queenOfClubs.compareTo(queenOfDiamonds), "Two queens ought to be of equal value.");
	}

	/**
	 * twoIdenticalCardsAreEqual
	 */
	@Test
	void twoIdenticalCardsAreEqual()
	{
		Card queenOfHeartsOne = new Card(CardSuit.HEARTS, CardValue.QUEEN);
		Card queenOfHeartsTwo = new Card(CardSuit.HEARTS, CardValue.QUEEN);

		assertEquals(queenOfHeartsOne, queenOfHeartsTwo, "These two ought to be recognized as identical");
	}

	/**
	 * bothPropertiesMusMatchToBeEqual
	 */
	@Test
	void bothPropertiesMusMatchToBeEqual()
	{
		Card aceOfHearts = new Card(CardSuit.HEARTS, CardValue.ACE);
		Card aceOfClubs = new Card(CardSuit.CLUBS, CardValue.ACE);

		assertNotEquals(aceOfHearts, aceOfClubs, "These two ought to be recognized as different");

		Card kingOfHearts = new Card(CardSuit.HEARTS, CardValue.KING);
		assertNotEquals(aceOfHearts, kingOfHearts, "These two ought to be recognized as different");
	}

	/**
	 * euqalsIsNullSafe
	 */
	@Test
	void euqalsIsNullSafe()
	{
		Card aceOfHearts = new Card(CardSuit.HEARTS, CardValue.ACE);

		boolean result = aceOfHearts.equals(null);
		assertFalse(result, "This boolean should be false");
	}

	/**
	 * euqalsIsDeepNullSafe
	 */
	@Test
	void euqalsIsDeepNullSafe()
	{
		Card aceOfHearts = new Card(CardSuit.HEARTS, CardValue.ACE);
		Card incomplete = new Card(null, null);

		boolean result = aceOfHearts.equals(incomplete);
		assertFalse(result, "This boolean should be false");

		result = incomplete.equals(aceOfHearts);
		assertFalse(result, "This boolean should still be false");
	}
}
