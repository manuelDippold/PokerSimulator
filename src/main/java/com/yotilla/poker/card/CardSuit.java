package com.yotilla.poker.card;

/**
 * Description: Card suits assigned with a code <br>
 * Date: 20.12.2020
 *
 * @author Manuel
 *
 */
public enum CardSuit
{
	CLUBS("C"),
	DIAMONDS("D"),
	HEARTS("H"),
	SPADES("S");

	private String code;

	/**
	 * Constructor.
	 *
	 * @param code code to set
	 */
	private CardSuit(String argCode)
	{
		code = argCode;
	}

	/**
	 * @return the code
	 */
	public String getCode()
	{
		return code;
	}

	/**
	 * Finds and returns the suit matching the provided code, if there is one. Case
	 * insensitive, i.e. both 'c' and 'C' match Clubs.
	 *
	 * @param codeToMatch code to match
	 * @return CardSuit, if found. Null, if not.
	 */
	public static CardSuit getByCode(final String codeToMatch)
	{
		for (CardSuit suit : values())
		{
			if (suit.getCode().equalsIgnoreCase(codeToMatch))
			{
				return suit;
			}
		}

		return null;
	}
}
