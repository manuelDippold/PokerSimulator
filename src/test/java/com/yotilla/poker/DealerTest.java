package com.yotilla.poker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.yotilla.poker.card.CardSuit;
import com.yotilla.poker.card.CardValue;
import com.yotilla.poker.card.DeckOfCards;
import com.yotilla.poker.card.HandOfCards;
import com.yotilla.poker.error.DeckException;
import com.yotilla.poker.error.HandExceededException;

/**
 * Description:
 *
 * <br>
 * Date: 26.12.2020
 *
 * @author Manuel
 *
 */
class DealerTest
{
	private DeckOfCards deck;

	/**
	 * Spin up a fresh deck before each test
	 */
	@BeforeEach
	private void setUp()
	{
		deck = new DeckOfCards();
	}

	/**
	 * Fill the hand with cards from the deck
	 *
	 * @param hand hand to fill
	 * @throws DeckException         if deck was emptied and we needed more cards
	 * @throws HandExceededException error case
	 */
	private void fillHandFromDeck(final HandOfCards hand) throws HandExceededException, DeckException
	{
		if (hand == null)
		{
			return;
		}

		while (hand.getAmountOfCards() < HandOfCards.MAX_HAND_SIZE)
		{
			hand.addCard(deck.drawNextCard());
		}
	}

	/**
	 * recognizeHighCard
	 *
	 * @throws HandExceededException error case
	 * @throws DeckException         error case
	 */
	@Test
	void recognizeHighCard() throws HandExceededException, DeckException
	{
		HandOfCards hand = new HandOfCards();

		// An Ace...
		hand.addCard(deck.drawCard(CardSuit.DIAMONDS, CardValue.ACE));

		// ...and four random cards
		fillHandFromDeck(hand);

		CardValue result = new Dealer().getHighCard(hand);

		assertEquals(CardValue.ACE, result,
				"There is an ace in this hand, it ought to be the high card, no matter what.");
	}

	/**
	 * recognizeHighCardIsNullSafe
	 */
	@Test
	void recognizeHighCardIsNullSafe()
	{
		HandOfCards hand = new HandOfCards();
		CardValue result = new Dealer().getHighCard(hand);

		assertNull(result, "Empty hand, no high card");
	}

	/**
	 * pairIsRecognized
	 *
	 * @throws HandExceededException error case
	 * @throws DeckException         error case
	 */
	@Test
	void pairIsRecognized() throws HandExceededException, DeckException
	{
		HandOfCards hand = new HandOfCards();

		// A pair
		hand.addCard(deck.drawCard(CardSuit.DIAMONDS, CardValue.ACE));
		hand.addCard(deck.drawCard(CardSuit.CLUBS, CardValue.ACE));

		// and three random cards
		fillHandFromDeck(hand);

		CardValue result = new Dealer().getPair(hand);

		assertEquals(CardValue.ACE, result, "This hand definitely contains a pair of aces.");
	}

	/**
	 * noPairIsrecognizedIfThereIsNone
	 *
	 * @throws HandExceededException error
	 * @throws DeckException         error
	 */
	@Test
	void noPairIsrecognizedIfThereIsNone() throws HandExceededException, DeckException
	{
		HandOfCards hand = new HandOfCards();

		// Add five distinct Cards, make sure there is no pair.
		hand.addCard(deck.drawCard(CardSuit.SPADES, CardValue.TWO));
		hand.addCard(deck.drawCard(CardSuit.SPADES, CardValue.THREE));
		hand.addCard(deck.drawCard(CardSuit.HEARTS, CardValue.SIX));
		hand.addCard(deck.drawCard(CardSuit.DIAMONDS, CardValue.EIGHT));
		hand.addCard(deck.drawCard(CardSuit.SPADES, CardValue.NINE));

		CardValue result = new Dealer().getPair(hand);

		assertNull(result, "This hand does not contain a pair.");
	}

	/**
	 * copyIsNullSafe
	 *
	 * @throws HandExceededException error case
	 */
	@Test
	void copyIsNullSafe() throws HandExceededException
	{
		HandOfCards result = new Dealer().copyHandOfCards(null);
		assertNull(result, "A copy of null should be null.");
	}

	/**
	 * copyDuplicatesObjects
	 *
	 * @throws DeckException         error case
	 * @throws HandExceededException error case
	 */
	@Test
	void copyDuplicatesObjects() throws HandExceededException, DeckException
	{
		HandOfCards hand = new HandOfCards();
		hand.addCard(deck.drawCard(CardSuit.SPADES, CardValue.TWO));
		hand.addCard(deck.drawCard(CardSuit.SPADES, CardValue.THREE));
		hand.addCard(deck.drawCard(CardSuit.HEARTS, CardValue.SIX));

		HandOfCards copy = new Dealer().copyHandOfCards(hand);

		for (int i = 0; i < 3; i++)
		{
			assertEquals(hand.getCards().get(i), copy.getCards().get(i), "These two cards should be equal.");
			assertNotSame(hand.getCards().get(i), copy.getCards().get(i),
					"These two cards should be equal, but not identical.");
		}
	}

	/**
	 * twoPairsAreRecognized
	 *
	 * @throws HandExceededException error case
	 * @throws DeckException         error case
	 */
	@Test
	void twoPairsAreRecognized() throws HandExceededException, DeckException
	{
		HandOfCards hand = new HandOfCards();

		// Two pairs
		hand.addCard(deck.drawCard(CardSuit.DIAMONDS, CardValue.ACE));
		hand.addCard(deck.drawCard(CardSuit.CLUBS, CardValue.ACE));

		hand.addCard(deck.drawCard(CardSuit.SPADES, CardValue.KING));
		hand.addCard(deck.drawCard(CardSuit.HEARTS, CardValue.KING));

		// A filler Card
		hand.addCard(deck.drawCard(CardSuit.DIAMONDS, CardValue.FOUR));

		CardValue result = new Dealer().getTwoPairs(hand);

		assertEquals(CardValue.ACE, result,
				"This hand had two pairs, one of them was of aces. This should be the result.");
	}
}
