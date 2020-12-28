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
import com.yotilla.poker.result.evaluator.FlushEvaluator;

/**
 * Description:
 *
 * <br>
 * Date: 28.12.2020
 *
 * @author Manuel
 *
 */
class FlushEvaluatorTest extends AbstractEvaluatorTest
{
	/**
	 * getFlushIsNullSafe
	 *
	 * @throws HandExceededException error case
	 */
	@Test
	void getFlushIsNullSafe() throws HandExceededException
	{
		PokerHand result = new FlushEvaluator().evaluate(null);
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

		PokerHand result = new FlushEvaluator().evaluate(hand);

		assertEquals(PokerHandRanking.FLUSH, result.getRanking(), "This should result in a flush");
		assertEquals(CardValue.KING, result.getRankCards().get(0),
				"Highest card in this flush is a king. The result should reflect that.");
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

		PokerHand result = new FlushEvaluator().evaluate(hand);
		assertNull(result, "Without a flush, result should be null.");
	}
}
