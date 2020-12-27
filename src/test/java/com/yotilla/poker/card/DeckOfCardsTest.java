package com.yotilla.poker.card;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import com.yotilla.poker.error.DeckException;
import com.yotilla.poker.error.DeckExceptionCause;

/**
 * Description:
 *
 * <br>
 * Date: 26.12.2020
 *
 * @author Manuel
 *
 */
class DeckOfCardsTest
{
	/**
	 * constructorFillsDeckWithFiftyTwoCards
	 */
	@Test
	void constructorFillsDeckWithFiftyTwoCards()
	{
		DeckOfCards deck = new DeckOfCards();
		assertEquals(DeckOfCards.DECK_SIZE, deck.getAmountOfCardsLeft(), "A new deck should hold fifty-two cards.");
	}

	/**
	 * Draw three distinct cards from the deck and check each one.
	 *
	 * @throws DeckException error case
	 */
	@Test
	void drawThreeCards() throws DeckException
	{
		DeckOfCards deck = new DeckOfCards();

		Card threeOfClubs = deck.drawCard(CardSuit.CLUBS, CardValue.THREE);
		Card anotherThreeOfClubs = new Card(CardSuit.CLUBS, CardValue.THREE);
		assertEquals(anotherThreeOfClubs, threeOfClubs,
				"The three of clubs from the deck should be equal to any other.");

		Card nineOfHearts = deck.drawCard(CardSuit.HEARTS, CardValue.NINE);
		Card anotherNineOfHearts = new Card(CardSuit.HEARTS, CardValue.NINE);
		assertEquals(anotherNineOfHearts, nineOfHearts,
				"The nine of hearts from the deck should be equal to any other.");

		Card kingOfDiamonds = deck.drawCard(CardSuit.DIAMONDS, CardValue.KING);
		Card anotherKingOfDiamonds = new Card(CardSuit.DIAMONDS, CardValue.KING);
		assertEquals(anotherKingOfDiamonds, kingOfDiamonds,
				"The King of diamond from the deck should be equal to any other.");
	}

	/**
	 * drawingTheSameCardTwiceThrowsException
	 *
	 * @throws DeckException error case
	 */
	@Test
	void drawingTheSameCardTwiceThrowsException() throws DeckException
	{
		DeckOfCards deck = new DeckOfCards();

		// draw the jack of Hearts.
		deck.drawCard(CardSuit.HEARTS, CardValue.JACK);

		// attempt to draw the same card again.
		// Should throw an exception with a distinct cause
		try
		{
			deck.drawCard(CardSuit.HEARTS, CardValue.JACK);
			fail("The code before this line should have thrown an exception.");
		}
		catch (DeckException e)
		{
			assertEquals(DeckExceptionCause.CARD_ALREADY_DRAWN, e.getDeckExceptionCause());
		}
	}

	/**
	 * check the deck for completeness
	 *
	 * @throws DeckException error case
	 */
	@Test
	void freshDeckContainsAllCards() throws DeckException
	{
		DeckOfCards deck = new DeckOfCards();

		// Loop over and draw all possible cards
		for (CardSuit suit : CardSuit.values())
		{
			for (CardValue value : CardValue.values())
			{
				Card referenceCard = new Card(suit, value);
				Card cardFromDeck = deck.drawCard(suit, value);

				assertEquals(referenceCard, cardFromDeck, "Card from deck and reference should match.");
			}
		}

		assertTrue(deck.isEmpty(), "We've drawn all cards, the deck should be empty.");
	}

	/**
	 * drawingFromAnEmptyDeckThrowsException
	 *
	 * @throws DeckException error case
	 */
	@Test
	void drawingFromAnEmptyDeckThrowsException() throws DeckException
	{
		DeckOfCards deck = new DeckOfCards();

		// Loop over and draw all possible cards
		for (CardSuit suit : CardSuit.values())
		{
			for (CardValue value : CardValue.values())
			{
				deck.drawCard(suit, value);
			}
		}

		try
		{
			// Attempt to draw a card, doesn't matter which
			deck.drawCard(null, null);
			fail("The code before this line should have thrown an exception.");
		}
		catch (DeckException e)
		{
			assertEquals(DeckExceptionCause.DECK_IS_EMPTY, e.getDeckExceptionCause());
		}
	}

	/**
	 * drawNextDrawsCard
	 *
	 * @throws DeckException error case
	 */
	@Test
	void drawNextDrawsCard() throws DeckException
	{
		DeckOfCards deck = new DeckOfCards();
		Card card = deck.drawNextCard();

		assertNotNull(card, "Card drawn from deck should not be null.");
	}

	/**
	 * drawNextRemovesCardFomDeck
	 *
	 * @throws DeckException error case
	 */
	@Test
	void drawNextRemovesCardFomDeck() throws DeckException
	{
		DeckOfCards deck = new DeckOfCards();
		int sizeBefore = deck.getAmountOfCardsLeft();

		deck.drawNextCard();
		int sizeAfter = deck.getAmountOfCardsLeft();

		assertEquals(sizeBefore - 1, sizeAfter,
				"After drawing one card, said card should have been removed from the deck.");
	}

	/**
	 * drawNextThrowsExceptionOnAnEmptyDeck
	 *
	 * @throws DeckException error case
	 */
	@Test
	void drawNextThrowsExceptionOnAnEmptyDeck() throws DeckException
	{
		DeckOfCards deck = new DeckOfCards();

		// draw all cards in the deck
		for (int i = 0; i < DeckOfCards.DECK_SIZE; i++)
		{
			deck.drawNextCard();
		}

		// attempt to draw one more time. Exception expected.
		try
		{
			deck.drawNextCard();
			fail("Deck is empty. Draw attempt should have thrown exception.");
		}
		catch (DeckException e)
		{
			assertEquals(DeckExceptionCause.DECK_IS_EMPTY, e.getDeckExceptionCause(),
					"Drawing from an empty deck should throw an according exception.");
		}
	}
}
