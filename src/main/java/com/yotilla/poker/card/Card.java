package com.yotilla.poker.card;

import java.util.Objects;

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
	 * <br>
	 * The suit does not play a role in the comparison, as it has no bearing on the
	 * order.
	 *
	 * @param otherCard Card to compare this one to
	 */
	@Override
	public int compareTo(Card otherCard)
	{
		// Even the smallest card wins when compared to nothing.
		if (otherCard == null || otherCard.getCardValue() == null)
		{
			return 1;
		}

		return Integer.compare(getCardValue().getNumericalValue(), otherCard.getCardValue().getNumericalValue());
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(cardSuit, cardValue);
	}

	/**
	 * Two cards are equal when they are identical in both suit and value.
	 */
	@Override
	public boolean equals(Object otherObject)
	{
		if (!(otherObject instanceof Card))
		{
			return false;
		}

		Card otherCard = (Card) otherObject;

		return otherCard.getCardSuit() == cardSuit && otherCard.getCardValue() == cardValue;
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();

		if (cardValue != null)
		{
			sb.append(cardValue.name());
		} else
		{
			sb.append("?");
		}

		sb.append(" of ");

		if (cardSuit != null)
		{
			sb.append(cardSuit.name());
		} else
		{
			sb.append("?");
		}

		return sb.toString();
	}

}
