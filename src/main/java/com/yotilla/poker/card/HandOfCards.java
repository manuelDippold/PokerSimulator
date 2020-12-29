package com.yotilla.poker.card;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
	public static final int HAND_SIZE = 5;

	private List<Card> cards;

	/**
	 * Construct a new hand.
	 */
	public HandOfCards()
	{
		cards = new ArrayList<>();
	}

	/**
	 * @return the cards
	 */
	public List<Card> getCards()
	{
		return cards;
	}

	/**
	 * get the amount of cards in this hand.
	 *
	 * @return integer
	 */
	public int getAmountOfCards()
	{
		return cards != null ? cards.size() : 0;
	}

	/**
	 * returns true if there are no cards on this hand.
	 *
	 * @return boolean
	 */
	public boolean isEmpty()
	{
		return cards == null || cards.isEmpty();
	}

	/**
	 * @param argCards the cards to set
	 * @throws HandExceededException if argCards contains more elements than allowed
	 */
	public void setCards(List<Card> argCards) throws HandExceededException
	{
		if (argCards != null && argCards.size() > HAND_SIZE)
		{
			throw new HandExceededException(
					String.format("A hand of cards must not hold more than %d cards.", HAND_SIZE));
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
		if (toAdd == null)
		{
			return;
		}

		if (cards == null)
		{
			cards = new ArrayList<>(HAND_SIZE);
		}

		if (cards.size() == HAND_SIZE)
		{
			throw new HandExceededException(
					String.format("A hand of cards must not hold more than %d cards.", HAND_SIZE));
		}

		cards.add(toAdd);
	}

	/**
	 * return several cards from this hand
	 *
	 * @param toRemove cards to remove
	 * @return true if all stated cards were successfully removed
	 * @throws HandExceededException if the hand was already empty.
	 */
	public boolean removeCards(final Collection<Card> toRemove) throws HandExceededException
	{
		if (toRemove == null)
		{
			return false;
		}

		boolean success = true;

		for (Card cardToRemove : toRemove)
		{
			success &= removeCard(cardToRemove);
		}

		return success;
	}

	/**
	 * remove the specified card from the hand.
	 *
	 * @param toRemove card you wish removed
	 * @return success boolean
	 * @throws HandExceededException if the hand was already empty.
	 */
	public boolean removeCard(final Card toRemove) throws HandExceededException
	{
		if (cards == null || cards.isEmpty())
		{
			throw new HandExceededException("removeCard: Hand was already empty.");
		}

		if (toRemove == null || !cards.contains(toRemove))
		{
			return false;
		}

		return cards.remove(toRemove);
	}
}
