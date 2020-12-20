package com.yotilla.poker;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.LinkedList;
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
	 * Creates and returns a pseudo - randomly generated card mock. Does in no way
	 * guarantee the cards to be unique.
	 *
	 * @return a card mock
	 */
	private Card getRandomCardMock()
	{
		// roll the dice for a card value between 2 and 14.
		int numericalCardValue = (int) Math.ceil((Math.random()) * 13 + 1);

		// correct for the very unlikely case that random() returned zero.
		if (numericalCardValue == 1)
		{
			numericalCardValue = 2;
		}

		CardValue cardValue = CardValue.getByNumericalValue(numericalCardValue);

		// roll again for the suit
		int suitValue = (int) Math.ceil(Math.random() * 4);
		CardSuit suit;

		switch (suitValue) {
		case 1:
			suit = CardSuit.CLUBS;
			break;
		case 2:
			suit = CardSuit.DIAMONDS;
			break;
		case 3:
			suit = CardSuit.HEARTS;
			break;
		case 4:
			suit = CardSuit.SPADES;
			break;
		default:
			suit = null;
			break;
		}

		return getCardMock(suit, cardValue);
	}

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
			mocks.add(getRandomCardMock());
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
			mocks[i] = getRandomCardMock();
		}

		return mocks;
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

	/**
	 * settingSixCardsThrowsException
	 */
	@Test
	public void settingSixCardsThrowsException()
	{
		HandOfCards hand = new HandOfCards();

		List<Card> tooManyCards = getRandomCardMocksAsList(HandOfCards.MAX_HAND_SIZE + 1);

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
	public void settingFiveCardsWorksFine() throws HandExceededException
	{
		HandOfCards hand = new HandOfCards();
		List<Card> maximumCards = getRandomCardMocksAsList(HandOfCards.MAX_HAND_SIZE);

		hand.setCards(maximumCards);

		assertTrue(hand.getCards().equals(maximumCards), "Adding this many cards shouldn't be a problem.");
	}

	/**
	 * addingSixCardsAtOnceThrowsException
	 */
	@Test
	public void addingSixCardsAtOnceThrowsException()
	{
		HandOfCards hand = new HandOfCards();

		Card[] tooManyCards = getRandomCardMocksAsArray(HandOfCards.MAX_HAND_SIZE + 1);

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
	public void addingFiveCardsAtOnceIsFine() throws HandExceededException
	{
		HandOfCards hand = new HandOfCards();
		Card[] maximumCards = getRandomCardMocksAsArray(HandOfCards.MAX_HAND_SIZE);

		hand.addCards(maximumCards);
		assertTrue(hand.getCards().size() == maximumCards.length, "Adding this many cards shouldn't be a problem.");
	}

	/**
	 * addingTheSixthCardThrowsException
	 *
	 * @throws HandExceededException first error, then expected
	 */
	@Test
	public void addingTheSixthCardThrowsException() throws HandExceededException
	{
		// Create a hand and add five cards
		HandOfCards hand = new HandOfCards();
		Card[] maximumCards = getRandomCardMocksAsArray(HandOfCards.MAX_HAND_SIZE);

		hand.addCards(maximumCards);

		assertThrows(HandExceededException.class, () -> {
			hand.addCard(getRandomCardMock());
		}, "Adding a sixth card to this hand should have thrown an exception.");
	}
}
