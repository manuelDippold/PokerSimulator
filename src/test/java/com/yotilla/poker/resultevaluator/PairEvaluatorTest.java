package com.yotilla.poker.resultevaluator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

import com.yotilla.poker.card.CardSuit;
import com.yotilla.poker.card.CardValue;
import com.yotilla.poker.card.HandOfCards;
import com.yotilla.poker.error.DeckException;
import com.yotilla.poker.error.HandExceededException;
import com.yotilla.poker.result.PokerHand;
import com.yotilla.poker.result.PokerHandRanking;
import com.yotilla.poker.result.evaluator.PairEvaluator;

/**
 * Description:
 *
 * <br>
 * Date: 28.12.2020
 *
 * @author Manuel
 *
 */
class PairEvaluatorTest extends AbstractEvaluatorTest
{
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

		// and three additional cards
		hand.addCard(deck.drawCard(CardSuit.CLUBS, CardValue.KING));
		hand.addCard(deck.drawCard(CardSuit.SPADES, CardValue.THREE));
		hand.addCard(deck.drawCard(CardSuit.HEARTS, CardValue.FIVE));

		PokerHand result = new PairEvaluator().evaluate(hand);

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

		PokerHand result = new PairEvaluator().evaluate(hand);
		assertNull(result, "This hand does not contain a pair.");
	}

	/**
	 * getPairIsNullSafe
	 */
	@Test
	void getPairIsNullSafe()
	{
		PokerHand result = new PairEvaluator().evaluate(null);
		assertNull(result, "This hand does not contain a pair. In fact, there isn't even a hand.");
	}
}
