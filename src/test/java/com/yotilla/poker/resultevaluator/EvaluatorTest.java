package com.yotilla.poker.resultevaluator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.yotilla.poker.card.Card;
import com.yotilla.poker.card.CardSuit;
import com.yotilla.poker.card.CardValue;
import com.yotilla.poker.card.HandOfCards;
import com.yotilla.poker.error.DeckException;
import com.yotilla.poker.error.HandExceededException;
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
class EvaluatorTest extends AbstractEvaluatorTest
{

	/**
	 * getKickerCardsIsNullSafe
	 *
	 * @throws HandExceededException error case
	 * @throws DeckException         error case
	 */
	@Test
	void getKickerCardsIsNullSafe() throws HandExceededException, DeckException
	{
		List<Card> kickerCards = new PairEvaluator().getKickerCards(null, null);
		assertTrue(kickerCards.isEmpty(), "null results in null");

		HandOfCards hand = new HandOfCards();

		fillHandFromDeck(hand);
		kickerCards = new PairEvaluator().getKickerCards(hand, null);
		assertFalse(kickerCards.isEmpty(), "This should result in the full hand being returned.");

		kickerCards = new PairEvaluator().getKickerCards(null, kickerCards);
		assertTrue(kickerCards.isEmpty(), "A hand of null should always result in null.");
	}

	/**
	 * Fill the hand with cards from the deck
	 *
	 * @param hand hand to fill
	 * @throws DeckException         if deck was emptied and we needed more cards
	 * @throws HandExceededException error case
	 */
	private void fillHandFromDeck(final HandOfCards hand) throws HandExceededException, DeckException
	{
		if (hand == null)
		{
			return;
		}

		while (hand.getAmountOfCards() < HandOfCards.HAND_SIZE)
		{
			hand.addCard(deck.drawNextCard());
		}
	}

	/**
	 *
	 * getKickerCardsFromPair
	 *
	 * @throws HandExceededException error case
	 * @throws DeckException         error case
	 */
	@Test
	void getKickerCardsFromPair() throws HandExceededException, DeckException
	{
		HandOfCards hand = new HandOfCards();

		// add a pair to the hand, then fill it up randomly.
		Card aceOfHearts = deck.drawCard(CardSuit.HEARTS, CardValue.ACE);
		Card aceOfDiamonds = deck.drawCard(CardSuit.DIAMONDS, CardValue.ACE);

		hand.addCard(aceOfHearts);
		hand.addCard(aceOfDiamonds);
		List<Card> partOfRanking = Arrays.asList(aceOfHearts, aceOfDiamonds);

		// add three other cards. These should come back as kickers,
		// in descending order.
		Card fiveOfSpades = deck.drawCard(CardSuit.SPADES, CardValue.FIVE);
		Card nineOfSpades = deck.drawCard(CardSuit.SPADES, CardValue.NINE);
		Card kingOfHearts = deck.drawCard(CardSuit.HEARTS, CardValue.KING);

		hand.addCard(fiveOfSpades);
		hand.addCard(nineOfSpades);
		hand.addCard(kingOfHearts);

		List<Card> kickerCards = new PairEvaluator().getKickerCards(hand, partOfRanking);

		assertSame(3, kickerCards.size(), "A hand of five and a pair of two should leave three kicker cards.");

		Iterator<Card> iter = kickerCards.iterator();

		assertEquals(kingOfHearts, iter.next(), "First kicker card should be the king of hearts.");
		assertEquals(nineOfSpades, iter.next(), "Second kicker card should be the nine of spades.");
		assertEquals(fiveOfSpades, iter.next(), "Third kicker card should be the five of spades.");
	}

	/**
	 * copyIsNullSafe
	 *
	 * @throws HandExceededException error case
	 */
	@Test
	void copyIsNullSafe() throws HandExceededException
	{
		HandOfCards result = new PairEvaluator().copyHandOfCards(null);
		assertNull(result, "A copy of null should be null.");
	}

	/**
	 * copyDuplicatesObjects
	 *
	 * @throws DeckException         error case
	 * @throws HandExceededException error case
	 */
	@Test
	void copyDuplicatesObjects() throws HandExceededException, DeckException
	{
		HandOfCards hand = new HandOfCards();
		hand.addCard(deck.drawCard(CardSuit.SPADES, CardValue.TWO));
		hand.addCard(deck.drawCard(CardSuit.SPADES, CardValue.THREE));
		hand.addCard(deck.drawCard(CardSuit.HEARTS, CardValue.SIX));

		HandOfCards copy = new PairEvaluator().copyHandOfCards(hand);

		for (int i = 0; i < 3; i++)
		{
			assertEquals(hand.getCards().get(i), copy.getCards().get(i), "These two cards should be equal.");
			assertNotSame(hand.getCards().get(i), copy.getCards().get(i),
					"These two cards should be equal, but not identical.");
		}
	}
}
