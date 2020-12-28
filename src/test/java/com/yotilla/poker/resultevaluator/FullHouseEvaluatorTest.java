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
import com.yotilla.poker.result.evaluator.FullHouseEvaluator;

/**
 * Description:
 *
 * <br>
 * Date: 28.12.2020
 *
 * @author Manuel
 *
 */
class FullHouseEvaluatorTest extends AbstractEvaluatorTest
{
	/**
	 * getFullHouseIsNullSafe
	 *
	 * @throws HandExceededException error
	 */
	@Test
	void getFullHouseIsNullSafe() throws HandExceededException
	{
		PokerHand result = new FullHouseEvaluator().evaluate(null);
		assertNull(result, "Result should be null.");
	}

	/**
	 * fullHouseIsRecognized
	 *
	 * @throws HandExceededException error
	 * @throws DeckException         error
	 */
	@Test
	void fullHouseIsRecognized() throws HandExceededException, DeckException
	{
		HandOfCards hand = new HandOfCards();

		// Full House.
		hand.addCard(deck.drawCard(CardSuit.DIAMONDS, CardValue.KING));
		hand.addCard(deck.drawCard(CardSuit.SPADES, CardValue.KING));
		hand.addCard(deck.drawCard(CardSuit.HEARTS, CardValue.EIGHT));
		hand.addCard(deck.drawCard(CardSuit.CLUBS, CardValue.EIGHT));
		hand.addCard(deck.drawCard(CardSuit.DIAMONDS, CardValue.EIGHT));

		PokerHand result = new FullHouseEvaluator().evaluate(hand);

		assertEquals(PokerHandRanking.FULL_HOUSE, result.getRanking(), "This should result in a full house");
		assertEquals(CardValue.EIGHT, result.getRankCards().get(0),
				"The triple card is an eight. The result should reflect that.");
		assertEquals(CardValue.KING, result.getRankCards().get(1),
				"The pair card is a king. The result should reflect that.");

		assertTrue(result.getKickerCards().isEmpty(), "A full house does not leave room for kicker cards.");
	}

	/**
	 * fullHouseIsNotRecognizedIfThereIsNone
	 *
	 * @throws DeckException         error
	 * @throws HandExceededException error
	 */
	@Test
	void fullHouseIsNotRecognizedIfThereIsNone() throws HandExceededException, DeckException
	{
		HandOfCards hand = new HandOfCards();

		// Not a full House.
		hand.addCard(deck.drawCard(CardSuit.DIAMONDS, CardValue.KING));
		hand.addCard(deck.drawCard(CardSuit.SPADES, CardValue.QUEEN));
		hand.addCard(deck.drawCard(CardSuit.HEARTS, CardValue.TWO));
		hand.addCard(deck.drawCard(CardSuit.CLUBS, CardValue.EIGHT));
		hand.addCard(deck.drawCard(CardSuit.DIAMONDS, CardValue.EIGHT));

		PokerHand result = new FullHouseEvaluator().evaluate(hand);
		assertNull(result, "No full house, expected result is null.");
	}
}
