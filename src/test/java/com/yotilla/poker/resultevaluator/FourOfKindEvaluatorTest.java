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
import com.yotilla.poker.result.evaluator.FourOfKindEvaluator;

/**
 * Description:
 *
 * <br>
 * Date: 28.12.2020
 *
 * @author Manuel
 *
 */
class FourOfKindEvaluatorTest extends AbstractEvaluatorTest
{
	/**
	 * getFourOfKind
	 *
	 * @throws HandExceededException error
	 */
	@Test
	void getFourOfKindIsNullSafe() throws HandExceededException
	{
		PokerHand result = new FourOfKindEvaluator().evaluate(null);
		assertNull(result, "Result should be null.");
	}

	/**
	 * fourOfKindIsrecognized
	 *
	 * @throws DeckException         error
	 * @throws HandExceededException error
	 */
	@Test
	void fourOfKindIsrecognized() throws HandExceededException, DeckException
	{
		HandOfCards hand = new HandOfCards();

		// Four of a kind
		hand.addCard(deck.drawCard(CardSuit.DIAMONDS, CardValue.QUEEN));
		hand.addCard(deck.drawCard(CardSuit.SPADES, CardValue.QUEEN));
		hand.addCard(deck.drawCard(CardSuit.HEARTS, CardValue.QUEEN));
		hand.addCard(deck.drawCard(CardSuit.CLUBS, CardValue.QUEEN));

		// Fifth card.
		hand.addCard(deck.drawCard(CardSuit.DIAMONDS, CardValue.EIGHT));

		PokerHand result = new FourOfKindEvaluator().evaluate(hand);

		assertEquals(PokerHandRanking.FOUR_OF_A_KIND, result.getRanking(), "This should result in four of a kind");
		assertEquals(CardValue.QUEEN, result.getRankCards().get(0),
				"The rank card is the queen. The result should reflect that.");

		assertEquals(CardValue.EIGHT, result.getKickerCards().get(0), "The kicker card here should be the eight left.");
	}

	/**
	 * fourIsNotRecognizedIfNotThere
	 *
	 * @throws HandExceededException error
	 * @throws DeckException         error
	 */
	@Test
	void fourIsNotRecognizedIfNotThere() throws HandExceededException, DeckException
	{
		HandOfCards hand = new HandOfCards();

		// Not four of a kind
		hand.addCard(deck.drawCard(CardSuit.DIAMONDS, CardValue.QUEEN));
		hand.addCard(deck.drawCard(CardSuit.SPADES, CardValue.KING));
		hand.addCard(deck.drawCard(CardSuit.HEARTS, CardValue.SIX));
		hand.addCard(deck.drawCard(CardSuit.CLUBS, CardValue.TWO));

		// Fifth card.
		hand.addCard(deck.drawCard(CardSuit.DIAMONDS, CardValue.EIGHT));

		PokerHand result = new FourOfKindEvaluator().evaluate(hand);
		assertNull(result, "Not four of a kind, expected result is null.");
	}
}
