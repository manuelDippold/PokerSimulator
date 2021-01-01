package com.yotilla.poker;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.yotilla.poker.card.Card;
import com.yotilla.poker.card.CardSuit;
import com.yotilla.poker.card.CardValue;
import com.yotilla.poker.card.DeckOfCards;
import com.yotilla.poker.card.HandOfCards;
import com.yotilla.poker.error.DeckException;
import com.yotilla.poker.error.HandExceededException;
import com.yotilla.poker.error.PokerParseException;
import com.yotilla.poker.result.GameResult;
import com.yotilla.poker.result.PokerHand;
import com.yotilla.poker.result.evaluator.FlushEvaluator;
import com.yotilla.poker.result.evaluator.FourOfKindEvaluator;
import com.yotilla.poker.result.evaluator.FullHouseEvaluator;
import com.yotilla.poker.result.evaluator.HighCardEvaluator;
import com.yotilla.poker.result.evaluator.PairEvaluator;
import com.yotilla.poker.result.evaluator.PokerHandEvaluator;
import com.yotilla.poker.result.evaluator.RoyalFlushEvaluator;
import com.yotilla.poker.result.evaluator.StraightEvaluator;
import com.yotilla.poker.result.evaluator.StraightFlushEvaluator;
import com.yotilla.poker.result.evaluator.TripleEvaluator;
import com.yotilla.poker.result.evaluator.TwoPairsEvaluator;

/**
 * Description: Core service class in a game of Poker. <br>
 * Deals the cards and is the central authority of what's going on.
 * Date: 26.12.2020
 *
 * @author Manuel
 *
 */
public class Dealer
{
	private final DeckOfCards deck;
	private final List<PokerHandEvaluator> evaluators;

	/**
	 * Summon a new Dealer. A dealer always holds a freshly - shuffled deck.
	 */
	public Dealer(final DeckOfCards argDeck)
	{
		deck = argDeck;
		deck.shuffleDeck();

		evaluators = new ArrayList<>();

		// Place the evaluators in the correct order, top - down.
		evaluators.add(new RoyalFlushEvaluator());
		evaluators.add(new StraightFlushEvaluator());
		evaluators.add(new FourOfKindEvaluator());
		evaluators.add(new FullHouseEvaluator());
		evaluators.add(new FlushEvaluator());
		evaluators.add(new StraightEvaluator());
		evaluators.add(new TripleEvaluator());
		evaluators.add(new TwoPairsEvaluator());
		evaluators.add(new PairEvaluator());
		evaluators.add(new HighCardEvaluator());
	}

	/**
	 * Analyze a hand of cards, draw it from the deck, and deal it to a player
	 *
	 * @param handInput input to parse
	 * @param player    player who shall receive the hand.
	 * @throws PokerParseException   in case of invalid input
	 * @throws HandExceededException if there are more cards than a hand can hold
	 * @throws DeckException         @throws DeckException if a card was already drawn.
	 */
	public void parseInputAndDealHand(final String handInput, final Player player)
			throws PokerParseException, HandExceededException, DeckException
	{
		if (player == null)
		{
			throw new PokerParseException("Cannot deal a hand to a null player.");
		}

		HandOfCards hand;

		// No input? Just fill hand randomly from the deck.
		if (handInput == null || handInput.isEmpty())
		{
			hand = new HandOfCards();
		}
		else
		{
			hand = parseHandOfCards(handInput);
		}

		// If there is space left in this hand, fill up.
		while (hand.getAmountOfCards() < HandOfCards.HAND_SIZE)
		{
			hand.addCard(deck.drawNextCard());
		}

		player.dealHand(hand);
	}

	/**
	 * Recognize a hand of cards from an input String.
	 *
	 * @param input input String with cards, separated by space
	 * @return hand of cards
	 * @throws PokerParseException   in case of invalid input
	 * @throws HandExceededException if there are more cards than a hand can hold
	 * @throws DeckException         if a card was already drawn.
	 */
	HandOfCards parseHandOfCards(final String input) throws PokerParseException, HandExceededException, DeckException
	{
		if (input == null || input.isEmpty())
		{
			throw new PokerParseException("Could not parse poker hand: empty input.");
		}

		String[] cardStrings = input.split(" ");

		HandOfCards hand = new HandOfCards();

		for (int i = 0; i < cardStrings.length; i++)
		{
			Card card = parseCard(cardStrings[i]);
			hand.addCard(deck.drawCard(card));
		}

		return hand;
	}

	/**
	 * input in format valueSuit, such as:<br>
	 * AS - Ace of spades<br>
	 * 2C - Two of clubs<br>
	 * etc.
	 *
	 * @param input input string, must be valid
	 * @return Card
	 * @throws PokerParseException when the card cannot be recognized
	 */
	Card parseCard(final String input) throws PokerParseException
	{
		if (input == null || input.length() != 2)
		{
			throw new PokerParseException(String.format(
					"Parse error: A card must consist of two characters for suit and value. Faulty input: %s", input));
		}

		CardValue value = CardValue.getByCode(input.substring(0, 1));
		CardSuit suit = CardSuit.getByCode(input.substring(1, 2));

		if (suit == null || value == null)
		{
			throw new PokerParseException(String.format("Parse error: Card not recognized: %s", input));
		}

		return new Card(suit, value);
	}

	/**
	 * Take a look at a players hand and tell them what they hold.
	 *
	 * @param player player holding a hand
	 * @throws PokerParseException in case of invalid input
	 */
	public void evaluatePlayerHand(final Player player) throws PokerParseException
	{
		if (player == null || player.getHand() == null || player.getHand().isEmpty())
		{
			throw new PokerParseException(
					String.format("Hand recognition error: Player is either null or doesn't hold a hand: %s", player));
		}

		HandOfCards playerHand = player.getHand();

		// Go though the possible hands top - down until we hit something valid.
		PokerHand result = null;
		Iterator<PokerHandEvaluator> evaluatorIterator = evaluators.iterator();

		while (result == null && evaluatorIterator.hasNext())
		{
			result = evaluatorIterator.next().evaluate(playerHand);
		}

		// assign result to player
		player.setPokerHand(result);

		// This is virtually impossible in Poker, but let's be careful.
		if (player.getPokerHand() == null)
		{
			throw new PokerParseException("Error while parsing poker hand: No combination recognized.");
		}
	}

	/**
	 * return a printale String representation of the game result.
	 *
	 * @param result result of the game
	 * @return String
	 */
	public String printResult(final GameResult result)
	{
		StringBuilder builder = new StringBuilder("Ranking:\n");

		if (result != null)
		{
			builder.append(result.printRanks());
			builder.append("\n");
			builder.append(result.printFinalResult());
			builder.append("\n");
		}

		return builder.toString();
	}

	/**
	 * Determine the result of the game.
	 *
	 * @param players players with a poker Hand (a result) assigned each.
	 * @return Game result object.
	 */
	public GameResult determineGameResult(final List<Player> players)
	{
		if (players == null)
		{
			return null;
		}

		// Create a game result and add the players. They are sorted into the ranks automatically.
		GameResult result = new GameResult();
		players.stream().forEach(result::addToRanks);

		return result;
	}
}
