package com.yotilla.poker;

import com.yotilla.poker.card.HandOfCards;
import com.yotilla.poker.result.PokerHand;

/**
 * Description:
 * A poker playder
 * <br>
 * Date: 29.12.2020
 *
 * @author Manuel
 *
 */
public class Player
{
	private HandOfCards hand;
	private PokerHand pokerHand;
	private final String name;

	/**
	 * @param argName
	 */
	public Player(String argName)
	{
		super();
		name = argName;
	}

	/**
	 * Deal the player a hand of cards.
	 *
	 * @param argHand hand player will hold
	 */
	public void dealHand(final HandOfCards argHand)
	{
		if (hand != null)
		{
			throw new IllegalArgumentException("Player " + name + " already holds a hand!");
		}

		hand = argHand;
	}

	/**
	 * @return the hand
	 */
	public HandOfCards getHand()
	{
		return hand;
	}

	/**
	 * @return the pokerHand
	 */
	public PokerHand getPokerHand()
	{
		return pokerHand;
	}

	/**
	 * @param argPokerHand the pokerHand to set
	 */
	public void setPokerHand(PokerHand argPokerHand)
	{
		pokerHand = argPokerHand;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}
}
