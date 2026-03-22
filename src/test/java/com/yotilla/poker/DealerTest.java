package com.yotilla.poker;

import com.yotilla.poker.card.*;
import com.yotilla.poker.error.DeckException;
import com.yotilla.poker.error.HandExceededException;
import com.yotilla.poker.error.PokerParseException;
import com.yotilla.poker.result.GameResult;
import com.yotilla.poker.result.PokerHand;
import com.yotilla.poker.result.PokerHandRanking;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Description:
 *
 * <br>
 * Date: 26.12.2020
 *
 * @author Manuel
 *
 */
class DealerTest {
    private static final String PLAYER_1_NAME = "John Doe";
    private static final String PLAYER_2_NAME = "Jane Doe";
    private static final String PLAYER_3_NAME = "Max Mastermind";

    private Dealer sut;
    private DeckOfCards deckSpy;
    private Player playerOneSpy;
    private Player playerTwoSpy;
    private Player playerThreeSpy;

    @BeforeEach
    void setUp() {
        deckSpy = Mockito.spy(new DeckOfCards());
        sut = new Dealer(deckSpy);

        playerOneSpy = Mockito.spy(new Player(PLAYER_1_NAME));
        playerTwoSpy = Mockito.spy(new Player(PLAYER_2_NAME));
        playerThreeSpy = Mockito.spy(new Player(PLAYER_3_NAME));
    }

    /**
     * dealHandToPlayer
     *
     * @throws PokerParseException   in case of an error
     * @throws HandExceededException in case of too many cards
     * @throws DeckException         if a card was already drawn.
     */
    @Test
    void dealHandToPlayer() throws PokerParseException, HandExceededException, DeckException {
        String input = "2D 3C QH JS AC";

        Card twoOfDiamonds = new Card(CardSuit.DIAMONDS, CardValue.TWO);
        Card threeOfClubs = new Card(CardSuit.CLUBS, CardValue.THREE);
        Card queenOfHearts = new Card(CardSuit.HEARTS, CardValue.QUEEN);
        Card jackOfSpades = new Card(CardSuit.SPADES, CardValue.JACK);
        Card aceOfClubs = new Card(CardSuit.CLUBS, CardValue.ACE);

        sut.parseInputAndDealHand(input, playerOneSpy);
        HandOfCards playerHand = playerOneSpy.getHand();

        assertEquals(twoOfDiamonds, playerHand.getCards().get(0), "Cards must match after parse and deal.");
        assertEquals(threeOfClubs, playerHand.getCards().get(1), "Cards must match after parse and deal.");
        assertEquals(queenOfHearts, playerHand.getCards().get(2), "Cards must match after parse and deal.");
        assertEquals(jackOfSpades, playerHand.getCards().get(3), "Cards must match after parse and deal.");
        assertEquals(aceOfClubs, playerHand.getCards().get(4), "Cards must match after parse and deal.");
    }

    /**
     * dealHandToNullPlayer
     *
     * @throws PokerParseException   error
     * @throws HandExceededException error
     */
    @Test
    void dealHandToNullPlayer() throws PokerParseException, HandExceededException {
        assertThrows(PokerParseException.class, () -> {
            sut.parseInputAndDealHand("2D 3C QH JS AC", null);
        }, "Exception expected on null player");
    }

    /**
     * dealingTheSameCardTwiceThrowsException
     */
    @Test
    void dealingTheSameCardTwiceThrowsException() {
        String input = "2D 2D QH JS AC";
        Player player = new Player(PLAYER_1_NAME);

        assertThrows(DeckException.class, () -> {
            sut.parseInputAndDealHand(input, player);
        }, "Exception expected on dealing the same card twice");
    }

    /**
     * smallHandsAreFilledFromTheDeck
     *
     * @throws PokerParseException   in case of an error
     * @throws HandExceededException in case of too many cards
     * @throws DeckException         if a card was already drawn.
     */
    @Test
    void smallHandsAreFilledFromTheDeck() throws PokerParseException, HandExceededException, DeckException {
        String input = "2D 3C QH";
        Player player = new Player(PLAYER_1_NAME);

        sut.parseInputAndDealHand(input, player);
        HandOfCards playerHand = player.getHand();

        assertEquals(new Card(CardSuit.DIAMONDS, CardValue.TWO), playerHand.getCards().get(0), "Cards must match after parse and deal.");
        assertEquals(new Card(CardSuit.CLUBS, CardValue.THREE), playerHand.getCards().get(1), "Cards must match after parse and deal.");
        assertEquals(new Card(CardSuit.HEARTS, CardValue.QUEEN), playerHand.getCards().get(2), "Cards must match after parse and deal.");
        assertNotNull(playerHand.getCards().get(3), "Missing cards are filled from deck.");
        assertNotNull(playerHand.getCards().get(4), "Missing cards are filled from deck.");

        Mockito.verify(deckSpy, Mockito.times(2)).drawNextCard();
    }

