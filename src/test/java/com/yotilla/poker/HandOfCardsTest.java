package com.yotilla.poker;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.yotilla.poker.card.Card;
import com.yotilla.poker.card.CardSuit;
import com.yotilla.poker.card.CardValue;
import com.yotilla.poker.error.HandExceededException;

/**
 * Description: Test hand mechanics <br>
 * Date: 20.12.2020
 *
 * @author Manuel
 *
 */
public class HandOfCardsTest
{
	/**
	 * Create and return the mock of a card with the specified suit and value
	 *
	 * @param argCardSuit  desired card suit
	 * @param argCardValue desired card value
	 * @return a mocked card
	 */
	private Card getCardMock(final CardSuit argCardSuit, final CardValue argCardValue)
	{
		Card card = Mockito.mock(Card.class);
		Mockito.when(card.getCardSuit()).thenReturn(argCardSuit);
		Mockito.when(card.getCardValue()).thenReturn(argCardValue);

		return card;
	}

	/**
	 * addCardsCreatesCardsListIfThereIsNone
	 *
	 * @throws HandExceededException error case
	 */
	@Test
	public void addCardsCreatesCardsListIfThereIsNone() throws HandExceededException
	{
		// Create a new, empty hand
		HandOfCards hand = new HandOfCards();

		// Assert that this hand is indeed empty.
		assertNull(hand.getCards(), "New hand, shouldn't hold any cards");

		// Add a card.
		hand.addCards(getCardMock(CardSuit.SPADES, CardValue.SEVEN));

		// An internal list has been created to store the cards.
		assertNotNull(hand.getCards(), "We added a card, there ought to be a collection of cards now.");
	}

	/**
	 * addCardsActuallyAddsACard
	 *
	 * @throws HandExceededException error case
	 */
	@Test
	public void addCardActuallyAddsACard() throws HandExceededException
	{
		HandOfCards hand = new HandOfCards();

		Card aceOfSpades = getCardMock(CardSuit.SPADES, CardValue.ACE);
		hand.addCard(aceOfSpades);

		assertTrue(hand.getCards().contains(aceOfSpades));
	}

	/**
	 * addCardIsNullSafe
	 *
	 * @throws HandExceededException error case
	 */
	@Test
	public void addCardIsNullSafe() throws HandExceededException
	{
		// Create a new hand and assure it is empty
		HandOfCards hand = new HandOfCards();
		assertNull(hand.getCards(), "New hand, shouldn't hold any cards");

		// add nothing
		hand.addCard(null);

		// assure hand is still empty
		assertNull(hand.getCards(), "Adding null as a card doesn't make sense, hand ought to ignore that.");
	}

	/**
	 * addCardsActuallyAddsMultipleCards
	 *
	 * @throws HandExceededException error case
	 */
	@Test
	public void addCardsActuallyAddsMultipleCards() throws HandExceededException
	{
		HandOfCards hand = new HandOfCards();

		Card aceOfSpades = getCardMock(CardSuit.SPADES, CardValue.ACE);
		Card aceOfHearts = getCardMock(CardSuit.HEARTS, CardValue.ACE);

		hand.addCards(aceOfSpades, aceOfHearts);

		assertTrue(hand.getCards().containsAll(Arrays.asList(aceOfHearts, aceOfHearts)),
				"We added two cards to this hand, both ought to be in there.");
	}

	/**
	 * addCardsIsNullsafe
	 *
	 * @throws HandExceededException in case of an error
	 */
	@Test
	public void addCardsIsNullsafe() throws HandExceededException
	{
		HandOfCards hand = new HandOfCards();
		hand.addCards((Card[]) null);

		assertNull(hand.getCards(), "Adding null as cards doesn't make sense, hand ought to ignore that.");
	}

	@Test
	public void addingSixCardsAtOnceThrowsException()
	{
		List<Card> sixCards = new ArrayList<Card>(Arrays.asList(getCardMock(CardSuit.HEARTS, CardValue.FIVE),
				getCardMock(CardSuit.DIAMONDS, CardValue.SIX), getCardMock(CardSuit.HEARTS, CardValue.QUEEN),
				getCardMock(CardSuit.SPADES, CardValue.JACK), getCardMock(CardSuit.DIAMONDS, CardValue.TEN),
				getCardMock(CardSuit.CLUBS, CardValue.SEVEN)));
	}
}
