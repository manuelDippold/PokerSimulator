package com.yotilla.poker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
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
import com.yotilla.poker.result.PokerHandRanking;

/**
 * Description: Core service class in a game of Poker. <br>
 * Date: 26.12.2020
 *
 * @author Manuel
 *
 */
public class Dealer
{
	private static final Logger LOG = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	private CardValueComparator cardValueComparatorDescending = new CardValueComparator()
			.setOrder(SortOrder.DESCENDING);

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
	 * @return Poker hand with the high card of this hand. Null if the hand is
	 *         empty.
	 */
	public PokerHand getHighCard(final HandOfCards hand)
	{
		CardValue highCard = hand == null ? null : getHighCard(hand.getCards());

		if (highCard != null)
		{
			return new PokerHand(PokerHandRanking.HIGH_CARD, Arrays.asList(highCard), null);
		}
		else
		{
			return null;
		}
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
	public PokerHand getPair(final HandOfCards hand)
	{
		if (hand != null)
		{
			// Two cards form a pair when their value is equal to one another
			Map<CardValue, Card> pairCandidates = new EnumMap<>(CardValue.class);
			List<Card> pair = new ArrayList<>();
			CardValue pairValue = null;

			for (Card card : hand.getCards())
			{
				CardValue value = card.getCardValue();

				// If this value was already encountered at this point, we have a pair.
				if (pairCandidates.containsKey(value)) // NOSONAR - computeIf... doesn't serve us here.
				{
					pair.add(card);
					pair.add(pairCandidates.get(value));

					pairValue = value;
					break;
				}

				pairCandidates.put(value, card);
			}

			if (pairValue != null)
			{
				// We found a pair and we know its value. Now determine the high cards beside the pair.
				List<Card> kickerCards = getKickerCards(hand, pair);
				return new PokerHand(PokerHandRanking.ONE_PAIR, Arrays.asList(pairValue),
						cardsToSortedCardValues(kickerCards));
			}
		}

		// No pair found, return null
		return null;
	}

	/**
	 * If this hand contains two pairs, this method returns an according result.
	 *
	 * @param hand hand to check
	 * @return poker hand or null
	 */
	public PokerHand getTwoPairs(final HandOfCards hand)
	{
		if (hand != null)
		{

			Map<CardValue, Card> pairCandidates = new EnumMap<>(CardValue.class);
			List<Card> pairs = new ArrayList<>();

			for (Card card : hand.getCards())
			{
				CardValue value = card.getCardValue();

				// If we encountered this value before, this is at least a pair.
				if (pairCandidates.containsKey(value))
				{
					pairs.add(pairCandidates.get(value));
					pairs.add(card);
				}
				else
				{
					pairCandidates.put(value, card);
				}
			}

			if (pairs.size() >= 4)
			{
				// We found at least two pairs.
				return new PokerHand(PokerHandRanking.TWO_PAIRS, cardsToSortedSetList(pairs),
						cardsToSortedCardValues(getKickerCards(hand, pairs)));
			}
		}
		// No two pairs found. return null.
		return null;
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
	public List<Card> getKickerCards(final HandOfCards hand, final Collection<Card> partOfRanking)
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
			LOG.log(Level.SEVERE,
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

	/**
	 * transform a list of cards to their values. Sorts them descending, i.e. hoghest first.
	 *
	 * @param cards cards of interest
	 * @return List of card values. Sorted in descending order.
	 */
	private List<CardValue> cardsToSortedCardValues(final List<Card> cards)
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
	private List<CardValue> cardsToSortedSetList(final List<Card> cards)
	{
		List<CardValue> valueList = cardsToSortedCardValues(cards);

		// Linked hash set to remember the order.
		Set<CardValue> cardValues = new LinkedHashSet<>(valueList);

		List<CardValue> ret = new ArrayList<>();
		ret.addAll(cardValues);
		return ret;
	}
}
