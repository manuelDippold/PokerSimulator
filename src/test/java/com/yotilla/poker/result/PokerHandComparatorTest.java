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
		PokerHand hand = TestUtils.getPokerHandSpy(null, null, null);

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
		PokerHand onePair = TestUtils.getPokerHandSpy(PokerHandRanking.ONE_PAIR, null, null);
		PokerHand twoPairs = TestUtils.getPokerHandSpy(PokerHandRanking.TWO_PAIRS, null, null);

		int result = comp.compare(twoPairs, onePair);
		assertTrue(result > 0, "Two pairs must rank above one pair.");
	}

	/**
	 * straightScoresLessThanFullHouse
	 */
	@Test
	void straightScoresLessThanFullHouse()
	{
		PokerHand straight = TestUtils.getPokerHandSpy(PokerHandRanking.STRAIGHT, null, null);
		PokerHand fullHouse = TestUtils.getPokerHandSpy(PokerHandRanking.FULL_HOUSE, null, null);

		int result = comp.compare(straight, fullHouse);
		assertTrue(result < 0, "A straight must rank below a full house.");
	}

	/**
	 * onePairTieIsBrokenByRankCard
	 */
	@Test
	void onePairTieIsBrokenByRankCard()
	{
		PokerHand pairOfFives = TestUtils.getPokerHandSpy(PokerHandRanking.ONE_PAIR, Arrays.asList(CardValue.FIVE), null);
		PokerHand pairOfKings = TestUtils.getPokerHandSpy(PokerHandRanking.ONE_PAIR, Arrays.asList(CardValue.KING), null);

		int result = comp.compare(pairOfKings, pairOfFives);
		assertTrue(result > 0, "A pair of Kings beats a pair of fives.");
	}

	/**
	 * twoPairTieCanBeBrokenByFirstRankCard
	 */
	@Test
	void twoPairTieCanBeBrokenByFirstRankCard()
	{
		PokerHand pairOfKingsAndPairOfFours = TestUtils.getPokerHandSpy(PokerHandRanking.TWO_PAIRS,
				Arrays.asList(CardValue.KING, CardValue.FOUR), null);
		PokerHand pairOfQueensAndPairOfJacks = TestUtils.getPokerHandSpy(PokerHandRanking.TWO_PAIRS,
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
		PokerHand pairOfKingsAndPairOfJacks = TestUtils.getPokerHandSpy(PokerHandRanking.TWO_PAIRS,
				Arrays.asList(CardValue.KING, CardValue.JACK), null);
		PokerHand pairOfKingsAndPairOfFours = TestUtils.getPokerHandSpy(PokerHandRanking.TWO_PAIRS,
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
		PokerHand minorPairOfKingsAndPairOfJacks = TestUtils.getPokerHandSpy(PokerHandRanking.TWO_PAIRS,
				Arrays.asList(CardValue.KING, CardValue.JACK), Arrays.asList(CardValue.FOUR));

		PokerHand majorPairOfKingsAndPairOfJacks = TestUtils.getPokerHandSpy(PokerHandRanking.TWO_PAIRS,
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
		PokerHand justQueenHighCard = TestUtils.getPokerHandSpy(null, null, Arrays.asList(CardValue.QUEEN));
		PokerHand justNineHighCard = TestUtils.getPokerHandSpy(null, null, Arrays.asList(CardValue.NINE));

		int result = comp.compare(justQueenHighCard, justNineHighCard);
		assertTrue(result > 0, "High cards only, queen should beat nine.");
	}

	/**
	 * straightTieCanBeBrokenByTopCard
	 */
	@Test
	void straightTieCanBeBrokenByTopCard()
	{
		PokerHand kingStraight = TestUtils.getPokerHandSpy(PokerHandRanking.STRAIGHT, Arrays.asList(CardValue.KING), null);
		PokerHand nineStraight = TestUtils.getPokerHandSpy(PokerHandRanking.STRAIGHT, Arrays.asList(CardValue.NINE), null);

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
		PokerHand fullHouseQueensAndNines = TestUtils.getPokerHandSpy(PokerHandRanking.FULL_HOUSE,
				Arrays.asList(CardValue.QUEEN, CardValue.NINE), null);
		PokerHand fullHouseQueensAndFours = TestUtils.getPokerHandSpy(PokerHandRanking.FULL_HOUSE,
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
		PokerHand fourKingsAndOneThree = TestUtils.getPokerHandSpy(PokerHandRanking.FOUR_OF_A_KIND,
				Arrays.asList(CardValue.KING), Arrays.asList(CardValue.THREE));
		PokerHand fourTensAndOneAce = TestUtils.getPokerHandSpy(PokerHandRanking.FOUR_OF_A_KIND,
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
		PokerHand flushWithKing = TestUtils.getPokerHandSpy(PokerHandRanking.FLUSH,
				Arrays.asList(CardValue.KING, CardValue.TEN), Collections.emptyList());
		PokerHand flushWithQueen = TestUtils.getPokerHandSpy(PokerHandRanking.FLUSH,
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
		PokerHand minorFlush = TestUtils.getPokerHandSpy(PokerHandRanking.FLUSH,
				Arrays.asList(CardValue.TWO, CardValue.TEN, CardValue.FIVE, CardValue.SIX, CardValue.TEN),
				Collections.emptyList());

		PokerHand majorFlush = TestUtils.getPokerHandSpy(PokerHandRanking.FLUSH,
				Arrays.asList(CardValue.THREE, CardValue.TEN, CardValue.FIVE, CardValue.SIX, CardValue.TEN),
				Collections.emptyList());

		int result = comp.compare(minorFlush, majorFlush);
		assertTrue(result < 0, "Fifth card: Three trumps two.");
	}
}
