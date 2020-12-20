package com.yotilla.poker;

import java.util.List;

import com.yotilla.poker.card.Card;

/**
 * Description: A hand of five cards. <br>
 * Date: 20.12.2020
 *
 * @author Manuel
 *
 */
public class HandOfCards
{
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
	 */
	public void setCards(List<Card> argCards)
	{
		cards = argCards;
	}

	/**
	 * Add a card to this hand. Will only work
	 *
	 * @param toAdd Cards to add to this hand
	 * @throws HandExceededException whenever someone tries to add more than five
	 *                               cards.
	 */
	public void addCard(final Card... toAdd) throws HandExceededException
	{
		// TODO
	}
}
