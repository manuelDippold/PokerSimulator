package com.yotilla.poker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.yotilla.poker.card.Card;
import com.yotilla.poker.card.CardSuit;
import com.yotilla.poker.card.CardValue;
import com.yotilla.poker.card.DeckOfCards;
import com.yotilla.poker.card.HandOfCards;
import com.yotilla.poker.error.DeckException;
import com.yotilla.poker.error.HandExceededException;
import com.yotilla.poker.result.PokerHand;
import com.yotilla.poker.result.PokerHandRanking;

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
	// sut: subject under test.
	private Dealer sut;
	private DeckOfCards deck;

	/**
	 * Spin up a fresh deck and dealer before each test
	 */
	@BeforeEach
	private void setUp()
	{
		deck = new DeckOfCards();
		sut = new Dealer();
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

		// ...and four furhter cards
		hand.addCard(deck.drawCard(CardSuit.DIAMONDS, CardValue.THREE));
		hand.addCard(deck.drawCard(CardSuit.SPADES, CardValue.FIVE));
		hand.addCard(deck.drawCard(CardSuit.CLUBS, CardValue.EIGHT));
		hand.addCard(deck.drawCard(CardSuit.DIAMONDS, CardValue.JACK));

		PokerHand result = sut.getHighCard(hand);

		assertEquals(PokerHandRanking.HIGH_CARD, result.getRanking(), "This is supposed to be a high card ranking.");
		assertEquals(CardValue.ACE, result.getRankCards().get(0),
				"There is an ace in this hand, it ought to be the high card, no matter what.");
		assertEquals(CardValue.JACK, result.getRankCards().get(1), "Second card: Jack.");
		assertEquals(CardValue.EIGHT, result.getRankCards().get(2), "Third card: Eight.");
		assertEquals(CardValue.FIVE, result.getRankCards().get(3), "Fourth card: Five.");
		assertEquals(CardValue.THREE, result.getRankCards().get(4), "Fifth card: Three.");
	}

	/**
	 * recognizeHighCardIsNullSafe
	 */
	@Test
	void recognizeHighCardIsNullSafe()
	{
		PokerHand result = sut.getHighCard(null);

		assertNull(result, "Empty hand, no high card");
	}

	/**
	 * getKickerCardsIsNullSafe
	 *
	 * @throws HandExceededException error case
	 * @throws DeckException         error case
	 */
	@Test
	void getKickerCardsIsNullSafe() throws HandExceededException, DeckException
	{
		List<Card> kickerCards = sut.getKickerCards(null, null);
		assertTrue(kickerCards.isEmpty(), "null results in null");

		HandOfCards hand = new HandOfCards();
		fillHandFromDeck(hand);
		kickerCards = sut.getKickerCards(hand, null);
		assertFalse(kickerCards.isEmpty(), "This should result in the full hand being returned.");

		kickerCards = sut.getKickerCards(null, kickerCards);
		assertTrue(kickerCards.isEmpty(), "A hand of null should always result in null.");
	}

	/**
	 *
	 * getKickerCardsFromPair
	 *
	 * @throws HandExceededException error case
	 * @throws DeckException         error case
	 */
	@Test
	void getKickerCardsFromPair() throws HandExceededException, DeckException
	{
		HandOfCards hand = new HandOfCards();

		// add a pair to the hand, then fill it up randomly.
		Card aceOfHearts = deck.drawCard(CardSuit.HEARTS, CardValue.ACE);
		Card aceOfDiamonds = deck.drawCard(CardSuit.DIAMONDS, CardValue.ACE);

		hand.addCard(aceOfHearts);
		hand.addCard(aceOfDiamonds);
		List<Card> partOfRanking = Arrays.asList(aceOfHearts, aceOfDiamonds);

		// add three other cards. These should come back as kickers,
		// in descending order.
		Card fiveOfSpades = deck.drawCard(CardSuit.SPADES, CardValue.FIVE);
		Card nineOfSpades = deck.drawCard(CardSuit.SPADES, CardValue.NINE);
		Card kingOfHearts = deck.drawCard(CardSuit.HEARTS, CardValue.KING);

		hand.addCard(fiveOfSpades);
		hand.addCard(nineOfSpades);
		hand.addCard(kingOfHearts);

		List<Card> kickerCards = sut.getKickerCards(hand, partOfRanking);

		assertSame(3, kickerCards.size(), "A hand of five and a pair of two should leave three kicker cards.");

		Iterator<Card> iter = kickerCards.iterator();

		assertEquals(kingOfHearts, iter.next(), "First kicker card should be the king of hearts.");
		assertEquals(nineOfSpades, iter.next(), "Second kicker card should be the nine of spades.");
		assertEquals(fiveOfSpades, iter.next(), "Third kicker card should be the five of spades.");
	}

	/**
	 * getKickerCardsFromStraight
	 *
	 * @throws HandExceededException error case
	 * @throws DeckException         error case
	 */
	@Test
	void getKickerCardsFromStraight() throws HandExceededException, DeckException
	{
		HandOfCards hand = new HandOfCards();

		// Draw a straight
		hand.addCard(deck.drawCard(CardSuit.SPADES, CardValue.ACE));
		hand.addCard(deck.drawCard(CardSuit.HEARTS, CardValue.KING));
		hand.addCard(deck.drawCard(CardSuit.SPADES, CardValue.QUEEN));
		hand.addCard(deck.drawCard(CardSuit.CLUBS, CardValue.JACK));
		hand.addCard(deck.drawCard(CardSuit.CLUBS, CardValue.TEN));

		// This should leave no kicker cards.
		List<Card> kickerCards = sut.getKickerCards(hand, hand.getCards());
		assertTrue(kickerCards.isEmpty(), "Five cards leave no room for kicker cards.");
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

		PokerHand result = sut.getPair(hand);

		assertEquals(PokerHandRanking.ONE_PAIR, result.getRanking(), "This hand definitely contains a pair of aces.");

		assertEquals(CardValue.ACE, result.getRankCards().iterator().next(),
				"This hand definitely contains a pair of aces.");

		assertSame(3, result.getKickerCards().size(), "One Pair should leave three kicker cards.");
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

		PokerHand result = new Dealer().getPair(hand);
		assertNull(result, "This hand does not contain a pair.");
	}

	/**
	 * getPairIsNullSafe
	 */
	@Test
	void getPairIsNullSafe()
	{
		PokerHand result = new Dealer().getPair(null);
		assertNull(result, "This hand does not contain a pair. In fact, there isn't even a hand.");
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

		PokerHand result = sut.getTwoPairs(hand);

		assertEquals(PokerHandRanking.TWO_PAIRS, result.getRanking(),
				"This hand had two pairs, one of them was of aces. This should be the result.");

		assertEquals(CardValue.ACE, result.getRankCards().get(0),
				"This hand had two pairs, one of them was of aces. This should be the result.");

		assertEquals(CardValue.KING, result.getRankCards().get(1),
				"This hand had two pairs, the second one was of kings. This should be the result.");

		assertEquals(CardValue.FOUR, result.getKickerCards().get(0),
				"The high card of the hand was a four. This shoul reflect in the result.");
	}

	/**
	 * twoPairsNotRecognizedIfThereIsOnlyOne
	 *
	 * @throws HandExceededException error
	 * @throws DeckException         error
	 */
	@Test
	void twoPairsNotRecognizedIfThereIsOnlyOne() throws HandExceededException, DeckException
	{
		HandOfCards hand = new HandOfCards();

		// one pair
		hand.addCard(deck.drawCard(CardSuit.DIAMONDS, CardValue.ACE));
		hand.addCard(deck.drawCard(CardSuit.CLUBS, CardValue.ACE));

		// Three filler Cards
		hand.addCard(deck.drawCard(CardSuit.SPADES, CardValue.JACK));
		hand.addCard(deck.drawCard(CardSuit.HEARTS, CardValue.KING));
		hand.addCard(deck.drawCard(CardSuit.DIAMONDS, CardValue.FOUR));

		PokerHand result = sut.getTwoPairs(hand);
		assertNull(result, "Without two pairs, result should be null.");
	}

	/**
	 * twoPairsIsNullSafe
	 *
	 * @throws HandExceededException error case
	 * @throws DeckException         error case
	 */
	@Test
	void twoPairsIsNullSafe() throws HandExceededException, DeckException
	{
		PokerHand result = sut.getTwoPairs(null);
		assertNull(result, "This hand does not contain a pair, much less two. In fact, there isn't even a hand.");
	}

	/**
	 * threeOfKindIsNullSafe
	 */
	@Test
	void threeOfKindIsNullSafe()
	{
		PokerHand result = sut.getThreeOfKind(null);
		assertNull(result, "When analyzing null, result should be null");
	}

	/**
	 * tripleIsRecognized
	 *
	 * @throws HandExceededException error
	 * @throws DeckException         error
	 */
	@Test
	void tripleIsRecognized() throws HandExceededException, DeckException
	{
		HandOfCards hand = new HandOfCards();

		// Three of a kind
		hand.addCard(deck.drawCard(CardSuit.HEARTS, CardValue.TEN));
		hand.addCard(deck.drawCard(CardSuit.SPADES, CardValue.TEN));
		hand.addCard(deck.drawCard(CardSuit.DIAMONDS, CardValue.TEN));

		// Two Kicker cards
		hand.addCard(deck.drawCard(CardSuit.CLUBS, CardValue.TWO));
		hand.addCard(deck.drawCard(CardSuit.DIAMONDS, CardValue.FIVE));

		PokerHand result = sut.getThreeOfKind(hand);

		assertEquals(PokerHandRanking.THREE_OF_A_KIND, result.getRanking(), "Three of a kind is the expected result.");
		assertEquals(CardValue.TEN, result.getRankCards().get(0), "A triple of tens, result should reflect the ten.");

		assertEquals(CardValue.FIVE, result.getKickerCards().get(0), "First kicker card should be a five.");
		assertEquals(CardValue.TWO, result.getKickerCards().get(1), "Second kicker card should be a two.");
	}

	/**
	 * tripleIsNotrecognizedIfThereIsNone
	 *
	 * @throws HandExceededException error
	 * @throws DeckException         error
	 */
	@Test
	void tripleIsNotrecognizedIfThereIsNone() throws HandExceededException, DeckException
	{
		HandOfCards hand = new HandOfCards();

		// Two pairs
		hand.addCard(deck.drawCard(CardSuit.DIAMONDS, CardValue.ACE));
		hand.addCard(deck.drawCard(CardSuit.CLUBS, CardValue.ACE));

		hand.addCard(deck.drawCard(CardSuit.SPADES, CardValue.KING));
		hand.addCard(deck.drawCard(CardSuit.HEARTS, CardValue.KING));

		// A filler Card
		hand.addCard(deck.drawCard(CardSuit.DIAMONDS, CardValue.FOUR));

		PokerHand result = sut.getThreeOfKind(hand);
		assertNull(result, "Without a triple, result should be null.");
	}

	/**
	 * getStraightIsNullSafe
	 *
	 * @throws HandExceededException error case
	 */
	@Test
	void getStraightIsNullSafe() throws HandExceededException
	{
		PokerHand result = sut.getStraight(null);
		assertNull(result, "When analyzing null, result should be null");
	}

	/**
	 * straightIsRecognized
	 *
	 * @throws HandExceededException error
	 * @throws DeckException         error
	 */
	@Test
	void straightIsRecognized() throws HandExceededException, DeckException
	{
		HandOfCards hand = new HandOfCards();

		// Straight
		hand.addCard(deck.drawCard(CardSuit.DIAMONDS, CardValue.THREE));
		hand.addCard(deck.drawCard(CardSuit.CLUBS, CardValue.FOUR));
		hand.addCard(deck.drawCard(CardSuit.SPADES, CardValue.FIVE));
		hand.addCard(deck.drawCard(CardSuit.DIAMONDS, CardValue.SIX));
		hand.addCard(deck.drawCard(CardSuit.CLUBS, CardValue.SEVEN));

		PokerHand result = sut.getStraight(hand);

		assertEquals(PokerHandRanking.STRAIGHT, result.getRanking(), "This should result in a straight");
		assertEquals(CardValue.SEVEN, result.getRankCards().get(0),
				"Straight is lead by a seven, result should reflect that.");
		assertTrue(result.getKickerCards().isEmpty(), "A Straight does not leave room for kicker cards.");
	}

	/**
	 * straightEndingWithAnAceIsRecognized
	 *
	 * @throws HandExceededException error case
	 * @throws DeckException         error case
	 */
	@Test
	void straightEndingWithAnAceIsRecognized() throws HandExceededException, DeckException
	{
		HandOfCards hand = new HandOfCards();

		// Straight
		hand.addCard(deck.drawCard(CardSuit.DIAMONDS, CardValue.ACE));
		hand.addCard(deck.drawCard(CardSuit.CLUBS, CardValue.KING));
		hand.addCard(deck.drawCard(CardSuit.SPADES, CardValue.QUEEN));
		hand.addCard(deck.drawCard(CardSuit.DIAMONDS, CardValue.JACK));
		hand.addCard(deck.drawCard(CardSuit.CLUBS, CardValue.TEN));

		PokerHand result = sut.getStraight(hand);

		assertEquals(PokerHandRanking.STRAIGHT, result.getRanking(), "This should result in a straight");
		assertEquals(CardValue.ACE, result.getRankCards().get(0),
				"Straight is lead by an Ace, result should reflect that.");
		assertTrue(result.getKickerCards().isEmpty(), "A Straight does not leave room for kicker cards.");
	}

	/**
	 * straightIsNotRecognizedWhenThereIsNone
	 *
	 * @throws HandExceededException error
	 * @throws DeckException         error
	 */
	@Test
	void straightIsNotRecognizedWhenThereIsNone() throws HandExceededException, DeckException
	{
		HandOfCards hand = new HandOfCards();

		// Not a straight
		hand.addCard(deck.drawCard(CardSuit.DIAMONDS, CardValue.FIVE));
		hand.addCard(deck.drawCard(CardSuit.CLUBS, CardValue.KING));
		hand.addCard(deck.drawCard(CardSuit.SPADES, CardValue.THREE));
		hand.addCard(deck.drawCard(CardSuit.DIAMONDS, CardValue.JACK));
		hand.addCard(deck.drawCard(CardSuit.CLUBS, CardValue.TEN));

		PokerHand result = sut.getStraight(hand);
		assertNull(result, "Without a straight, result should be null.");
	}

	/**
	 * StraightStartingWithAnAceIsRecognized
	 *
	 * @throws HandExceededException error case
	 * @throws DeckException         error case
	 */
	@Test
	void StraightStartingWithAnAceIsRecognized() throws HandExceededException, DeckException
	{
		HandOfCards hand = new HandOfCards();

		// Straight, starting with an ace
		hand.addCard(deck.drawCard(CardSuit.DIAMONDS, CardValue.ACE));
		hand.addCard(deck.drawCard(CardSuit.CLUBS, CardValue.TWO));
		hand.addCard(deck.drawCard(CardSuit.SPADES, CardValue.THREE));
		hand.addCard(deck.drawCard(CardSuit.DIAMONDS, CardValue.FOUR));
		hand.addCard(deck.drawCard(CardSuit.CLUBS, CardValue.FIVE));

		PokerHand result = sut.getStraight(hand);

		assertEquals(PokerHandRanking.STRAIGHT, result.getRanking(), "This should result in a straight");
		assertEquals(CardValue.FIVE, result.getRankCards().get(0),
				" Straight contains an Ace, but this in this special case, the ace acts as 'one', so the ranking card is the five. The result should reflect that.");
		assertTrue(result.getKickerCards().isEmpty(), "A Straight does not leave room for kicker cards.");
	}

	/**
	 * getFlushIsNullSafe
	 *
	 * @throws HandExceededException error case
	 */
	@Test
	void getFlushIsNullSafe() throws HandExceededException
	{
		PokerHand result = sut.getFlush(null);
		assertNull(result, "When analyzing null, result should be null");
	}

	/**
	 * flushIsRecognized
	 *
	 * @throws HandExceededException error
	 * @throws DeckException         error
	 */
	@Test
	void flushIsRecognized() throws HandExceededException, DeckException
	{
		HandOfCards hand = new HandOfCards();

		// Flush, lead by a King
		hand.addCard(deck.drawCard(CardSuit.DIAMONDS, CardValue.KING));
		hand.addCard(deck.drawCard(CardSuit.DIAMONDS, CardValue.THREE));
		hand.addCard(deck.drawCard(CardSuit.DIAMONDS, CardValue.NINE));
		hand.addCard(deck.drawCard(CardSuit.DIAMONDS, CardValue.FOUR));
		hand.addCard(deck.drawCard(CardSuit.DIAMONDS, CardValue.EIGHT));

		PokerHand result = sut.getFlush(hand);

		assertEquals(PokerHandRanking.FLUSH, result.getRanking(), "This should result in a flush");
		assertEquals(CardValue.KING, result.getRankCards().get(0),
				" Highest card in this flush is a king. The result should reflect that.");
		assertTrue(result.getKickerCards().isEmpty(), "A Straight does not leave room for kicker cards.");
	}

	/**
	 * flushIsNotRecognizedWhenThereIsNone
	 *
	 * @throws HandExceededException error
	 * @throws DeckException         error
	 */
	@Test
	void flushIsNotRecognizedWhenThereIsNone() throws HandExceededException, DeckException
	{
		HandOfCards hand = new HandOfCards();

		// Not a flush.
		hand.addCard(deck.drawCard(CardSuit.DIAMONDS, CardValue.KING));
		hand.addCard(deck.drawCard(CardSuit.SPADES, CardValue.THREE));
		hand.addCard(deck.drawCard(CardSuit.DIAMONDS, CardValue.NINE));
		hand.addCard(deck.drawCard(CardSuit.CLUBS, CardValue.FOUR));
		hand.addCard(deck.drawCard(CardSuit.DIAMONDS, CardValue.EIGHT));

		PokerHand result = sut.getFlush(hand);
		assertNull(result, "Without a flush, result should be null.");
	}

	// TODO: full house
	// TODO: no full house
	// TODO: Four
	// TODO: Not Four
	// TODO: straight flush
	// TODO: not straight flush
	// TODO: royal flush
	// TODO: not royal flush
}
