package com.yotilla.poker;

import com.yotilla.poker.card.Card;
import com.yotilla.poker.card.DeckOfCards;
import com.yotilla.poker.card.HandOfCards;
import com.yotilla.poker.error.DeckException;
import com.yotilla.poker.error.HandExceededException;
import com.yotilla.poker.error.PokerParseException;
import com.yotilla.poker.result.GameResult;
import com.yotilla.poker.result.PokerHand;

import java.util.List;

/**
 * Description: Deals cards from the deck to players and evaluates their hands.
 * <br>
 * Date: 26.12.2020
 *
 * @author Manuel
 */
public class Dealer {
    private final DeckOfCards deck;
    private final CardParser cardParser;
    private final HandEvaluationService handEvaluationService;

    /**
     * @param deck deck of cards to deal from. Will be shuffled on construction.
     */
    public Dealer(final DeckOfCards deck) {
        this.deck = deck;
        this.cardParser = new CardParser();
        this.handEvaluationService = new HandEvaluationService();
        deck.shuffleDeck();
    }

    /**
     * @param handInput space-separated card codes, or null/empty for a random hand
     * @param player    player who shall receive the hand
     * @throws PokerParseException   if input is invalid
     * @throws HandExceededException if the hand exceeds the card limit
     * @throws DeckException         if a card was already drawn
     */
    public void parseInputAndDealHand(final String handInput, final Player player)
            throws PokerParseException, HandExceededException, DeckException {
        if (player == null) {
            throw new PokerParseException("Cannot deal a hand to a null player.");
        }

        HandOfCards hand = new HandOfCards();

        if (handInput != null && !handInput.isEmpty()) {
            for (Card card : cardParser.parseCards(handInput)) {
                hand.addCard(deck.drawCard(card));
            }
        }

        while (hand.getAmountOfCards() < HandOfCards.HAND_SIZE) {
            hand.addCard(deck.drawNextCard());
        }

        player.dealHand(hand);
    }

    /**
     * @param player player whose hand will be evaluated
     * @throws PokerParseException if the player or their hand is null/empty, or no hand is recognized
     */
    public void evaluatePlayerHand(final Player player) throws PokerParseException {
        if (player == null || player.getHand() == null || player.getHand().isEmpty()) {
            throw new PokerParseException(
                    String.format("Hand recognition error: Player is either null or doesn't hold a hand: %s", player));
        }

        PokerHand result = handEvaluationService.evaluate(player.getHand());
        player.setPokerHand(result);

        if (player.getPokerHand() == null) {
            throw new PokerParseException("Error while parsing poker hand: No combination recognized.");
        }
    }

    /**
     * @param players players with an evaluated poker hand each
     * @return game result with players sorted by rank
     */
    public GameResult determineGameResult(final List<Player> players) {
        if (players == null) {
            return null;
        }

        GameResult result = new GameResult();
        players.forEach(result::addToRanks);

        return result;
    }
}
