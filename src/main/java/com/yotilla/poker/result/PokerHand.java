package com.yotilla.poker.result;

import java.util.List;

import com.yotilla.poker.card.CardValue;

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
 *
 */
public class PokerHand
{
	private final PokerHandRanking ranking;
	private final List<CardValue> rankCards;
	private final List<CardValue> kickerCards;

	/**
	 * @param argRanking     The overall ranking of the result: Pair, Straight etc.
	 * @param argRankCards   The rank cards of the result in descending order.
	 * @param argKickerCards Kicker cards in descending order. A
	 */
	public PokerHand(PokerHandRanking argRanking, List<CardValue> argRankCards, List<CardValue> argKickerCards)
	{
		super();
		ranking = argRanking;
		rankCards = argRankCards;
		kickerCards = argKickerCards;
	}

	/**
	 * @return the ranking
	 */
	public PokerHandRanking getRanking()
	{
		return ranking;
	}

	/**
	 * @return the rankCards
	 */
	public List<CardValue> getRankCards()
	{
		return rankCards;
	}

	/**
	 * @return the kickerCards
	 */
	public List<CardValue> getKickerCards()
	{
		return kickerCards;
	}

}
