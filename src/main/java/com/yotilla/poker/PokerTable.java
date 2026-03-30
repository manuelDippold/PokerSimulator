package com.yotilla.poker;

import com.yotilla.poker.card.DeckOfCards;
import com.yotilla.poker.error.DeckException;
import com.yotilla.poker.error.HandExceededException;
import com.yotilla.poker.error.PokerParseException;
import com.yotilla.poker.result.GameResult;
import com.yotilla.poker.util.LogPrinter;
import com.yotilla.poker.util.PureLogFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Description:
 * This is where poker nights both begin and end.
 * <br>
 * Date: 29.12.2020
 *
 * @author Manuel
 *
 */
public class PokerTable {
    private final Dealer dealer;
    private final LogPrinter printer;

    public PokerTable(final LogPrinter printer, Dealer dealer) {
        this.printer = printer;
        this.dealer = dealer;
    }

    /**
     * Starting point of this poker simulator.
     *
     * @param hands Poker hands in String form. For example: <br>
     *              "2D 9C AS AH AC" "3D 6D 7D TD QD" "2C 5C 7C 8S QH"
     */
    public static void main(String[] hands) {
        Logger logger = Logger.getGlobal();
        Handler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new PureLogFormatter());
        logger.addHandler(consoleHandler);
        logger.setUseParentHandlers(false);

        Dealer dealer = new Dealer(new DeckOfCards());
        new PokerTable(new LogPrinter(logger), dealer).playPoker(hands);
    }

    /**
     * Play a game of Poker
     *
     * @param hands Strings of hands to be dealt.
     */
    void playPoker(final String[] hands) {
        if (hands == null) {
            printer.print("No hands have been dealt. Quitting.");
            return;
        }

        int amountOfPlayers = hands.length;

        // Seat the players at the table.
        List<Player> players = createPlayers(amountOfPlayers);
        printer.print(amountOfPlayers + " Players at the table.\n");

        // Deal the cards to the players
        for (int i = 0; i < hands.length; i++) {
            String handInput = hands[i];
            Player player = players.get(i);

            try {
                dealer.parseInputAndDealHand(handInput, player);

                // Evaluate each player's hand, tell them what they hold.
                dealer.evaluatePlayerHand(player);
            } catch (PokerParseException e) {
                printer.getLogger().log(Level.WARNING,
                        String.format("Invalid hand input for %s: %s — skipping.", player.getName(), e.getMessage()));
            } catch (HandExceededException | DeckException e) {
                printer.getLogger().log(Level.SEVERE,
                        String.format("Internal error while dealing cards: %s", e.getMessage()), e);
                return;
            }
        }

        // determine and print result
        GameResult result = dealer.determineGameResult(players);
        if (result != null) {
            printer.print("Ranking:\n" + result.printRanks() + "\n" + result.printFinalResult() + "\n");
        }
    }

    /**
     * Create players and give them names
     *
     * @param numberOfPlayers how many players there are.
     * @return List of players.
     */
    private static List<Player> createPlayers(final int numberOfPlayers) {
        List<Player> players = new ArrayList<>();

        for (int i = 1; i <= numberOfPlayers; i++) {
            Player player = new Player("Player " + i);
            players.add(player);
        }

        return players;
    }

}
