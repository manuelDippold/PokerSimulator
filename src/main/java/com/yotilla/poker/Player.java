package com.yotilla.poker;

import com.yotilla.poker.card.HandOfCards;

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
	private final String name;

	private int wins;
	private int splitPots;
	private int losses;

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
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @return the wins
	 */
	public int getWins()
	{
		return wins;
	}

	/**
	 * Increase player win count by one.
	 */
	public void increaseWinCount()
	{
		wins++;
	}

	/**
	 * @return the splitPots
	 */
	public int getSplitPots()
	{
		return splitPots;
	}

	/**
	 * Increase player "split pots" count by one.
	 */
	public void increaseSplitPotsCount()
	{
		splitPots++;
	}

	/**
	 * @return the losses
	 */
	public int getLosses()
	{
		return losses;
	}

	/**
	 * Increase player loss count by one.
	 */
	public void increaseLossCount()
	{
		losses++;
	}
}
