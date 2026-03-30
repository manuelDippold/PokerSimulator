package com.yotilla.poker.result;

import com.yotilla.poker.card.CardValue;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

/**
 * Description: This data structure resembles the result after the dealer
 * analyzed a hand of cards. It consists of the Ranking (straight, flush etc.),
 * the rank cards and the kicker cards.<br>
 * Rank cards are your tie breaker against an equal hand. E.g. with a pair of
 * sevens and a pair of fives, your rank cards are seven and five, in that
 * order.<br>
 * Kicker cards are your tie breakers beyond that, the high cards left after the
 * rank cards.<br>
 * Date: 27.12.2020
 *
 * @author Manuel
 * <p>
 * <p>
 * * @param ranking     The overall ranking of the result: Pair, Straight etc.
 * * @param rankCards   The rank cards of the result in descending order.
 * * @param kickerCards Kicker cards in descending order. A
 */
public record PokerHand(PokerHandRanking ranking, List<CardValue> rankCards, List<CardValue> kickerCards) {


    /**
     * @return the ranking
     */
    @Override
    public PokerHandRanking ranking() {
        return ranking;
    }

    /**
     * @return the rankCards
     */
    @Override
    public List<CardValue> rankCards() {
        return rankCards;
    }

    /**
     * @return the kickerCards
     */
    @Override
    public List<CardValue> kickerCards() {
        return kickerCards;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(ranking().name());

        if (!CollectionUtils.isEmpty(rankCards())) {
            sb.append(", ");

            List<String> rankCardNames = rankCards().stream().map(CardValue::name).toList();
            sb.append(String.join(", ", rankCardNames));
        }

        sb.append(".");

        if (kickerCards() != null && !kickerCards().isEmpty()) {
            sb.append(" Kickers: ");

            List<String> kickerCardNames = kickerCards().stream().map(CardValue::name).toList();
            sb.append(String.join(", ", kickerCardNames));
            sb.append(".");
        }

        return sb.toString();
    }
}
