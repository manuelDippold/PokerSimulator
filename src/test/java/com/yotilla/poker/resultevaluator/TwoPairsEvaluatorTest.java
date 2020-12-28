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
import com.yotilla.poker.result.evaluator.TwoPairsEvaluator;

/**
 * Description:
 *
 * <br>
 * Date: 28.12.2020
 *
 * @author Manuel
 *
 */
class TwoPairsEvaluatorTest extends AbstractEvaluatorTest
{
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

		PokerHand result = new TwoPairsEvaluator().evaluate(hand);

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

		PokerHand result = new TwoPairsEvaluator().evaluate(hand);
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
		PokerHand result = new TwoPairsEvaluator().evaluate(null);
		assertNull(result, "This hand does not contain a pair, much less two. In fact, there isn't even a hand.");
	}
}
