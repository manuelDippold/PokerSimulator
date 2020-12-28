package com.yotilla.poker.result.evaluator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.SortOrder;

import com.yotilla.poker.card.Card;
import com.yotilla.poker.card.CardValue;
import com.yotilla.poker.card.CardValueComparator;
import com.yotilla.poker.card.HandOfCards;
import com.yotilla.poker.error.HandExceededException;
import com.yotilla.poker.result.PokerHand;

/**
 * Description:
 * This interface describes the logic to deduce a poker hand from a hand of cards.
 * <br>
 * Date: 28.12.2020
 *
 * @author Manuel
 *
 */
public interface PokerHandEvaluator
{
	CardValueComparator cardValueComparatorDescending = new CardValueComparator().setOrder(SortOrder.DESCENDING);

	/**
	 * Attempt to find a PokerHand in this HandOfCards and return the according result.<br>
	 * If the hand we're looking for is not present, return null.
	 *
	 * @param hand Hand of cards provided
	 * @return PokerHand as a result. Null, if nothing was found.
	 */
	public PokerHand evaluate(final HandOfCards hand);

	/**
	 * transform a list of cards to their values. Sorts them descending, i.e. highest first.
	 *
	 * @param cards cards of interest
	 * @return List of card values. Sorted in descending order.
	 */
	default List<CardValue> cardsToSortedCardValues(final List<Card> cards)
	{
		if (cards == null)
		{
			return Collections.emptyList();
		}

		List<CardValue> values = new ArrayList<>();
		cards.stream().forEach(c -> values.add(c.getCardValue()));

		// sort, highest first
		values.sort(cardValueComparatorDescending);

		return values;
	}

	/**
	 * Transform a list of cards to sorted list of their unique values
	 *
	 * @param cards cards to analyze
	 * @return list of cards.
	 */
	default List<CardValue> cardsToSortedSetList(final List<Card> cards)
	{
		List<CardValue> valueList = cardsToSortedCardValues(cards);

		// Linked hash set to remember the order.
		Set<CardValue> cardValues = new LinkedHashSet<>(valueList);

		List<CardValue> ret = new ArrayList<>();
		ret.addAll(cardValues);
		return ret;
	}

	/**
	 * Make a quick copy of a hand of cards
	 *
	 * @param toCopy hand to copy
	 * @return hand of cards
	 * @throws HandExceededException when the cards exceed the limit
	 */
	default HandOfCards copyHandOfCards(final HandOfCards toCopy)
	{
		if (toCopy == null)
		{
			return null;
		}

		HandOfCards copy = new HandOfCards();

		try
		{
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
		}
		catch (HandExceededException e)
		{
			Logger.getGlobal().log(Level.SEVERE,
					String.format("An error occurred while copying a hand of cards: %s", e.getMessage()), e);
		}

		return copy;
	}

	/**
	 * Get the cards of this hand that can serve as kickers, i.e. that are not part
	 * of the ranking.<br>
	 * For example, in a hand with one pair, the three remaining cards will be
	 * returned if the pair is in {@code partOfRanking}<br>
	 * The kicker cards will be sorted in descending order, i.e. highest first.
	 *
	 * @param hand          the hand
	 * @param partOfRanking cards that are part of the ranking
	 * @return List of cards.
	 */
	default List<Card> getKickerCards(final HandOfCards hand, final Collection<Card> partOfRanking)
	{
		if (hand == null)
		{
			return Collections.emptyList();
		}

		if (partOfRanking == null)
		{
			return hand.getCards();
		}

		// Make a working copy of the hand and remove the cards that are part of the ranking.
		HandOfCards workingCopy = null;
		try
		{
			workingCopy = copyHandOfCards(hand);
			workingCopy.removeCards(partOfRanking);
		}
		catch (HandExceededException e)
		{
			Logger.getGlobal().log(Level.SEVERE,
					String.format("An error occurred while deducing the kicker cards from a hand: %s", e.getMessage()),
					e);
		}

		// The remaining cards are the Kicker cards. Sort and return.
		if (workingCopy != null)
		{
			// Cards implement comparable, so we don't need a comparator here.
			// Use reverse order so the highest Card comes first.
			List<Card> kickerCards = workingCopy.getCards();
			kickerCards.sort(Collections.reverseOrder());
			return workingCopy.getCards();
		}

		return Collections.emptyList();
	}
}
