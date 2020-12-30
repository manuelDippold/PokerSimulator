package com.yotilla.poker.card;

/**
 * Description: card values assigned with value and code <br>
 * Date: 20.12.2020
 *
 * @author Manuel
 *
 */
public enum CardValue
{
	TWO("2", 2),
	THREE("3", 3),
	FOUR("4", 4),
	FIVE("5", 5),
	SIX("6", 6),
	SEVEN("7", 7),
	EIGHT("8", 8),
	NINE("9", 9),
	TEN("T", 10),
	JACK("J", 11),
	QUEEN("Q", 12),
	KING("K", 13),
	ACE("A", 14);

	private String code;
	private int numericalValue;

	/**
	 * @param argCode           code to set
	 * @param argNumericalValue value to set
	 */
	private CardValue(String argCode, int argNumericalValue)
	{
		code = argCode;
		numericalValue = argNumericalValue;
	}

	/**
	 * @return the code the card value can be identified by
	 */
	public String getCode()
	{
		return code;
	}

	/**
	 * @return the cards value within the deck.
	 */
	public int getNumericalValue()
	{
		return numericalValue;
	}

	/**
	 * Returns the value with this code, if one exists.
	 *
	 * @param codeToMatch code to be matched
	 * @return card value, if found. Null, if not.
	 */
	public static CardValue getByCode(final String codeToMatch)
	{
		for (CardValue cardValue : values())
		{
			if (cardValue.getCode().equalsIgnoreCase(codeToMatch))
			{
				return cardValue;
			}
		}

		return null;
	}

	/**
	 * Returns the value with this numerical value, if one exists.
	 *
	 * @param numericalValueToMatch numerical value to match
	 * @return card value, if found. Null, if not.
	 */
	public static CardValue getByNumericalValue(final int numericalValueToMatch)
	{
		for (CardValue cardValue : values())
		{
			if (cardValue.getNumericalValue() == numericalValueToMatch)
			{
				return cardValue;
			}
		}

		return null;
	}
}
