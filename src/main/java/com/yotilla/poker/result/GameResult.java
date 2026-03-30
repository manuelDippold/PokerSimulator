package com.yotilla.poker.result;

import com.yotilla.poker.Player;
import com.yotilla.poker.card.Card;

import java.util.*;

/**
 * Description:
 * The result of a poker game.
 * <br>
 * Date: 30.12.2020
 *
 * @author Manuel
 *
 */
public class GameResult {
    private final SortedMap<PokerHand, List<Player>> ranking;

    /**
     * Default constructor.
     * Automatically creates the ranking map with a reversed poker hand comparator.
     * Reversed for highest scores first.
     */
    public GameResult() {
        PokerHandComparator pokerHandComparator = new PokerHandComparator();
        ranking = new TreeMap<>(pokerHandComparator.reversed());
    }

    private List<Player> resolveWinners() {
        if (ranking.isEmpty()) {
            return Collections.emptyList();
        }
        return ranking.get(ranking.firstKey());
    }

    public Player getWinner() {
        List<Player> currentWinners = resolveWinners();
        if (currentWinners.size() != 1) {
            return null;
        }
        return currentWinners.getFirst();
    }

    /**
     * Players who split the pot among them
     *
     * @return the potSplit
     */
    public List<Player> getPotSplit() {
        List<Player> currentWinners = resolveWinners();
        if (currentWinners.size() <= 1) {
            return Collections.emptyList();
        }
        return currentWinners;
    }

    /**
     * @return the ranking
     */
    public SortedMap<PokerHand, List<Player>> getRanking() {
        return ranking;
    }

    /**
     * Add player to the ranks of the result.<br>
     * Winner(s) are determined automatically.
     *
     * @param player player to add
     */
    public void addToRanks(final Player player) {
        if (player != null && player.getPokerHand() != null) {
            PokerHand playerHand = player.getPokerHand();

            ranking.computeIfAbsent(playerHand, hand -> new ArrayList<>()).add(player);
        }
    }

    /**
     * return a printable String representation of the player ranks.
     *
     * @return string
     */
    public String printRanks() {
        StringBuilder builder = new StringBuilder();

        int rankCounter = 0;

        for (List<Player> playersOnThisRank : ranking.values()) {
            if (playersOnThisRank != null && !playersOnThisRank.isEmpty()) {
                rankCounter++;
                for (Player player : playersOnThisRank) {
                    builder.append(printPlayerAndHand(rankCounter, player));
                    builder.append("\n");
                }
            }
        }

        return builder.toString();
    }

    public String printPlayerAndHand(final int rank, final Player player) {
        if (!isPlayerPrintable(player)) {
            return "";
        }

        PokerHand playerHand = player.getPokerHand();

        StringBuilder builder = new StringBuilder(rank + "\t" + player.getName() + "\t");

        Iterator<Card> cardIterator = player.getHand().getCards().iterator();

        while (cardIterator.hasNext()) {
            Card card = cardIterator.next();
            builder.append(card.cardValue().getCode())
                    .append(card.cardSuit().getCode());

            if (cardIterator.hasNext()) {
                builder.append(" ");
            }
        }

        builder.append("\t")
                .append(playerHand.toString());

        return builder.toString();
    }

    private boolean isPlayerPrintable(Player player) {
        return player != null
                && player.getPokerHand() != null
                && player.getPokerHand().ranking() != null
                && player.getHand() != null
                && player.getHand().getCards() != null;
    }

    /**
     * print winner
     *
     * @return String
     */
    public String printFinalResult() {
        Player soleWinner = getWinner();
        if (soleWinner != null) {
            return soleWinner.getName() + " wins.";
        }

        List<Player> splitWinners = getPotSplit();
        if (!splitWinners.isEmpty()) {
            List<String> names = splitWinners.stream().map(Player::getName).toList();
            return "Players " + String.join(", ", names) + " split the pot.";
        }

        return "";
    }
}
