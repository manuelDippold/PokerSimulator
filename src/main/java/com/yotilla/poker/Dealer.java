package com.yotilla.poker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.SortOrder;

import com.yotilla.poker.card.Card;
import com.yotilla.poker.card.CardSuit;
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
		if (hand == null)
		{
			return null;
		}

		return new PokerHand(PokerHandRanking.HIGH_CARD, cardsToSortedCardValues(hand.getCards()),
				Collections.emptyList());
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
		return getMultipleHand(hand, 2);
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
	 * Attempts to find three of a kind in this hand and returns an according result.
	 *
	 * @param hand hand to analyze
	 * @return poker hand
	 */
	public PokerHand getThreeOfKind(final HandOfCards hand)
	{
		return getMultipleHand(hand, 3);
	}

	/**
	 * Attempts to find a straight in this hand and returns an according result.
	 *
	 * @param hand hand to analyze
	 * @return poker hand or null.
	 * @throws HandExceededException in case of an error
	 */
	public PokerHand getStraight(final HandOfCards hand) throws HandExceededException
	{
		if (hand != null)
		{
			// make a working copy of the hand and order it
			List<Card> workingCopy = copyHandOfCards(hand).getCards();
			workingCopy.sort(null);

			// special case: If the hand contains an ace, it can be the starting or the ending card.
			accountForAceBeginningStraight(workingCopy);

			boolean skipped = false;
			int highestvalue = 0;
			int runningCardValue = 0;

			// See if we can step trough the ordered cards without skipping one.
			for (Card card : workingCopy)
			{
				int thisCardValue = card.getCardValue().getNumericalValue();

				if (runningCardValue > 0 && Math.abs(thisCardValue - runningCardValue) > 1)
				{
					// we skipped a card. Break here.
					skipped = true;
					break;
				}
				else
				{
					runningCardValue = thisCardValue;

					if (thisCardValue > highestvalue)
					{
						highestvalue = thisCardValue;
					}
				}
			}

			if (!skipped)
			{
				// We found the straight, construct a result. The ranking card is the highest card of the straight.
				return new PokerHand(PokerHandRanking.STRAIGHT,
						Arrays.asList(CardValue.getByNumericalValue(highestvalue)), Collections.emptyList());
			}
		}

		return null;
	}

	/**
	 * account for the fact that an ace can lead a straight.
	 * Do so by removing it if there is a two.
	 *
	 * @param workingCopy copy of cards to work with
	 */
	private void accountForAceBeginningStraight(final List<Card> workingCopy)
	{
		boolean containsAce = workingCopy.stream().anyMatch(c -> c.getCardValue() == CardValue.ACE);
		boolean containsTwo = workingCopy.stream().anyMatch(c -> c.getCardValue() == CardValue.TWO);

		// if this hand contains an ace, check if there is also a kind or a two, so it can form a straight.
		if (containsAce && containsTwo)
		{
			// If there are an ace and a two, remove the ace and continue.
			// The ace must begin the the straight this way, there is not other possibility.
			workingCopy.removeIf(c -> c.getCardValue() == CardValue.ACE);
		}
	}

	/**
	 * Attempts to find a flush in this hand and returns an according result.
	 *
	 * @param hand hand to analyze
	 * @return poker hand or null.
	 * @throws HandExceededException in case of an error
	 */
	public PokerHand getFlush(final HandOfCards hand) throws HandExceededException
	{
		if (hand != null)
		{
			boolean flush = true;
			CardSuit runningSuit = null;
			for (Card card : hand.getCards())
			{
				CardSuit thisSuit = card.getCardSuit();

				if (runningSuit != null && runningSuit != thisSuit)
				{
					// there are differing suits in this hand. This is not a flush.
					flush = false;
					break;
				}
				runningSuit = thisSuit;
			}

			if (flush)
			{
				// We found a flush, construct an according result.
				// All flush cards serve as rank cards
				return new PokerHand(PokerHandRanking.FLUSH, cardsToSortedCardValues(hand.getCards()),
						Collections.emptyList());
			}
		}

		return null;
	}

	/**
	 * Attempts to find a full house in this hand and returns an according result.
	 *
	 * @param hand hand to analyze
	 * @return poker hand or null.
	 * @throws HandExceededException in case of an error
	 */
	public PokerHand getFullHouse(final HandOfCards hand) throws HandExceededException
	{
		if (hand != null)
		{
			CardCollector collector = new CardCollector();
			CardValue tripleValue = null;
			CardValue pairValue = null;

			// load all cards to collector
			collector.addCards(hand.getCards());

			// Loop over entry set, see if there are both a triple and a pair.
			for (Entry<CardValue, List<Card>> entry : collector.entrySet())
			{
				CardValue value = entry.getKey();
				int cards = entry.getValue().size();

				if (cards == 3)
				{
					tripleValue = value;
				}
				if (cards == 2)
				{
					pairValue = value;
				}
			}

			if (tripleValue != null && pairValue != null)
			{
				// we found a triple and a pair => a full house. Build a result.
				return new PokerHand(PokerHandRanking.FULL_HOUSE, Arrays.asList(tripleValue, pairValue),
						Collections.emptyList());
			}
		}

		return null;
	}

	/**
	 * Attempts to find four of a kind in this hand and returns an according result.
	 *
	 * @param hand hand to analyze
	 * @return poker hand or null.
	 * @throws HandExceededException in case of an error
	 */
	public PokerHand getFourOfKind(final HandOfCards hand)
	{
		return getMultipleHand(hand, 4);
	}

	/**
	 * Attempts to find multiples (pairs, triples, fours) in this hand and returns an according result.
	 * Does not work for two pairs, though.
	 *
	 * @param handOfCards     hand of cards to analyze
	 * @param desiredMultiple desired multiple we want. 2, 3 or 4
	 * @return poker hand or null
	 */
	private PokerHand getMultipleHand(final HandOfCards hand, final int desiredMultiple)
	{
		if (hand != null && desiredMultiple >= 2 && desiredMultiple <= 4)
		{
			CardCollector collector = new CardCollector();
			CardValue multipleCardValue = null;

			for (Card card : hand.getCards())
			{
				collector.addCard(card);

				CardValue val = card.getCardValue();
				if (collector.getAmountBehindKey(val) >= desiredMultiple)
				{
					multipleCardValue = val;
				}
			}

			if (multipleCardValue != null)
			{
				// We found it. Build a result.
				List<Card> remainingCards = getKickerCards(hand, collector.get(multipleCardValue));

				PokerHandRanking ranking;

				switch (desiredMultiple) {
				case 2:
					ranking = PokerHandRanking.ONE_PAIR;
					break;
				case 3:
					ranking = PokerHandRanking.THREE_OF_A_KIND;
					break;
				case 4:
					ranking = PokerHandRanking.FOUR_OF_A_KIND;
					break;
				default: // Impossible, but alas...
					return null;

				}

				return new PokerHand(ranking, Arrays.asList(multipleCardValue),
						cardsToSortedCardValues(remainingCards));
			}
		}

		return null;
	}

	/**
	 * Attempts to a straight flush in this hand and returns an according result.
	 *
	 * @param hand hand to analyze
	 * @return poker hand or null.
	 * @throws HandExceededException in case of an error
	 */
	public PokerHand getStraightFlush(final HandOfCards hand) throws HandExceededException
	{
		if (hand != null)
		{
			// A straight flush is a straight that is also a flush. The straight leading card is the rank card.
			PokerHand straight = getStraight(hand);

			if (straight != null)
			{
				PokerHand flush = getFlush(hand);

				if (flush != null)
				{
					// We found a straight flush. Build an according result.
					return new PokerHand(PokerHandRanking.STRAIGHT_FLUSH, straight.getRankCards(),
							Collections.emptyList());
				}
			}

		}

		return null;
	}

	/**
	 * Attempts to a royal flush in this hand and returns an according result.
	 *
	 * @param hand hand to analyze
	 * @return poker hand or null.
	 * @throws HandExceededException in case of an error
	 */
	public PokerHand getRoyalFlush(final HandOfCards hand) throws HandExceededException
	{
		if (hand != null)
		{
			// A royal flush is a straight flush that leads with an ace.
			PokerHand straightFlush = getStraightFlush(hand);

			if (straightFlush != null && straightFlush.getRankCards().get(0) == CardValue.ACE)
			{
				// A straight flush with an Ace leading it. That's called a royal flush.
				return new PokerHand(PokerHandRanking.ROYAL_FLUSH, Collections.emptyList(), Collections.emptyList());
			}

		}

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
	 * transform a list of cards to their values. Sorts them descending, i.e. highest first.
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