    /**
     * evaluateHandThrowsExceptionOnNull
     */
    @Test
    void evaluateHandThrowsExceptionOnNull() {
        assertThrows(PokerParseException.class, () -> {
            sut.evaluatePlayerHand(null);
        }, "Exception expected on dealing with a null player");

        Mockito.when(playerOneSpy.getHand()).thenReturn(null);

        assertThrows(PokerParseException.class, () -> {
            sut.evaluatePlayerHand(playerOneSpy);
        }, "Exception expected on dealing with a player without a hand.");

        HandOfCards emptyHand = Mockito.mock(HandOfCards.class);
        Mockito.when(emptyHand.isEmpty()).thenReturn(true);
        Mockito.when(emptyHand.getCards()).thenReturn(Collections.emptyList());
        Mockito.when(playerOneSpy.getHand()).thenReturn(emptyHand);

        assertThrows(PokerParseException.class, () -> {
            sut.evaluatePlayerHand(playerOneSpy);
        }, "Exception expected on dealing with a player without a hand.");
    }

    /**
     * evaluateHandThrowsExceptionWhenNothingIsFound
     *
     * @throws HandExceededException error case
     */
    @Test
    void evaluateHandThrowsExceptionWhenNothingIsFound() throws HandExceededException {
        Mockito.when(playerOneSpy.getPokerHand()).thenReturn(null);

        assertThrows(PokerParseException.class, () -> {
            sut.evaluatePlayerHand(playerOneSpy);
        }, "Exception expected on dealing with a player with only one card");
    }

    /**
     * determineGameResultIsNullSafe
     *
     * @throws PokerParseException error
     */
    @Test
    void determineGameResultIsNullSafe() throws PokerParseException {
        GameResult result = sut.determineGameResult(null);
        assertNull(result, "no result expected.");
    }

    /**
     * determineGameResultWithClearWinner
     *
     * @throws PokerParseException error
     */
    @Test
    void determineGameResultWithClearWinner() throws PokerParseException {
        PokerHand handOne = TestUtils.getPokerHandSpy(PokerHandRanking.ONE_PAIR, Arrays.asList(CardValue.TEN),
                Arrays.asList(CardValue.FIVE, CardValue.THREE, CardValue.TWO));
        Mockito.when(playerOneSpy.getPokerHand()).thenReturn(handOne);

        PokerHand handTwo = TestUtils.getPokerHandSpy(PokerHandRanking.ONE_PAIR, Arrays.asList(CardValue.NINE),
                Arrays.asList(CardValue.JACK, CardValue.FOUR, CardValue.TWO));
        Mockito.when(playerTwoSpy.getPokerHand()).thenReturn(handTwo);

        PokerHand handThree = TestUtils.getPokerHandSpy(PokerHandRanking.FLUSH,
                Arrays.asList(CardValue.JACK, CardValue.QUEEN, CardValue.NINE, CardValue.FOUR, CardValue.THREE),
                Collections.emptyList());
        Mockito.when(playerThreeSpy.getPokerHand()).thenReturn(handThree);

        List<Player> players = Arrays.asList(playerOneSpy, playerThreeSpy, playerTwoSpy);
        GameResult result = sut.determineGameResult(players);

        assertEquals(playerThreeSpy, result.getWinner(), "Player three should have won that game.");
    }

    /**
     * determineGameResultWithWinnerByRankCard
     *
     * @throws PokerParseException error
     */
    @Test
    void determineGameResultWithWinnerByRankCard() throws PokerParseException {
        PokerHand handOne = TestUtils.getPokerHandSpy(PokerHandRanking.ONE_PAIR, Arrays.asList(CardValue.TEN),
                Arrays.asList(CardValue.FIVE, CardValue.THREE, CardValue.TWO));
        Mockito.when(playerOneSpy.getPokerHand()).thenReturn(handOne);

        PokerHand handTwo = TestUtils.getPokerHandSpy(PokerHandRanking.FLUSH,
                Arrays.asList(CardValue.JACK, CardValue.KING, CardValue.NINE, CardValue.FOUR, CardValue.THREE),
                Collections.emptyList());
        Mockito.when(playerTwoSpy.getPokerHand()).thenReturn(handTwo);

        PokerHand handThree = TestUtils.getPokerHandSpy(PokerHandRanking.FLUSH,
                Arrays.asList(CardValue.JACK, CardValue.QUEEN, CardValue.NINE, CardValue.FOUR, CardValue.THREE),
                Collections.emptyList());
        Mockito.when(playerThreeSpy.getPokerHand()).thenReturn(handThree);

        List<Player> players = Arrays.asList(playerOneSpy, playerThreeSpy, playerTwoSpy);
        GameResult result = sut.determineGameResult(players);

        assertEquals(playerTwoSpy, result.getWinner(), "Player two should have won that game.");
    }

