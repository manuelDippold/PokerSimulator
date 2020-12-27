package com.yotilla.poker.card;

import org.mockito.Mockito;

/**
 * Description: service class for testing convenience
 *
 * <br>
 * Date: 27.12.2020
 *
 * @author Manuel
 *
 */
public class TestCardUtils
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
}
