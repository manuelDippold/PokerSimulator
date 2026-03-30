package com.yotilla.poker.result.evaluator;

import com.yotilla.poker.card.Card;
import com.yotilla.poker.card.CardValue;
import com.yotilla.poker.card.CardValueComparator;
import com.yotilla.poker.card.HandOfCards;
import com.yotilla.poker.error.HandExceededException;
import com.yotilla.poker.result.PokerHand;

import java.util.*;

/**
 * Description:
 * This interface describes the logic to deduce a poker hand from a hand of cards.
 * <br>
 * Date: 28.12.2020
 *
 * @author Manuel
 *
 */
public interface PokerHandEvaluator {
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
    default List<CardValue> cardsToSortedCardValues(final List<Card> cards) {
        if (cards == null) {
            return Collections.emptyList();
        }

        List<CardValue> values = new ArrayList<>();
        cards.stream().forEach(c -> values.add(c.cardValue()));

        // sort, highest first
        values.sort(new CardValueComparator().reversed());

        return values;
    }

    /**
     * Transform a list of cards to sorted list of their unique values
     *
     * @param cards cards to analyze
     * @return list of cards.
     */
    default List<CardValue> cardsToSortedSetList(final List<Card> cards) {
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
     */
    default HandOfCards copyHandOfCards(final HandOfCards toCopy) {
        Objects.requireNonNull(toCopy, "Cannot copy a null hand.");

        try {
            HandOfCards copy = new HandOfCards();

            if (toCopy.getAmountOfCards() == 0) {
                copy.setCards(Collections.emptyList());
                return copy;
            }

            for (Card existing : toCopy.getCards()) {
                copy.addCard(existing);
            }

            return copy;
        } catch (HandExceededException e) {
            throw new IllegalStateException("Failed to copy hand of cards — this is a bug.", e);
        }
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
    default List<Card> getKickerCards(final HandOfCards hand, final Collection<Card> partOfRanking) {
        if (hand == null) {
            return Collections.emptyList();
        }

        if (partOfRanking == null) {
            return hand.getCards();
        }

        try {
            HandOfCards workingCopy = copyHandOfCards(hand);

            if (!workingCopy.removeCards(partOfRanking)) {
                throw new IllegalStateException(
                        String.format("Failed to remove ranking cards from hand — this is a bug: %s", partOfRanking));
            }

            List<Card> kickerCards = workingCopy.getCards();
            kickerCards.sort(Collections.reverseOrder());
            return kickerCards;
        } catch (HandExceededException e) {
            throw new IllegalStateException("Failed to determine kicker cards — this is a bug.", e);
        }
    }
}
