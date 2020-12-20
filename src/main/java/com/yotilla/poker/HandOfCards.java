package com.yotilla.poker;

import java.util.ArrayList;
import java.util.List;

import com.yotilla.poker.card.Card;
import com.yotilla.poker.error.HandExceededException;

/**
 * Description: A hand of cards. <br>
 * Date: 20.12.2020
 *
 * @author Manuel
 *
 */
public class HandOfCards
{
	/**
	 * How many cards a hand is allowed to hold
	 */
	public static final int MAX_HAND_SIZE = 5;

	private List<Card> cards;

	/**
	 * @return the cards
	 */
	public List<Card> getCards()
	{
		return cards;
	}

	/**
	 * @param argCards the cards to set
	 * @throws HandExceededException if argCards contains more elements than allowed
	 */
	public void setCards(List<Card> argCards) throws HandExceededException
	{
		if (argCards != null && argCards.size() > MAX_HAND_SIZE)
		{
			throw new HandExceededException(
					String.format("A hand of cards must not hold more than %d cards.", MAX_HAND_SIZE));
		}

		cards = argCards;
	}

	/**
	 * Add multiple cards to this hand
	 *
	 * @param toAdd cards to add
	 * @throws HandExceededException if adding these cards exceeds the limit
	 */
	public void addCards(final Card... toAdd) throws HandExceededException
	{
		if (toAdd == null)
		{
			return;
		}

		if (toAdd.length > MAX_HAND_SIZE)
		{
			throw new HandExceededException(
					String.format("A hand of cards must not hold more than %d cards.", MAX_HAND_SIZE));
		}

		for (int i = 0; i < toAdd.length; i++)
		{
			addCard(toAdd[i]);
		}

	}

	/**
	 * Add one card to this hand.
	 *
	 * @param toAdd Card to add to this hand
	 * @throws HandExceededException whenever someone tries to add more than the
	 *                               allowed amount of cards.
	 */
	public void addCard(final Card toAdd) throws HandExceededException
	{
		// TODO: HandExceededException
		if (toAdd == null)
		{
			return;
		}

		if (cards == null)
		{
			cards = new ArrayList<>(MAX_HAND_SIZE);
		}

		cards.add(toAdd);
	}
}
