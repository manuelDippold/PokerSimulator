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
import com.yotilla.poker.result.evaluator.HighCardEvaluator;

/**
 * Description:
 *
 * <br>
 * Date: 28.12.2020
 *
 * @author Manuel
 *
 */
class HighCardEvaluatorTest extends AbstractEvaluatorTest
{
	/**
	 * recognizeHighCard
	 *
	 * @throws HandExceededException error case
	 * @throws DeckException         error case
	 */
	@Test
	void evaluateHighCard() throws HandExceededException, DeckException
	{
		HandOfCards hand = new HandOfCards();

		// An Ace...
		hand.addCard(deck.drawCard(CardSuit.DIAMONDS, CardValue.ACE));

		// ...and four furhter cards
		hand.addCard(deck.drawCard(CardSuit.DIAMONDS, CardValue.THREE));
		hand.addCard(deck.drawCard(CardSuit.SPADES, CardValue.FIVE));
		hand.addCard(deck.drawCard(CardSuit.CLUBS, CardValue.EIGHT));
		hand.addCard(deck.drawCard(CardSuit.DIAMONDS, CardValue.JACK));

		PokerHand result = new HighCardEvaluator().evaluate(hand);

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
	void evaluateHighCardIsNullSafe()
	{
		PokerHand result = new HighCardEvaluator().evaluate(null);

		assertNull(result, "Empty hand, no high card");
	}
}
