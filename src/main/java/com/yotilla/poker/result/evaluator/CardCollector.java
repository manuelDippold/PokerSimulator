package com.yotilla.poker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;

import com.yotilla.poker.card.Card;
import com.yotilla.poker.card.CardValue;

/**
 * Description:
 * An extension of EnumMap to hold multiple values per key. A list, that is.
 * <br>
 * Date: 27.12.2020
 *
 * @author Manuel
 *
 */
public class CardCollector extends EnumMap<CardValue, List<Card>>
{
	/**
	 * @param argKeyType
	 */
	public CardCollector()
	{
		super(CardValue.class);
	}

	private static final long serialVersionUID = 5274749571351002385L;

	/**
	 * Add a card to the entry behind the key.
	 * If the entry doesn't exist, create it.
	 *
	 * @param card card to add.
	 */
	public void addCard(final Card card)
	{
		CardValue value = card.getCardValue();

		if (!containsKey(value))
		{
			put(value, new ArrayList<>());
		}

		get(value).add(card);
	}

	/**
	 * Add multiple cards
	 *
	 * @param cards cards to add.
	 */
	public void addCards(final Collection<Card> cards)
	{
		cards.stream().forEach(this::addCard);
	}

	/**
	 * get the size of the list mapped to this key
	 *
	 * @param key card value as key
	 * @return integer.
	 */
	public int getAmountBehindKey(final CardValue key)
	{
		if (!containsKey(key))
		{
			return 0;
		}

		return get(key).size();
	}
}
