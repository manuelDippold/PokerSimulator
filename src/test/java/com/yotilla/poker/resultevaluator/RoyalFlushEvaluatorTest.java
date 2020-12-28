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
import com.yotilla.poker.result.evaluator.RoyalFlushEvaluator;

/**
 * Description:
 *
 * <br>
 * Date: 28.12.2020
 *
 * @author Manuel
 *
 */
class RoyalFlushEvaluatorTest extends AbstractEvaluatorTest
{

	/**
	 * getRoyalFlushIsNullSafe
	 *
	 * @throws HandExceededException error
	 */
	@Test
	void getRoyalFlushIsNullSafe() throws HandExceededException
	{
		PokerHand result = new RoyalFlushEvaluator().evaluate(null);
		assertNull(result, "Analyzing null, expected result is null.");
	}

	/**
	 * royalFlushIsRecognized
	 *
	 * @throws HandExceededException error
	 * @throws DeckException         error
	 */
	@Test
	void royalFlushIsRecognized() throws HandExceededException, DeckException
	{
		HandOfCards hand = new HandOfCards();

		// Royal flush
		hand.addCard(deck.drawCard(CardSuit.DIAMONDS, CardValue.KING));
		hand.addCard(deck.drawCard(CardSuit.DIAMONDS, CardValue.ACE));
		hand.addCard(deck.drawCard(CardSuit.DIAMONDS, CardValue.QUEEN));
		hand.addCard(deck.drawCard(CardSuit.DIAMONDS, CardValue.TEN));
		hand.addCard(deck.drawCard(CardSuit.DIAMONDS, CardValue.JACK));

		PokerHand result = new RoyalFlushEvaluator().evaluate(hand);
		assertEquals(PokerHandRanking.ROYAL_FLUSH, result.getRanking(), "This should result in a royal flush");
		assertTrue(result.getRankCards().isEmpty(),
				"A royal flush does not leave room for rank cards. There is no tie breaker.");
		assertTrue(result.getKickerCards().isEmpty(), "A royal flush does not leave room for kicker cards.");
	}

	/**
	 * royalFlushIsRecognized
	 *
	 * @throws HandExceededException error
	 * @throws DeckException         error
	 */
	@Test
	void royalFlushIsNotRecognizedWhenAbsent() throws HandExceededException, DeckException
	{
		HandOfCards hand = new HandOfCards();

		// Not a royal flush
		hand.addCard(deck.drawCard(CardSuit.DIAMONDS, CardValue.KING));
		hand.addCard(deck.drawCard(CardSuit.SPADES, CardValue.QUEEN));
		hand.addCard(deck.drawCard(CardSuit.HEARTS, CardValue.TWO));
		hand.addCard(deck.drawCard(CardSuit.CLUBS, CardValue.EIGHT));
		hand.addCard(deck.drawCard(CardSuit.DIAMONDS, CardValue.EIGHT));

		PokerHand result = new RoyalFlushEvaluator().evaluate(hand);
		assertNull(result, "No royalflush, expected result is null.");
	}
}
