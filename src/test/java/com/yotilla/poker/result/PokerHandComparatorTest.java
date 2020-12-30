package com.yotilla.poker.result;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import com.yotilla.poker.TestUtils;
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
	 * comparatorIsNullSafe
	 */
	@Test
	void comparatorIsNullSafe()
	{
		PokerHand hand = TestUtils.getPokerHandMock(null, null, null);

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
		PokerHand onePair = TestUtils.getPokerHandMock(PokerHandRanking.ONE_PAIR, null, null);
		PokerHand twoPairs = TestUtils.getPokerHandMock(PokerHandRanking.TWO_PAIRS, null, null);

		int result = comp.compare(twoPairs, onePair);
		assertTrue(result > 0, "Two pairs must rank above one pair.");
	}

	/**
	 * straightScoresLessThanFullHouse
	 */
	@Test
	void straightScoresLessThanFullHouse()
	{
		PokerHand straight = TestUtils.getPokerHandMock(PokerHandRanking.STRAIGHT, null, null);
		PokerHand fullHouse = TestUtils.getPokerHandMock(PokerHandRanking.FULL_HOUSE, null, null);

		int result = comp.compare(straight, fullHouse);
		assertTrue(result < 0, "A straight must rank below a full house.");
	}

	/**
	 * onePairTieIsBrokenByRankCard
	 */
	@Test
	void onePairTieIsBrokenByRankCard()
	{
		PokerHand pairOfFives = TestUtils.getPokerHandMock(PokerHandRanking.ONE_PAIR, Arrays.asList(CardValue.FIVE), null);
		PokerHand pairOfKings = TestUtils.getPokerHandMock(PokerHandRanking.ONE_PAIR, Arrays.asList(CardValue.KING), null);

		int result = comp.compare(pairOfKings, pairOfFives);
		assertTrue(result > 0, "A pair of Kings beats a pair of fives.");
	}

	/**
	 * twoPairTieCanBeBrokenByFirstRankCard
	 */
	@Test
	void twoPairTieCanBeBrokenByFirstRankCard()
	{
		PokerHand pairOfKingsAndPairOfFours = TestUtils.getPokerHandMock(PokerHandRanking.TWO_PAIRS,
				Arrays.asList(CardValue.KING, CardValue.FOUR), null);
		PokerHand pairOfQueensAndPairOfJacks = TestUtils.getPokerHandMock(PokerHandRanking.TWO_PAIRS,
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
		PokerHand pairOfKingsAndPairOfJacks = TestUtils.getPokerHandMock(PokerHandRanking.TWO_PAIRS,
				Arrays.asList(CardValue.KING, CardValue.JACK), null);
		PokerHand pairOfKingsAndPairOfFours = TestUtils.getPokerHandMock(PokerHandRanking.TWO_PAIRS,
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
		PokerHand minorPairOfKingsAndPairOfJacks = TestUtils.getPokerHandMock(PokerHandRanking.TWO_PAIRS,
				Arrays.asList(CardValue.KING, CardValue.JACK), Arrays.asList(CardValue.FOUR));

		PokerHand majorPairOfKingsAndPairOfJacks = TestUtils.getPokerHandMock(PokerHandRanking.TWO_PAIRS,
				Arrays.asList(CardValue.KING, CardValue.JACK), Arrays.asList(CardValue.TEN));

		int result = comp.compare(minorPairOfKingsAndPairOfJacks, majorPairOfKingsAndPairOfJacks);

		assertTrue(result < 0, "The minor two pair combo should lose by its high card.");
	}

	/**
	 * voidOfAnyRankingsHighCardWins
	 */
	@Test
	void voidOfAnyRankingsHighCardWins()
	{
		PokerHand justQueenHighCard = TestUtils.getPokerHandMock(null, null, Arrays.asList(CardValue.QUEEN));
		PokerHand justNineHighCard = TestUtils.getPokerHandMock(null, null, Arrays.asList(CardValue.NINE));

		int result = comp.compare(justQueenHighCard, justNineHighCard);
		assertTrue(result > 0, "High cards only, queen should beat nine.");
	}

	/**
	 * straightTieCanBeBrokenByTopCard
	 */
	@Test
	void straightTieCanBeBrokenByTopCard()
	{
		PokerHand kingStraight = TestUtils.getPokerHandMock(PokerHandRanking.STRAIGHT, Arrays.asList(CardValue.KING), null);
		PokerHand nineStraight = TestUtils.getPokerHandMock(PokerHandRanking.STRAIGHT, Arrays.asList(CardValue.NINE), null);

		int result = comp.compare(nineStraight, kingStraight);
		assertTrue(result < 0, "A straight lead by a nine should lose to a straight lead by a King.");
	}

	/**
	 * fullHouseTieCanBeBrokenBySeconRankCard
	 */
	@Test
	void fullHouseTieCanBeBrokenBySeconRankCard()
	{
		// This combination is currently impossible to happen at this feature status,
		// but if we ever play, say, Texas hold'em, this can be the case.
		PokerHand fullHouseQueensAndNines = TestUtils.getPokerHandMock(PokerHandRanking.FULL_HOUSE,
				Arrays.asList(CardValue.QUEEN, CardValue.NINE), null);
		PokerHand fullHouseQueensAndFours = TestUtils.getPokerHandMock(PokerHandRanking.FULL_HOUSE,
				Arrays.asList(CardValue.QUEEN, CardValue.FOUR), null);

		int result = comp.compare(fullHouseQueensAndNines, fullHouseQueensAndFours);
		assertTrue(result > 0, "The queens in the upper pair match, but the nines beat the fours.");
	}

	/**
	 * fourTieIsBrokenByRankCardBeforeKickerCard
	 */
	@Test
	void fourTieIsBrokenByRankCardBeforeKickerCard()
	{
		PokerHand fourKingsAndOneThree = TestUtils.getPokerHandMock(PokerHandRanking.FOUR_OF_A_KIND,
				Arrays.asList(CardValue.KING), Arrays.asList(CardValue.THREE));
		PokerHand fourTensAndOneAce = TestUtils.getPokerHandMock(PokerHandRanking.FOUR_OF_A_KIND,
				Arrays.asList(CardValue.TEN), Arrays.asList(CardValue.ACE));

		int result = comp.compare(fourTensAndOneAce, fourKingsAndOneThree);
		assertTrue(result < 0, "The four kings beat the four tens, the ace does not matter here.");
	}

	/**
	 * flushTieCanBeBrokenByFirstCard
	 */
	@Test
	void flushTieCanBeBrokenByFirstCard()
	{
		PokerHand flushWithKing = TestUtils.getPokerHandMock(PokerHandRanking.FLUSH,
				Arrays.asList(CardValue.KING, CardValue.TEN), Collections.emptyList());
		PokerHand flushWithQueen = TestUtils.getPokerHandMock(PokerHandRanking.FLUSH,
				Arrays.asList(CardValue.QUEEN, CardValue.TEN), Collections.emptyList());

		int result = comp.compare(flushWithKing, flushWithQueen);
		assertTrue(result > 0, "The king should rank higher than the queen here.");
	}

	/**
	 * flushTieCanBeBrokenByFifthCard
	 */
	@Test
	void flushTieCanBeBrokenByFifthCard()
	{
		PokerHand minorFlush = TestUtils.getPokerHandMock(PokerHandRanking.FLUSH,
				Arrays.asList(CardValue.TWO, CardValue.TEN, CardValue.FIVE, CardValue.SIX, CardValue.TEN),
				Collections.emptyList());

		PokerHand majorFlush = TestUtils.getPokerHandMock(PokerHandRanking.FLUSH,
				Arrays.asList(CardValue.THREE, CardValue.TEN, CardValue.FIVE, CardValue.SIX, CardValue.TEN),
				Collections.emptyList());

		int result = comp.compare(minorFlush, majorFlush);
		assertTrue(result < 0, "Fifth card: Three trumps two.");
	}
}
