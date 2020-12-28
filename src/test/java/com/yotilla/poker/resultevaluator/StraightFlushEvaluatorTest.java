package com.yotilla.poker.resultevaluator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.yotilla.poker.card.CardSuit;
import com.yotilla.poker.card.CardValue;
import com.yotilla.poker.card.HandOfCards;
import com.yotilla.poker.error.DeckException;
import com.yotilla.poker.error.HandExceededException;
import com.yotilla.poker.result.PokerHand;
import com.yotilla.poker.result.PokerHandRanking;
import com.yotilla.poker.result.evaluator.StraightEvaluator;
import com.yotilla.poker.result.evaluator.StraightFlushEvaluator;

/**
 * Description:
 *
 * <br>
 * Date: 28.12.2020
 *
 * @author Manuel
 *
 */
public class StraightFlushEvaluatorTest extends AbstractEvaluatorTest
{
	/**
	 * getStraighFlushIsNullSafe
	 *
	 * @throws HandExceededException error
	 */
	@Test
	void getStraighFlushIsNullSafe() throws HandExceededException
	{
		PokerHand result = new StraightEvaluator().evaluate(null);
		assertNull(result, "Analyzing null, expected result is null.");

	}

	/**
	 * straightFlushIsRecognized
	 *
	 * @throws DeckException         error
	 * @throws HandExceededException error
	 */
	@Test
	void straightFlushIsRecognized() throws HandExceededException, DeckException
	{
		HandOfCards hand = new HandOfCards();

		// Straight flush
		hand.addCard(deck.drawCard(CardSuit.DIAMONDS, CardValue.KING));
		hand.addCard(deck.drawCard(CardSuit.DIAMONDS, CardValue.QUEEN));
		hand.addCard(deck.drawCard(CardSuit.DIAMONDS, CardValue.JACK));
		hand.addCard(deck.drawCard(CardSuit.DIAMONDS, CardValue.TEN));
		hand.addCard(deck.drawCard(CardSuit.DIAMONDS, CardValue.NINE));

		PokerHand result = new StraightFlushEvaluator().evaluate(hand);

		assertEquals(PokerHandRanking.STRAIGHT_FLUSH, result.getRanking(), "This should result in a straight flush");
		assertEquals(CardValue.KING, result.getRankCards().get(0),
				"The rank card is the king. The result should reflect that.");

		assertTrue(result.getKickerCards().isEmpty(), "A straight flush does not leave room for kicker cards.");
	}

	/**
	 * straightFlushIsRecognizedWithLeadingAce
	 *
	 * @throws DeckException         error
	 * @throws HandExceededException error
	 */
	@Test
	void straightFlushIsRecognizedWithLeadingAce() throws HandExceededException, DeckException
	{
		HandOfCards hand = new HandOfCards();

		// Straight flush, leading ace
		hand.addCard(deck.drawCard(CardSuit.DIAMONDS, CardValue.TWO));
		hand.addCard(deck.drawCard(CardSuit.DIAMONDS, CardValue.FOUR));
		hand.addCard(deck.drawCard(CardSuit.DIAMONDS, CardValue.ACE));
		hand.addCard(deck.drawCard(CardSuit.DIAMONDS, CardValue.FIVE));
		hand.addCard(deck.drawCard(CardSuit.DIAMONDS, CardValue.THREE));

		PokerHand result = new StraightFlushEvaluator().evaluate(hand);

		assertEquals(PokerHandRanking.STRAIGHT_FLUSH, result.getRanking(), "This should result in a straight flush");
		assertEquals(CardValue.FIVE, result.getRankCards().get(0),
				"The rank card is the five. The result should reflect that.");

		assertTrue(result.getKickerCards().isEmpty(), "A straight flush does not leave room for kicker cards.");
	}

	/**
	 * straightFlushIsNotRecognizedWhenThereIsNone
	 *
	 * @throws DeckException         error
	 * @throws HandExceededException error
	 */
	@Test
	void straightFlushIsNotRecognizedWhenThereIsNone() throws HandExceededException, DeckException
	{
		HandOfCards hand = new HandOfCards();

		// Not a straight flush
		hand.addCard(deck.drawCard(CardSuit.DIAMONDS, CardValue.KING));
		hand.addCard(deck.drawCard(CardSuit.SPADES, CardValue.QUEEN));
		hand.addCard(deck.drawCard(CardSuit.HEARTS, CardValue.TWO));
		hand.addCard(deck.drawCard(CardSuit.CLUBS, CardValue.EIGHT));
		hand.addCard(deck.drawCard(CardSuit.DIAMONDS, CardValue.EIGHT));

		PokerHand result = new StraightEvaluator().evaluate(hand);
		assertNull(result, "No straight flush, expected result is null.");
	}
}
