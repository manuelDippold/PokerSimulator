package com.yotilla.poker;

import com.yotilla.poker.card.CardSuit;
import com.yotilla.poker.card.CardValue;
import com.yotilla.poker.card.HandOfCards;
import com.yotilla.poker.error.HandExceededException;
import com.yotilla.poker.result.PokerHand;
import com.yotilla.poker.result.PokerHandRanking;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HandEvaluationServiceTest {

    private HandEvaluationService sut;

    @BeforeEach
    void setUp() {
        sut = new HandEvaluationService();
    }

    @Test
    void recognizesRoyalFlush() throws HandExceededException {
        HandOfCards hand = TestUtils.getHandSpy(
                Arrays.asList(CardSuit.HEARTS, CardSuit.HEARTS, CardSuit.HEARTS, CardSuit.HEARTS, CardSuit.HEARTS),
                Arrays.asList(CardValue.TEN, CardValue.JACK, CardValue.QUEEN, CardValue.KING, CardValue.ACE));

        PokerHand result = sut.evaluate(hand);

        assertEquals(PokerHandRanking.ROYAL_FLUSH, result.getRanking(), "Royal flush expected.");
    }

    @Test
    void recognizesFullHouse() throws HandExceededException {
        HandOfCards hand = TestUtils.getHandSpy(
                Arrays.asList(CardSuit.HEARTS, CardSuit.SPADES, CardSuit.SPADES, CardSuit.DIAMONDS, CardSuit.HEARTS),
                Arrays.asList(CardValue.TEN, CardValue.TEN, CardValue.FIVE, CardValue.FIVE, CardValue.FIVE));

        PokerHand result = sut.evaluate(hand);

        assertEquals(PokerHandRanking.FULL_HOUSE, result.getRanking(), "Full house expected.");
        assertEquals(CardValue.FIVE, result.getRankCards().get(0), "First rank card is the triple, five.");
        assertEquals(CardValue.TEN, result.getRankCards().get(1), "Second rank card is the pair, ten.");
    }

    @Test
    void recognizesStraight() throws HandExceededException {
        HandOfCards hand = TestUtils.getHandSpy(
                Arrays.asList(CardSuit.HEARTS, CardSuit.SPADES, CardSuit.SPADES, CardSuit.DIAMONDS, CardSuit.DIAMONDS),
                Arrays.asList(CardValue.EIGHT, CardValue.NINE, CardValue.TEN, CardValue.JACK, CardValue.QUEEN));

        PokerHand result = sut.evaluate(hand);

        assertEquals(PokerHandRanking.STRAIGHT, result.getRanking(), "Straight expected.");
        assertEquals(CardValue.QUEEN, result.getRankCards().get(0), "Queen is the rank card.");
    }

    @Test
    void recognizesHighCard() throws HandExceededException {
        HandOfCards hand = TestUtils.getHandSpy(
                Arrays.asList(CardSuit.HEARTS, CardSuit.SPADES, CardSuit.SPADES, CardSuit.DIAMONDS, CardSuit.DIAMONDS),
                Arrays.asList(CardValue.TWO, CardValue.FOUR, CardValue.TEN, CardValue.FIVE, CardValue.JACK));

        PokerHand result = sut.evaluate(hand);

        assertEquals(PokerHandRanking.HIGH_CARD, result.getRanking(), "High card expected.");
        assertEquals(CardValue.JACK, result.getRankCards().get(0), "Rank card 1, jack.");
        assertEquals(CardValue.TEN, result.getRankCards().get(1), "Rank card 2, ten.");
        assertEquals(CardValue.FIVE, result.getRankCards().get(2), "Rank card 3, five.");
        assertEquals(CardValue.FOUR, result.getRankCards().get(3), "Rank card 4, four.");
        assertEquals(CardValue.TWO, result.getRankCards().get(4), "Rank card 5, two.");
    }
}
