package com.yotilla.poker.result;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.yotilla.poker.card.CardValue;

/**
 * Description:
 *
 * <br>
 * Date: 27.12.2020
 *
 * @author Manuel
 *
 */
class PokerHandComparatorTest
{
	private final PokerHandComparator comp = new PokerHandComparator();

	/**
	 * mock a poker hand for test purposes
	 *
	 * @param ranking     card ranking
	 * @param rankCards   rank cards, if necessary
	 * @param kickerCards kicker cards, if necessary
	 * @return Poker hand mock
	 */
	private PokerHand getHand(final PokerHandRanking ranking, final List<CardValue> rankCards,
			final List<CardValue> kickerCards)
	{
		PokerHand hand = Mockito.mock(PokerHand.class);

		Mockito.when(hand.getRanking()).thenReturn(ranking);
		Mockito.when(hand.getRankCards()).thenReturn(rankCards);
		Mockito.when(hand.getKickerCards()).thenReturn(kickerCards);

		return hand;
	}

	/**
	 * comparatorIsNullSafe
	 */
	@Test
	void comparatorIsNullSafe()
	{
		PokerHand hand = getHand(null, null, null);

		int result = comp.compare(null, null);
		assertSame(0, result, "Null and null are equal.");

		result = comp.compare(hand, null);
		assertTrue(result > 0, "Anything is more than null.");

		result = comp.compare(null, hand);
		assertTrue(result < 0, "Null is less everything else the game  has to offer.");
	}

	/**
	 * twoPairsRankHigherThanOnePair
	 */
	@Test
	void twoPairsRankHigherThanOnePair()
	{
		PokerHand onePair = getHand(PokerHandRanking.ONE_PAIR, null, null);
		PokerHand twoPairs = getHand(PokerHandRanking.TWO_PAIRS, null, null);

		int result = comp.compare(twoPairs, onePair);
		assertTrue(result > 0, "Two pairs must rank above one pair.");
	}

	/**
	 * straightScoresLessThanFullHouse
	 */
	@Test
	void straightScoresLessThanFullHouse()
	{
		PokerHand straight = getHand(PokerHandRanking.STRAIGHT, null, null);
		PokerHand fullHouse = getHand(PokerHandRanking.FULL_HOUSE, null, null);

		int result = comp.compare(straight, fullHouse);
		assertTrue(result < 0, "A straight must rank below a full house.");
	}

	/**
	 * onePairTieIsBrokenByRankCard
	 */
	@Test
	void onePairTieIsBrokenByRankCard()
	{
		PokerHand pairOfFives = getHand(PokerHandRanking.ONE_PAIR, Arrays.asList(CardValue.FIVE), null);
		PokerHand pairOfKings = getHand(PokerHandRanking.ONE_PAIR, Arrays.asList(CardValue.KING), null);

		int result = comp.compare(pairOfKings, pairOfFives);
		assertTrue(result > 0, "A pair of Kings beats a pair of fives.");
	}

	/**
	 * twoPairTieCanBeBrokenByFirstRankCard
	 */
	@Test
	void twoPairTieCanBeBrokenByFirstRankCard()
	{
		PokerHand pairOfKingsAndPairOfFours = getHand(PokerHandRanking.TWO_PAIRS,
				Arrays.asList(CardValue.KING, CardValue.FOUR), null);
		PokerHand pairOfQueensAndPairOfJacks = getHand(PokerHandRanking.TWO_PAIRS,
				Arrays.asList(CardValue.QUEEN, CardValue.JACK), null);

		int result = comp.compare(pairOfKingsAndPairOfFours, pairOfQueensAndPairOfJacks);
		assertTrue(result > 0, "Among two pairs, the Kings still beat the queens.");
	}

	/**
	 * twoPairTieCanBeBrokenBySecondRankCard
	 */
	@Test
	void twoPairTieCanBeBrokenBySecondRankCard()
	{
		PokerHand pairOfKingsAndPairOfJacks = getHand(PokerHandRanking.TWO_PAIRS,
				Arrays.asList(CardValue.KING, CardValue.JACK), null);
		PokerHand pairOfKingsAndPairOfFours = getHand(PokerHandRanking.TWO_PAIRS,
				Arrays.asList(CardValue.KING, CardValue.FOUR), null);

		int result = comp.compare(pairOfKingsAndPairOfJacks, pairOfKingsAndPairOfFours);
		assertTrue(result > 0, "The king pairs are equal, yet the jacks beat the fours.");
	}

	/**
	 * twoPairTieCanBeBrokenByKickerCard
	 */
	@Test
	void twoPairTieCanBeBrokenByKickerCard()
	{
		// Assume the very unlikely case that two players managed to draw the exact same
		// pairs. The only difference is in the high card.
		PokerHand minorPairOfKingsAndPairOfJacks = getHand(PokerHandRanking.TWO_PAIRS,
				Arrays.asList(CardValue.KING, CardValue.JACK), Arrays.asList(CardValue.FOUR));

		PokerHand majorPairOfKingsAndPairOfJacks = getHand(PokerHandRanking.TWO_PAIRS,
				Arrays.asList(CardValue.KING, CardValue.JACK), Arrays.asList(CardValue.TEN));

		int result = comp.compare(minorPairOfKingsAndPairOfJacks, majorPairOfKingsAndPairOfJacks);

		assertTrue(result < 0, "The minor two pair combo should lose by its high card.");
	}
}
