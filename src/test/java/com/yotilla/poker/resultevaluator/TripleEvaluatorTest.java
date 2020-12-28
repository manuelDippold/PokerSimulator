package com.yotilla.poker.resultevaluator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import com.yotilla.poker.card.CardSuit;
import com.yotilla.poker.card.CardValue;
import com.yotilla.poker.card.HandOfCards;
import com.yotilla.poker.error.DeckException;
import com.yotilla.poker.error.HandExceededException;
import com.yotilla.poker.result.PokerHand;
import com.yotilla.poker.result.PokerHandRanking;
import com.yotilla.poker.result.evaluator.TripleEvaluator;

/**
 * Description:
 *
 * <br>
 * Date: 28.12.2020
 *
 * @author Manuel
 *
 */
class TripleEvaluatorTest extends AbstractEvaluatorTest
{

	/**
	 * threeOfKindIsNullSafe
	 */
	@Test
	void threeOfKindIsNullSafe()
	{
		PokerHand result = new TripleEvaluator().evaluate(null);
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

		PokerHand result = new TripleEvaluator().evaluate(hand);

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

		PokerHand result = new TripleEvaluator().evaluate(hand);
		assertNull(result, "Without a triple, result should be null.");
	}
}
