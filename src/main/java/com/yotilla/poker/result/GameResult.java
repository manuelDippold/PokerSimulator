package com.yotilla.poker.result;

import java.util.Iterator;
import java.util.List;

import com.yotilla.poker.Player;

/**
 * Description:
 * The result of a poker game.
 * <br>
 * Date: 30.12.2020
 *
 * @author Manuel
 *
 */
public class GameResult
{
	private Player winner;
	private List<Player> potSplit;

	/**
	 * the sole winner
	 *
	 * @return the winner
	 */
	public Player getWinner()
	{
		return winner;
	}

	/**
	 * the sole winner
	 *
	 * @param argWinner the winner to set
	 */
	public void setWinner(Player argWinner)
	{
		winner = argWinner;
	}

	/**
	 * Players who split the pot among them
	 *
	 * @return the potSplit
	 */
	public List<Player> getPotSplit()
	{
		return potSplit;
	}

	/**
	 * Players who split the pot among them
	 *
	 * @param argPotSplit the potSplit to set
	 */
	public void setPotSplit(List<Player> argPotSplit)
	{
		potSplit = argPotSplit;
	}

	@Override
	public String toString()
	{
		if (winner != null)
		{
			return winner.getName() + " wins.";
		}

		if (potSplit != null && !potSplit.isEmpty())
		{
			Iterator<Player> playerIterator = potSplit.iterator();

			StringBuilder sb = new StringBuilder("Players ");

			while (playerIterator.hasNext())
			{
				sb.append(playerIterator.next().getName());

				if (playerIterator.hasNext())
				{
					sb.append(", ");
				}
			}

			sb.append(" split the pot.");

			return sb.toString();
		}

		return "";
	}
}
