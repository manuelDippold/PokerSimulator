package com.yotilla.poker;

import com.yotilla.poker.card.Card;
import com.yotilla.poker.card.CardSuit;
import com.yotilla.poker.card.CardValue;
import com.yotilla.poker.card.DeckOfCards;
import com.yotilla.poker.card.HandOfCards;
import com.yotilla.poker.error.DeckException;
import com.yotilla.poker.error.HandExceededException;
import com.yotilla.poker.error.PokerParseException;

/**
 * Description: Core service class in a game of Poker. <br>
 * Date: 26.12.2020
 *
 * @author Manuel
 *
 */
public class Dealer
{
	private final DeckOfCards deck;

	/**
	 * Summon a new Dealer. A dealer always holds a freshly - shuffled deck.
	 */
	public Dealer(final DeckOfCards argDeck)
	{
		deck = argDeck;
		shuffleDeck();
	}

	/**
	 * Shuffle this dealer's deck.
	 */
	public void shuffleDeck()
	{
		deck.shuffleDeck();
	}

	/**
	 * Analyse a hand of cards, draw it from the deck, and deal it to a player
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

		HandOfCards hand = parseHandOfCards(handInput);

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
			Card toDraw = parseCard(cardStrings[i]);

			hand.addCard(deck.drawCard(toDraw.getCardSuit(), toDraw.getCardValue()));
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
	 * @return Card object
	 * @throws PokerParseException when the card cannot be regognized
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
}
