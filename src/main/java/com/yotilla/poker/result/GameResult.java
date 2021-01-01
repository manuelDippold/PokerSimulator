package com.yotilla.poker.result;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.yotilla.poker.Player;
import com.yotilla.poker.card.Card;

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
	private List<Player> winners;

	private final PokerHandComparator pokerHandComparator;
	private final SortedMap<PokerHand, List<Player>> ranking;

	/**
	 * Default constructor.
	 * Automatically creates the ranking map with a reversed poker hand comparator.
	 * Reversed for highest scores first.
	 */
	public GameResult()
	{
		pokerHandComparator = new PokerHandComparator();
		ranking = new TreeMap<>(pokerHandComparator.reversed());
	}

	/**
	 * the sole winner, if there is one.
	 *
	 * @return the winner
	 */
	public Player getWinner()
	{
		if (winners == null || winners.isEmpty() || winners.size() > 1)
		{
			return null;
		}

		return winners.stream().findFirst().orElse(null);
	}

	/**
	 * Players who split the pot among them
	 *
	 * @return the potSplit
	 */
	public List<Player> getPotSplit()
	{
		if (winners == null || winners.size() <= 1)
		{
			return Collections.emptyList();
		}

		return winners;
	}

	/**
	 * @return the ranking
	 */
	public SortedMap<PokerHand, List<Player>> getRanking()
	{
		return ranking;
	}

	/**
	 * Add player to the ranks of the result.<br>
	 * Winner(s) are determined automatically.
	 *
	 * @param player player to add
	 */
	public void addToRanks(final Player player)
	{
		if (player != null && player.getPokerHand() != null)
		{
			PokerHand playerHand = player.getPokerHand();

			if (!ranking.containsKey(playerHand)) // NOSONAR- computeIfAbsent doesn't fit
			{
				ranking.put(playerHand, new ArrayList<>());
			}

			ranking.get(playerHand).add(player);

			List<Player> winningPlayers = ranking.values().stream().findFirst().orElse(null);

			if (winningPlayers == null)
			{
				return;
			}

			winners = Collections.unmodifiableList(winningPlayers);
		}
	}

	/**
	 * return a printable String representation of the player ranks.
	 *
	 * @return string
	 */
	public String printRanks()
	{
		StringBuilder builder = new StringBuilder();

		int rankCounter = 0;

		for (List<Player> playersOnThisRank : ranking.values())
		{
			if (playersOnThisRank != null && !playersOnThisRank.isEmpty())
			{
				rankCounter++;
				for (Player player : playersOnThisRank)
				{
					builder.append(printPlayerAndHand(rankCounter, player));
					builder.append("\n");
				}
			}
		}

		return builder.toString();
	}

	/**
	 * Print a players result that game:<br>
	 * rank Name Hand PokerHand
	 *
	 * @param rank   rank the player scored. One is highest
	 * @param player player to be printed
	 * @return String
	 */
	public String printPlayerAndHand(final int rank, final Player player)
	{
		if (player == null || player.getPokerHand() == null || player.getPokerHand().getRanking() == null
				|| player.getHand() == null || player.getHand().getCards() == null)
		{
			return "";
		}

		PokerHand playerHand = player.getPokerHand();

		StringBuilder builder = new StringBuilder(rank + "\t" + player.getName() + "\t");

		Iterator<Card> cardIterator = player.getHand().getCards().iterator();

		while (cardIterator.hasNext())
		{
			Card card = cardIterator.next();
			builder.append(card.getCardValue().getCode() + card.getCardSuit().getCode());

			if (cardIterator.hasNext())
			{
				builder.append(" ");
			}
		}

		builder.append("\t" + playerHand.toString());

		return builder.toString();
	}

	/**
	 * print winner
	 *
	 * @return String
	 */
	public String printFinalResult()
	{
		if (winners != null && !winners.isEmpty())
		{
			Player soleWinner = getWinner();

			if (soleWinner != null)
			{
				return soleWinner.getName() + " wins.";
			}

			List<String> names = winners.stream().map(Player::getName).collect(Collectors.toList());

			StringBuilder sb = new StringBuilder("Players ");
			sb.append(String.join(", ", names));
			sb.append(" split the pot.");

			return sb.toString();
		}

		return "";
	}
}
