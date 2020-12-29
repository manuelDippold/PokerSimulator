package com.yotilla.poker;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.yotilla.poker.card.DeckOfCards;
import com.yotilla.poker.util.PureLogFormatter;

/**
 * Description:
 * This is where poker nights both begin and end.
 * <br>
 * Date: 29.12.2020
 *
 * @author Manuel
 *
 */
public class PokerTable
{
	private static final Logger LOG = Logger.getGlobal();

	static
	{
		// Use direct print to the console, no heads or tails.
		Handler consoleHandler = new ConsoleHandler();
		consoleHandler.setFormatter(new PureLogFormatter());
		LOG.addHandler(consoleHandler);
		LOG.setUseParentHandlers(false);
	}

	private static PrintStream out = System.out; // NOSONAR - we want to print directyl to the console

	/**
	 * Starting point of this poker simulator.
	 *
	 * @param hands Poker hands in String form. For example: <br>
	 *              "2D 9C AS AH AC" "3D 6D 7D TD QD" "2C 5C 7D 8S QH"
	 */
	public static void main(String[] hands)
	{
		if (hands == null)
		{
			print("No hands have been dealt. Quittin.");
		}

		int amountOfPlayers = hands.length;

		print(amountOfPlayers + " Players at at the table.");
		List<Player> players = createPlayers(amountOfPlayers);

		// Summon a dealer and prepare a deck of cards.
		DeckOfCards deck = new DeckOfCards();
		Dealer dealer = new Dealer(deck);

		// TODO: remove this fromt the table. the table must hold the players. We need a new main class.

		// TODO: Parse each String
		// TODO create players
		// TODO: Create hands
		// TODO: evaluate hands
		// TODO: print players and their hands.
		// TODO: Rank hands. Beware of ties. Determine winner
		// TODO: print winner
	}

	/**
	 * Log something
	 *
	 * @param logMessage
	 */
	private static void print(final String message)
	{
		LOG.log(Level.INFO, message);
	}

	private static List<Player> createPlayers(final int numberOfPlayers)
	{
		List<Player> players = new ArrayList<>();

		for (int i = 0; i < numberOfPlayers; i++)
		{
			Player player = new Player("Player " + i);
			players.add(player);
		}

		return players;
	}

}
