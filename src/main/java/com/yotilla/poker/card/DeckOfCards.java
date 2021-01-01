package com.yotilla.poker.card;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

import com.yotilla.poker.error.DeckException;
import com.yotilla.poker.error.DeckExceptionCause;

/**
 * Description: A French deck of cards, holding 52 pieces. <br>
 * Date: 26.12.2020
 *
 * @author Manuel
 *
 */
public class DeckOfCards
{
	// A French deck holds 52 cards.
	static final int DECK_SIZE = 52;

	// Collection of cards, mapped by their hash code
	private final Deque<Card> cards;

	/**
	 * Create a new deck of 52 unique cards.
	 */
	public DeckOfCards()
	{
		cards = new ArrayDeque<>(DECK_SIZE);

		for (CardSuit suit : CardSuit.values())
		{
			for (CardValue value : CardValue.values())
			{
				Card card = new Card(suit, value);
				cards.push(card);
			}
		}
	}

	/**
	 * returns true if the deck is empty, i.e. if there are no more cards.
	 *
	 * @return boolean
	 */
	public boolean isEmpty()
	{
		return cards.isEmpty();
	}

	/**
	 * returns the amount of cards left in the deck.
	 *
	 * @return integer
	 */
	public int getAmountOfCardsLeft()
	{
		return cards.size();
	}

	/**
	 * Recognize the card by its hash and draw it.
	 *
	 * @param toDraw card desired
	 * @return Card, if it is in the deck.
	 *
	 * @throws DeckException if the card in question was already drawn or if the
	 *                       deck is empty.
	 */
	public Card drawCard(final Card toDraw) throws DeckException
	{
		if (isEmpty())
		{
			throw new DeckException(DeckExceptionCause.DECK_IS_EMPTY);
		}

		// Find the card, remove it and return it.
		Card ret = cards.stream().filter(c -> c.equals(toDraw)).findFirst().orElse(null);

		if (ret == null)
		{
			throw new DeckException(DeckExceptionCause.CARD_ALREADY_DRAWN, toDraw);
		}

		cards.remove(ret);

		return ret;
	}

	/**
	 * Recognize the card by its hash and draw it.
	 *
	 * @param argSuit  suit desired
	 * @param argValue value desired
	 *
	 * @return Card, if it is in the deck.
	 *
	 * @throws DeckException if the card in question was already drawn or if the
	 *                       deck is empty.
	 */
	public Card drawCard(CardSuit argSuit, CardValue argValue) throws DeckException
	{
		return drawCard(new Card(argSuit, argValue));
	}

	/**
	 * Draw the next card from the deck
	 *
	 * @return Card
	 * @throws DeckException if you try to draw from an empty deck.
	 */
	public Card drawNextCard() throws DeckException
	{
		if (isEmpty())
		{
			throw new DeckException(DeckExceptionCause.DECK_IS_EMPTY);
		}

		return cards.pop();
	}

	/**
	 * shuffle the deck, randomizing the order.
	 */
	public void shuffleDeck()
	{
		// get a working copy list of the remaining cards in this deck.
		List<Card> cardList = new ArrayList<>();
		cardList.addAll(cards);

		// shuffle the list
		Collections.shuffle(cardList);

		// clear the deck itself and re-add all cards from the shuffled list.
		cards.clear();
		cards.addAll(cardList);
	}
}
