package com.yotilla.poker.card;

/**
 * Description: One card of the French deck.
 *
 * <br>
 * Date: 20.12.2020
 *
 * @author Manuel
 *
 */
public class Card implements Comparable<Card>
{
	private CardSuit cardSuit;
	private CardValue cardValue;

	/**
	 * @param argCardSuit  card suit, or color.
	 * @param argCardValue card value
	 */
	public Card(CardSuit argCardSuit, CardValue argCardValue)
	{
		cardSuit = argCardSuit;
		cardValue = argCardValue;
	}

	/**
	 * @return the cardSuit
	 */
	public CardSuit getCardSuit()
	{
		return cardSuit;
	}

	/**
	 * @return the cardValue
	 */
	public CardValue getCardValue()
	{
		return cardValue;
	}

	/**
	 * Compares two cards based on their value. Returns: <br>
	 * -1 if this card is worth less than otherCard <br>
	 * 0 if this card is of equal value to otherCard <br>
	 * 1 if this card is worth more than otherCard <br>
	 *
	 * @param otherCard Card to compare this one to
	 */
	public int compareTo(Card otherCard)
	{
		// Even the smallest card wins when compared to nothing.
		if (otherCard == null || otherCard.getCardValue() == null)
		{
			return 1;
		}

		return Integer.compare(getCardValue().getNumericalValue(), otherCard.getCardValue().getNumericalValue());
	}

}
