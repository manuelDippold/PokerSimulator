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
import com.yotilla.poker.result.GameResult;
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
	private final Logger log;
	private final Dealer dealer;

	public PokerTable(final Logger argLog, Dealer argDealer)
	{
		log = argLog;
		dealer = argDealer;

		// Use direct print to the console, no heads or tails, except warning or higher.
		Handler consoleHandler = new ConsoleHandler();
		consoleHandler.setFormatter(new PureLogFormatter());
		log.addHandler(consoleHandler);
		log.setUseParentHandlers(false);
	}

	/**
	 * Starting point of this poker simulator.
	 *
	 * @param hands Poker hands in String form. For example: <br>
	 *              "2D 9C AS AH AC" "3D 6D 7D TD QD" "2C 5C 7C 8S QH"
	 */
	public static void main(String[] hands)
	{
		// Summon a dealer and prepare a deck of cards.
		Dealer dealer = new Dealer(new DeckOfCards());

		new PokerTable(Logger.getGlobal(), dealer).playPoker(hands);
	}

	/**
	 * Play a game of Poker
	 *
	 * @param hands Strings of hands to be dealt.
	 */
	void playPoker(final String[] hands)
	{
		if (hands == null)
		{
			print("No hands have been dealt. Quitting.");
			return;
		}

		int amountOfPlayers = hands.length;

		// Seat the players at the table.
		List<Player> players = createPlayers(amountOfPlayers);
		print(amountOfPlayers + " Players at at the table.\n");

		// Deal the cards to the players
		for (int i = 0; i < hands.length; i++)
		{
			String handInput = hands[i];
			Player player = players.get(i);

			try
			{
				dealer.parseInputAndDealHand(handInput, player);

				// Evaluate each player's hand, tell them what they hold.
				dealer.evaluatePlayerHand(player);
			}
			catch (PokerParseException | HandExceededException | DeckException e)
			{
				log.log(Level.SEVERE, (String.format(
						"Something went wrong while dealing the cards and evaluating the hands: %s", e.getMessage())),
						e);
				return;
			}
		}

		// determine and print result
		GameResult result = dealer.determineGameResult(players);
		print(dealer.printResult(result));
	}

	/**
	 * Log something to the console as info. Info and below are not written with heads or tails.
	 *
	 * @param logMessage
	 */
	private void print(final String message)
	{
		log.log(Level.INFO, message);
		log.log(Level.INFO, "\n");
	}

	/**
	 * Create players and give them names
	 *
	 * @param numberOfPlayers how many players there are.
	 * @return List of players.
	 */
	private static List<Player> createPlayers(final int numberOfPlayers)
	{
		List<Player> players = new ArrayList<>();

		for (int i = 1; i <= numberOfPlayers; i++)
		{
			Player player = new Player("Player " + i);
			players.add(player);
		}

		return players;
	}

}
