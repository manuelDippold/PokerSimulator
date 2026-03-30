package com.yotilla.poker.result;

import com.yotilla.poker.TestUtils;
import com.yotilla.poker.card.CardValue;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Description:
 *
 * <br>
 * Date: 27.12.2020
 *
 * @author Manuel
 *
 */
class PokerHandComparatorTest {
    private final PokerHandComparator comp = new PokerHandComparator();

    /**
     * comparatorIsNullSafe
     */
    @Test
    void comparatorIsNullSafe() {
        PokerHand hand = TestUtils.getPokerHand(null, null, null);

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
    void twoPairsRankHigherThanOnePair() {
        PokerHand onePair = TestUtils.getPokerHand(PokerHandRanking.ONE_PAIR, null, null);
        PokerHand twoPairs = TestUtils.getPokerHand(PokerHandRanking.TWO_PAIRS, null, null);

        int result = comp.compare(twoPairs, onePair);
        assertTrue(result > 0, "Two pairs must rank above one pair.");
    }

    /**
     * straightScoresLessThanFullHouse
     */
    @Test
    void straightScoresLessThanFullHouse() {
        PokerHand straight = TestUtils.getPokerHand(PokerHandRanking.STRAIGHT, null, null);
        PokerHand fullHouse = TestUtils.getPokerHand(PokerHandRanking.FULL_HOUSE, null, null);

        int result = comp.compare(straight, fullHouse);
        assertTrue(result < 0, "A straight must rank below a full house.");
    }

    /**
     * onePairTieIsBrokenByRankCard
     */
    @Test
    void onePairTieIsBrokenByRankCard() {
        PokerHand pairOfFives = TestUtils.getPokerHand(PokerHandRanking.ONE_PAIR, List.of(CardValue.FIVE), null);
        PokerHand pairOfKings = TestUtils.getPokerHand(PokerHandRanking.ONE_PAIR, List.of(CardValue.KING), null);

        int result = comp.compare(pairOfKings, pairOfFives);
        assertTrue(result > 0, "A pair of Kings beats a pair of fives.");
    }

    /**
     * twoPairTieCanBeBrokenByFirstRankCard
     */
    @Test
    void twoPairTieCanBeBrokenByFirstRankCard() {
        PokerHand pairOfKingsAndPairOfFours = TestUtils.getPokerHand(PokerHandRanking.TWO_PAIRS,
                List.of(CardValue.KING, CardValue.FOUR), null);
        PokerHand pairOfQueensAndPairOfJacks = TestUtils.getPokerHand(PokerHandRanking.TWO_PAIRS,
                List.of(CardValue.QUEEN, CardValue.JACK), null);

        int result = comp.compare(pairOfKingsAndPairOfFours, pairOfQueensAndPairOfJacks);
        assertTrue(result > 0, "Among two pairs, the Kings still beat the queens.");
    }

    /**
     * twoPairTieCanBeBrokenBySecondRankCard
     */
    @Test
    void twoPairTieCanBeBrokenBySecondRankCard() {
        PokerHand pairOfKingsAndPairOfJacks = TestUtils.getPokerHand(PokerHandRanking.TWO_PAIRS,
                List.of(CardValue.KING, CardValue.JACK), null);
        PokerHand pairOfKingsAndPairOfFours = TestUtils.getPokerHand(PokerHandRanking.TWO_PAIRS,
                List.of(CardValue.KING, CardValue.FOUR), null);

        int result = comp.compare(pairOfKingsAndPairOfJacks, pairOfKingsAndPairOfFours);
        assertTrue(result > 0, "The king pairs are equal, yet the jacks beat the fours.");
    }

    /**
     * twoPairTieCanBeBrokenByKickerCard
     */
    @Test
    void twoPairTieCanBeBrokenByKickerCard() {
        // Assume the very unlikely case that two players managed to draw the exact same
        // pairs. The only difference is in the high card.
        PokerHand minorPairOfKingsAndPairOfJacks = TestUtils.getPokerHand(PokerHandRanking.TWO_PAIRS,
                List.of(CardValue.KING, CardValue.JACK), List.of(CardValue.FOUR));

        PokerHand majorPairOfKingsAndPairOfJacks = TestUtils.getPokerHand(PokerHandRanking.TWO_PAIRS,
                List.of(CardValue.KING, CardValue.JACK), List.of(CardValue.TEN));

        int result = comp.compare(minorPairOfKingsAndPairOfJacks, majorPairOfKingsAndPairOfJacks);

        assertTrue(result < 0, "The minor two pair combo should lose by its high card.");
    }

    /**
     * voidOfAnyRankingsHighCardWins
     */
    @Test
    void voidOfAnyRankingsHighCardWins() {
        PokerHand justQueenHighCard = TestUtils.getPokerHand(null, null, List.of(CardValue.QUEEN));
        PokerHand justNineHighCard = TestUtils.getPokerHand(null, null, List.of(CardValue.NINE));

        int result = comp.compare(justQueenHighCard, justNineHighCard);
        assertTrue(result > 0, "High cards only, queen should beat nine.");
    }

    /**
     * straightTieCanBeBrokenByTopCard
     */
    @Test
    void straightTieCanBeBrokenByTopCard() {
        PokerHand kingStraight = TestUtils.getPokerHand(PokerHandRanking.STRAIGHT, List.of(CardValue.KING), null);
        PokerHand nineStraight = TestUtils.getPokerHand(PokerHandRanking.STRAIGHT, List.of(CardValue.NINE), null);

        int result = comp.compare(nineStraight, kingStraight);
        assertTrue(result < 0, "A straight lead by a nine should lose to a straight lead by a King.");
    }

    /**
     * fullHouseTieCanBeBrokenBySeconRankCard
     */
    @Test
    void fullHouseTieCanBeBrokenBySeconRankCard() {
        // This combination is currently impossible to happen at this feature status,
        // but if we ever play, say, Texas hold'em, this can be the case.
        PokerHand fullHouseQueensAndNines = TestUtils.getPokerHand(PokerHandRanking.FULL_HOUSE,
                List.of(CardValue.QUEEN, CardValue.NINE), null);
        PokerHand fullHouseQueensAndFours = TestUtils.getPokerHand(PokerHandRanking.FULL_HOUSE,
                List.of(CardValue.QUEEN, CardValue.FOUR), null);

        int result = comp.compare(fullHouseQueensAndNines, fullHouseQueensAndFours);
        assertTrue(result > 0, "The queens in the upper pair match, but the nines beat the fours.");
    }

    /**
     * fourTieIsBrokenByRankCardBeforeKickerCard
     */
    @Test
    void fourTieIsBrokenByRankCardBeforeKickerCard() {
        PokerHand fourKingsAndOneThree = TestUtils.getPokerHand(PokerHandRanking.FOUR_OF_A_KIND,
                List.of(CardValue.KING), List.of(CardValue.THREE));
        PokerHand fourTensAndOneAce = TestUtils.getPokerHand(PokerHandRanking.FOUR_OF_A_KIND,
                List.of(CardValue.TEN), List.of(CardValue.ACE));

        int result = comp.compare(fourTensAndOneAce, fourKingsAndOneThree);
        assertTrue(result < 0, "The four kings beat the four tens, the ace does not matter here.");
    }

    /**
     * flushTieCanBeBrokenByFirstCard
     */
    @Test
    void flushTieCanBeBrokenByFirstCard() {
        PokerHand flushWithKing = TestUtils.getPokerHand(PokerHandRanking.FLUSH,
                List.of(CardValue.KING, CardValue.TEN), Collections.emptyList());
        PokerHand flushWithQueen = TestUtils.getPokerHand(PokerHandRanking.FLUSH,
                List.of(CardValue.QUEEN, CardValue.TEN), Collections.emptyList());

        int result = comp.compare(flushWithKing, flushWithQueen);
        assertTrue(result > 0, "The king should rank higher than the queen here.");
    }

    /**
     * flushTieCanBeBrokenByFifthCard
     */
    @Test
    void flushTieCanBeBrokenByFifthCard() {
        PokerHand minorFlush = TestUtils.getPokerHand(PokerHandRanking.FLUSH,
                List.of(CardValue.TWO, CardValue.TEN, CardValue.FIVE, CardValue.SIX, CardValue.TEN),
                Collections.emptyList());

        PokerHand majorFlush = TestUtils.getPokerHand(PokerHandRanking.FLUSH,
                List.of(CardValue.THREE, CardValue.TEN, CardValue.FIVE, CardValue.SIX, CardValue.TEN),
                Collections.emptyList());

        int result = comp.compare(minorFlush, majorFlush);
        assertTrue(result < 0, "Fifth card: Three trumps two.");
    }
}
