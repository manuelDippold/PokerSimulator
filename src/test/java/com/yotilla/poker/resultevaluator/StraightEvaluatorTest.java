package com.yotilla.poker.resultevaluator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.yotilla.poker.card.Card;
import com.yotilla.poker.card.CardSuit;
import com.yotilla.poker.card.CardValue;
import com.yotilla.poker.card.HandOfCards;
import com.yotilla.poker.error.DeckException;
import com.yotilla.poker.error.HandExceededException;
import com.yotilla.poker.result.PokerHand;
import com.yotilla.poker.result.PokerHandRanking;
import com.yotilla.poker.result.evaluator.StraightEvaluator;

/**
 * Description:
 *
 * <br>
 * Date: 28.12.2020
 *
 * @author Manuel
 *
 */
class StraightEvaluatorTest extends AbstractEvaluatorTest
{
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
		List<Card> kickerCards = new StraightEvaluator().getKickerCards(hand, hand.getCards());
		assertTrue(kickerCards.isEmpty(), "Five cards leave no room for kicker cards.");
	}

	/**
	 * getStraightIsNullSafe
	 *
	 * @throws HandExceededException error case
	 */
	@Test
	void getStraightIsNullSafe() throws HandExceededException
	{
		PokerHand result = new StraightEvaluator().evaluate(null);
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
		hand.addCard(deck.drawCard(CardSuit.SPADES, CardValue.FIVE));
		hand.addCard(deck.drawCard(CardSuit.DIAMONDS, CardValue.THREE));
		hand.addCard(deck.drawCard(CardSuit.DIAMONDS, CardValue.SIX));
		hand.addCard(deck.drawCard(CardSuit.CLUBS, CardValue.FOUR));
		hand.addCard(deck.drawCard(CardSuit.CLUBS, CardValue.SEVEN));

		PokerHand result = new StraightEvaluator().evaluate(hand);

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

		PokerHand result = new StraightEvaluator().evaluate(hand);

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

		PokerHand result = new StraightEvaluator().evaluate(hand);
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
		hand.addCard(deck.drawCard(CardSuit.CLUBS, CardValue.TWO));
		hand.addCard(deck.drawCard(CardSuit.DIAMONDS, CardValue.ACE));
		hand.addCard(deck.drawCard(CardSuit.SPADES, CardValue.THREE));
		hand.addCard(deck.drawCard(CardSuit.CLUBS, CardValue.FIVE));
		hand.addCard(deck.drawCard(CardSuit.DIAMONDS, CardValue.FOUR));

		PokerHand result = new StraightEvaluator().evaluate(hand);

		assertEquals(PokerHandRanking.STRAIGHT, result.getRanking(), "This should result in a straight");
		assertEquals(CardValue.FIVE, result.getRankCards().get(0),
				"Straight contains an Ace, but this in this special case, the ace acts as 'one', so the ranking card is the five. The result should reflect that.");
		assertTrue(result.getKickerCards().isEmpty(), "A Straight does not leave room for kicker cards.");
	}
}