    /**
     * determineGameResultWithWinnerByHighCard
     *
     * @throws PokerParseException error
     */
    @Test
    void determineGameResultWithWinnerByHighCard() throws PokerParseException {
        PokerHand handOne = TestUtils.getPokerHandSpy(PokerHandRanking.ONE_PAIR, Arrays.asList(CardValue.TEN),
                Arrays.asList(CardValue.FIVE, CardValue.THREE, CardValue.TWO));
        Mockito.when(playerOneSpy.getPokerHand()).thenReturn(handOne);

        PokerHand handTwo = TestUtils.getPokerHandSpy(PokerHandRanking.TWO_PAIRS,
                Arrays.asList(CardValue.JACK, CardValue.JACK, CardValue.NINE, CardValue.NINE, CardValue.THREE),
                Collections.emptyList());
        Mockito.when(playerTwoSpy.getPokerHand()).thenReturn(handTwo);

        PokerHand handThree = TestUtils.getPokerHandSpy(PokerHandRanking.TWO_PAIRS,
                Arrays.asList(CardValue.JACK, CardValue.JACK, CardValue.NINE, CardValue.NINE, CardValue.TEN),
                Collections.emptyList());
        Mockito.when(playerThreeSpy.getPokerHand()).thenReturn(handThree);

        List<Player> players = Arrays.asList(playerOneSpy, playerThreeSpy, playerTwoSpy);
        GameResult result = sut.determineGameResult(players);

        assertEquals(playerThreeSpy, result.getWinner(), "Player three should have won that game.");
    }

    /**
     * determineGameResultWithSplitPotBetweenTwo
     *
     * @throws PokerParseException error
     */
    @Test
    void determineGameResultWithSplitPotBetweenTwo() throws PokerParseException {
        PokerHand handOne = TestUtils.getPokerHandSpy(PokerHandRanking.ONE_PAIR, Arrays.asList(CardValue.TEN),
                Arrays.asList(CardValue.FIVE, CardValue.THREE, CardValue.TWO));
        Mockito.when(playerOneSpy.getPokerHand()).thenReturn(handOne);

        PokerHand handTwo = TestUtils.getPokerHandSpy(PokerHandRanking.TWO_PAIRS,
                Arrays.asList(CardValue.JACK, CardValue.JACK, CardValue.NINE, CardValue.NINE, CardValue.THREE),
                Collections.emptyList());
        Mockito.when(playerTwoSpy.getPokerHand()).thenReturn(handTwo);

        PokerHand handThree = TestUtils.getPokerHandSpy(PokerHandRanking.TWO_PAIRS,
                Arrays.asList(CardValue.JACK, CardValue.JACK, CardValue.NINE, CardValue.NINE, CardValue.THREE),
                Collections.emptyList());
        Mockito.when(playerThreeSpy.getPokerHand()).thenReturn(handThree);

        List<Player> players = Arrays.asList(playerOneSpy, playerThreeSpy, playerTwoSpy);
        GameResult result = sut.determineGameResult(players);

        assertNull(result.getWinner(), "There is no winner when the pot is split");
        assertTrue(result.getPotSplit().contains(playerTwoSpy), "Pot should have been split between players two and three.");
        assertTrue(result.getPotSplit().contains(playerThreeSpy), "Pot should have been split between players two and three.");
    }

    /**
     * determineGameResultWithSplitPotBetweenThree
     *
     * @throws PokerParseException error
     */
    @Test
    void determineGameResultWithSplitPotBetweenThree() throws PokerParseException {
        PokerHand handOne = TestUtils.getPokerHandSpy(PokerHandRanking.HIGH_CARD,
                Arrays.asList(CardValue.QUEEN, CardValue.TEN, CardValue.NINE, CardValue.EIGHT, CardValue.FIVE),
                Collections.emptyList());
        Mockito.when(playerOneSpy.getPokerHand()).thenReturn(handOne);

        PokerHand handTwo = TestUtils.getPokerHandSpy(PokerHandRanking.HIGH_CARD,
                Arrays.asList(CardValue.QUEEN, CardValue.TEN, CardValue.NINE, CardValue.EIGHT, CardValue.FIVE),
                Collections.emptyList());
        Mockito.when(playerTwoSpy.getPokerHand()).thenReturn(handTwo);

        PokerHand handThree = TestUtils.getPokerHandSpy(PokerHandRanking.HIGH_CARD,
                Arrays.asList(CardValue.QUEEN, CardValue.TEN, CardValue.NINE, CardValue.EIGHT, CardValue.FIVE),
                Collections.emptyList());
        Mockito.when(playerThreeSpy.getPokerHand()).thenReturn(handThree);

        List<Player> players = Arrays.asList(playerOneSpy, playerThreeSpy, playerTwoSpy);
        GameResult result = sut.determineGameResult(players);

        assertNull(result.getWinner(), "There is no winner when the pot is split");
        assertTrue(result.getPotSplit().contains(playerOneSpy), "Pot should have been split between all players.");
        assertTrue(result.getPotSplit().contains(playerTwoSpy), "Pot should have been split between all players.");
        assertTrue(result.getPotSplit().contains(playerThreeSpy), "Pot should have been split between all players.");
    }
}
