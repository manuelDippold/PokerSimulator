package com.yotilla.poker.card;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.yotilla.poker.TestUtils;
import com.yotilla.poker.error.HandExceededException;

/**
 * Description: Test hand mechanics <br>
 * Date: 20.12.2020
 *
 * @author Manuel
 *
 */
class HandOfCardsTest
{
	/**
	 * Create a list filled with randomly - generated cards
	 *
	 * @param argAmountOfCards amount of cards to be added to the list. Positive
	 *                         integer expected, negatives will be assumed zero.
	 * @return list of card mocks
	 */
	private List<Card> getRandomCardMocksAsList(final int argAmountOfCards)
	{
		List<Card> mocks = new LinkedList<>();

		int amountOfCards = argAmountOfCards;

		if (amountOfCards < 0)
		{
			amountOfCards = 0;
		}

		for (int i = 0; i < amountOfCards; i++)
		{
			mocks.add(TestUtils.getRandomCardMock());
		}

		return mocks;
	}

	/**
	 * Create an array filled with randomly - generated cards
	 *
	 * @param argAmountOfCards amount of cards to be added to the array. Positive
	 *                         integer expected, negatives will be assumed zero.
	 * @return array of card mocks
	 */
	private Card[] getRandomCardMocksAsArray(final int argAmountOfCards)
	{
		int amountOfCards = argAmountOfCards;

		if (amountOfCards < 0)
		{
			amountOfCards = 0;
		}

		Card[] mocks = new Card[amountOfCards];

		for (int i = 0; i < amountOfCards; i++)
		{
			mocks[i] = TestUtils.getRandomCardMock();
		}

		return mocks;
	}

	/**
	 * addCardsCreatesCardsListIfThereIsNone
	 *
	 * @throws HandExceededException error case
	 */
	@Test
	void addCardsCreatesCardsListIfThereIsNone() throws HandExceededException
	{
		// Create a new, empty hand
		HandOfCards hand = new HandOfCards();

		// Assert that this hand is indeed empty.
		assertTrue(hand.getCards().isEmpty(), "New hand, shouldn't hold any cards");

		// Add a card.
		hand.addCards(TestUtils.getCardMock(CardSuit.SPADES, CardValue.SEVEN));

		// An internal list has been created to store the cards.
		assertNotNull(hand.getCards(), "We added a card, there ought to be a collection of cards now.");
	}

	/**
	 * addCardsActuallyAddsACard
	 *
	 * @throws HandExceededException error case
	 */
	@Test
	void addCardActuallyAddsACard() throws HandExceededException
	{
		HandOfCards hand = new HandOfCards();

		Card aceOfSpades = TestUtils.getCardMock(CardSuit.SPADES, CardValue.ACE);
		hand.addCard(aceOfSpades);

		assertTrue(hand.getCards().contains(aceOfSpades));
	}

	/**
	 * addCardIsNullSafe
	 *
	 * @throws HandExceededException error case
	 */
	@Test
	void addCardIsNullSafe() throws HandExceededException
	{
		// Create a new hand and assure it is empty
		HandOfCards hand = new HandOfCards();
		assertTrue(hand.getCards().isEmpty(), "New hand, shouldn't hold any cards");

		// add nothing
		hand.addCard(null);

		// assure hand is still empty
		assertTrue(hand.getCards().isEmpty(), "Adding null as a card doesn't make sense, hand ought to ignore that.");
	}

	/**
	 * addCardsActuallyAddsMultipleCards
	 *
	 * @throws HandExceededException error case
	 */
	@Test
	void addCardsActuallyAddsMultipleCards() throws HandExceededException
	{
		HandOfCards hand = new HandOfCards();

		Card aceOfSpades = TestUtils.getCardMock(CardSuit.SPADES, CardValue.ACE);
		Card aceOfHearts = TestUtils.getCardMock(CardSuit.HEARTS, CardValue.ACE);

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
	void addCardsIsNullsafe() throws HandExceededException
	{
		HandOfCards hand = new HandOfCards();
		hand.addCards((Card[]) null);

		assertTrue(hand.getCards().isEmpty(), "Adding null as cards doesn't make sense, hand ought to ignore that.");
	}

	/**
	 * settingSixCardsThrowsException
	 */
	@Test
	void settingSixCardsThrowsException()
	{
		HandOfCards hand = new HandOfCards();

		List<Card> tooManyCards = getRandomCardMocksAsList(HandOfCards.HAND_SIZE + 1);

		assertThrows(HandExceededException.class, () -> {
			hand.setCards(tooManyCards);
		}, "Setting the hand to six cards ought to throw an exception.");
	}

	/**
	 * settingFiveCardsWorksFine
	 *
	 * @throws HandExceededException error case
	 */
	@Test
	void settingFiveCardsWorksFine() throws HandExceededException
	{
		HandOfCards hand = new HandOfCards();
		List<Card> maximumCards = getRandomCardMocksAsList(HandOfCards.HAND_SIZE);

		hand.setCards(maximumCards);

		assertEquals(maximumCards, hand.getCards(), "Adding this many cards shouldn't be a problem.");
	}

	/**
	 * addingSixCardsAtOnceThrowsException
	 */
	@Test
	void addingSixCardsAtOnceThrowsException()
	{
		HandOfCards hand = new HandOfCards();

		Card[] tooManyCards = getRandomCardMocksAsArray(HandOfCards.HAND_SIZE + 1);

		assertThrows(HandExceededException.class, () -> {
			hand.addCards(tooManyCards);
		}, "Setting the hand to six cards ought to throw an exception.");
	}

	/**
	 * addingFiveCardsAtOnceIsFine
	 *
	 * @throws HandExceededException error case
	 */
	@Test
	void addingFiveCardsAtOnceIsFine() throws HandExceededException
	{
		HandOfCards hand = new HandOfCards();
		Card[] maximumCards = getRandomCardMocksAsArray(HandOfCards.HAND_SIZE);

		hand.addCards(maximumCards);
		assertEquals(maximumCards.length, hand.getCards().size(), "Adding this many cards shouldn't be a problem.");
	}

	/**
	 * addingTheSixthCardThrowsException
	 *
	 * @throws HandExceededException first error, then expected
	 */
	@Test
	void addingTheSixthCardThrowsException() throws HandExceededException
	{
		// Create a hand and add five cards
		HandOfCards hand = new HandOfCards();
		Card[] maximumCards = getRandomCardMocksAsArray(HandOfCards.HAND_SIZE);

		hand.addCards(maximumCards);

		assertThrows(HandExceededException.class, () -> {
			hand.addCard(TestUtils.getRandomCardMock());
		}, "Adding a sixth card to this hand should have thrown an exception.");
	}
}
