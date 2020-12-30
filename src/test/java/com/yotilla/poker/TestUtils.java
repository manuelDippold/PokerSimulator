package com.yotilla.poker;

import java.util.List;

import org.mockito.Mockito;

import com.yotilla.poker.card.Card;
import com.yotilla.poker.card.CardSuit;
import com.yotilla.poker.card.CardValue;
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
	 * mock a poker hand for test purposes
	 *
	 * @param ranking     card ranking
	 * @param rankCards   rank cards, if necessary
	 * @param kickerCards kicker cards, if necessary
	 * @return Poker hand mock
	 */
	public static PokerHand getPokerHandMock(final PokerHandRanking ranking, final List<CardValue> rankCards,
			final List<CardValue> kickerCards)
	{
		PokerHand hand = Mockito.mock(PokerHand.class);

		Mockito.when(hand.getRanking()).thenReturn(ranking);
		Mockito.when(hand.getRankCards()).thenReturn(rankCards);
		Mockito.when(hand.getKickerCards()).thenReturn(kickerCards);

		return hand;
	}
}
