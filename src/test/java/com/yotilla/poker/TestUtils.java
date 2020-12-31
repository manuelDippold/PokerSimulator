package com.yotilla.poker;

import java.util.List;

import org.mockito.Mockito;

import com.yotilla.poker.card.Card;
import com.yotilla.poker.card.CardSuit;
import com.yotilla.poker.card.CardValue;
import com.yotilla.poker.card.HandOfCards;
import com.yotilla.poker.error.HandExceededException;
import com.yotilla.poker.result.PokerHand;
import com.yotilla.poker.result.PokerHandRanking;

/**
 * Description: service class for testing convenience
 *
 * <br>
 * Date: 27.12.2020
 *
 * @author Manuel
 *
 */
public class TestUtils
{
	/**
	 * Create and return the mock of a card with the specified suit and value
	 *
	 * @param argCardSuit  desired card suit
	 * @param argCardValue desired card value
	 * @return a mocked card
	 */
	public static Card getCardMock(final CardSuit argCardSuit, final CardValue argCardValue)
	{
		Card card = Mockito.mock(Card.class);
		Mockito.when(card.getCardSuit()).thenReturn(argCardSuit);
		Mockito.when(card.getCardValue()).thenReturn(argCardValue);

		return card;
	}

	/**
	 * Creates and returns a pseudo - randomly generated card mock. Does in no way
	 * guarantee the cards to be unique.
	 *
	 * @return a card mock
	 */
	public static Card getRandomCardMock()
	{
		// roll the dice for a card value between 2 and 14.
		int numericalCardValue = (int) Math.ceil((Math.random()) * 13 + 1);

		// correct for the very unlikely case that random() returned zero.
		if (numericalCardValue == 1)
		{
			numericalCardValue = 2;
		}

		CardValue cardValue = CardValue.getByNumericalValue(numericalCardValue);

		// roll again for the suit
		int suitValue = (int) Math.ceil(Math.random() * 4);
		CardSuit suit;

		switch (suitValue) {
		case 1:
			suit = CardSuit.CLUBS;
			break;
		case 2:
			suit = CardSuit.DIAMONDS;
			break;
		case 3:
			suit = CardSuit.HEARTS;
			break;
		case 4:
			suit = CardSuit.SPADES;
			break;
		default:
			suit = null;
			break;
		}

		return getCardMock(suit, cardValue);
	}

	/**
	 * Spy a poker hand for test purposes
	 *
	 * @param ranking     card ranking
	 * @param rankCards   rank cards, if necessary
	 * @param kickerCards kicker cards, if necessary
	 * @return Poker hand mock
	 */
	public static PokerHand getPokerHandSpy(final PokerHandRanking ranking, final List<CardValue> rankCards,
			final List<CardValue> kickerCards)
	{
		return Mockito.spy(new PokerHand(ranking, rankCards, kickerCards));
	}

	/**
	 * Get a hand of cards spy with card mocks in it.
	 * suits and values must be of the same size.
	 * suits.get(0) and values.get(0) will make up the first card.
	 *
	 * @param suits  suits for the cards
	 * @param values values for the cards in order with the suits.
	 * @return hand of cards mock returning the cards collection when asked.
	 * @throws HandExceededException error case
	 */
	public static HandOfCards getHandSpy(final List<CardSuit> suits, final List<CardValue> values)
			throws HandExceededException
	{
		HandOfCards hand = Mockito.spy(new HandOfCards());

		for (int i = 0; i < suits.size(); i++)
		{
			Card card = new Card(suits.get(i), values.get(i));
			hand.addCard(card);
		}

		return hand;
	}
}
