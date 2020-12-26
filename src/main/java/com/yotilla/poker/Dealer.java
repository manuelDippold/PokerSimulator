package com.yotilla.poker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.yotilla.poker.card.Card;
import com.yotilla.poker.card.CardValue;
import com.yotilla.poker.error.HandExceededException;

/**
 * Description: Core service class in a game of Poker. <br>
 * Date: 26.12.2020
 *
 * @author Manuel
 *
 */
public class Dealer
{

	/**
	 * Make a quick copy of a hand of cards
	 *
	 * @param toCopy hand to copy
	 * @return hand of cards
	 * @throws HandExceededException when the cards exceed the limit
	 */
	HandOfCards copyHandOfCards(final HandOfCards toCopy) throws HandExceededException
	{
		if (toCopy == null)
		{
			return null;
		}

		HandOfCards copy = new HandOfCards();

		if (toCopy.getCards() == null)
		{
			return copy;
		}
		else if (toCopy.getAmountOfCards() == 0)
		{
			copy.setCards(Collections.emptyList());
			return copy;
		}

		for (Card existing : toCopy.getCards())
		{
			copy.addCard(new Card(existing.getCardSuit(), existing.getCardValue()));
		}

		return copy;
	}

	/**
	 * Get the high card from a hand
	 *
	 * @param hand hand to be checked
	 * @return high card of this hand. Null if the hand is empty.
	 */
	public CardValue getHighCard(final HandOfCards hand)
	{
		return hand == null ? null : getHighCard(hand.getCards());
	}

	/**
	 * Get the high card from an arbitrary Collection of Cards
	 *
	 * @param hand cards to search in
	 * @return highest card of this hand. Null if the hand is empty or null.
	 */
	public CardValue getHighCard(final Collection<Card> hand)
	{
		Card highCard = null;

		if (hand != null)
		{
			for (Card card : hand)
			{
				if (card.compareTo(highCard) > 0)
				{
					highCard = card;
				}
			}
		}
		return highCard != null ? highCard.getCardValue() : null;
	}

	/**
	 * Determines whether or not this hand of cards contains at least one Pair. If
	 * it does, returns the card value of the pair. Null, if not.
	 *
	 * @param hand hand of cards we wish to check
	 * @return Card value or null
	 */
	public CardValue getPair(final HandOfCards hand)
	{
		// TODO: null safety

		// Two cards form a pair when their value is equal to one another
		Set<CardValue> cardValues = new HashSet<>();

		for (Card card : hand.getCards())
		{
			CardValue value = card.getCardValue();

			// If this value was already encountered at this point, we have a pair.
			if (cardValues.contains(value))
			{
				return value;
			}

			cardValues.add(value);
		}

		// No pair found, return null
		return null;
	}

	/**
	 * If this hand contains two pairs, this method returns the card value of the
	 * higher one.
	 *
	 * @param hand hand to check
	 * @return card value or null
	 * @throws HandExceededException
	 */
	public CardValue getTwoPairs(final HandOfCards hand) throws HandExceededException
	{
		// TODO: tie breaker info into the result. Two calues maybe?
		// TODO: null safety
		Map<CardValue, Integer> counter = new EnumMap<>(CardValue.class);

		List<Card> pairs = new ArrayList<>();

		for (Card card : hand.getCards())
		{
			CardValue value = card.getCardValue();

			// If we encountered this value before, this is at least a pair.
			if (counter.containsKey(value))
			{
				counter.put(value, counter.get(value) + 1);
				pairs.add(card);
			}
			else
			{
				counter.put(value, 1);
			}
		}

		if (pairs.size() >= 2)
		{
			// We found at least two pairs. Return the most valuable
			return getHighCard(pairs);
		}
		else
		{
			return null;
		}
	}
}
