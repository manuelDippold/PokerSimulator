package com.yotilla.poker;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.yotilla.poker.card.DeckOfCards;
import com.yotilla.poker.error.DeckException;
import com.yotilla.poker.error.HandExceededException;
import com.yotilla.poker.error.PokerParseException;
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
		// Use direct print to the console, no heads or tails, except warning or higher.
		Handler consoleHandler = new ConsoleHandler();
		consoleHandler.setFormatter(new PureLogFormatter());
		LOG.addHandler(consoleHandler);
		LOG.setUseParentHandlers(false);
	}

	/**
	 * Starting point of this poker simulator.
	 *
	 * @param hands Poker hands in String form. For example: <br>
	 *              "2D 9C AS AH AC" "3D 6D 7D TD QD" "2C 5C 7D 8S QH"
	 */
	public static void main(String[] hands)
	{
		// TODO: At least one integration test.

		if (hands == null)
		{
			print("No hands have been dealt. Quitting.");
			return;
		}

		int amountOfPlayers = hands.length;

		// Seat the players at the table.
		List<Player> players = createPlayers(amountOfPlayers);
		print(amountOfPlayers + " Players at at the table.");

		// Summon a dealer and prepare a deck of cards.
		DeckOfCards deck = new DeckOfCards();
		Dealer dealer = new Dealer(deck);

		// Deal the cards to the players
		for (int i = 0; i < hands.length; i++)
		{
			String handInput = hands[i];
			Player player = players.get(i);

			try
			{
				dealer.parseInputAndDealHand(handInput, player);
			}
			catch (PokerParseException | HandExceededException | DeckException e)
			{
				LOG.log(Level.SEVERE,
						(String.format("Something went wrong while dealing the cards: %s", e.getMessage())));
			}
		}

		// Evaluate each player's hand

		// TODO: evaluate hands
		// TODO Give poker hands to players
		// TODO: print players and their hands.
		// TODO: Rank hands. Beware of ties. Determine winner
		// TODO: print winner
	}

	/**
	 * Log something to the console as info. Info and below are not written with heads or tails.
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
