package com.yotilla.poker.result;

import com.yotilla.poker.Player;
import com.yotilla.poker.TestUtils;
import com.yotilla.poker.card.CardSuit;
import com.yotilla.poker.card.CardValue;
import com.yotilla.poker.card.HandOfCards;
import com.yotilla.poker.error.HandExceededException;
import com.yotilla.poker.error.PokerParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static com.yotilla.poker.card.CardSuit.*;
import static com.yotilla.poker.card.CardValue.*;
import static com.yotilla.poker.result.PokerHandRanking.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Description:
 *
 * <br>
 * Date: 30.12.2020
 *
 * @author Manuel
 *
 */
class GameResultTest {
    private static final String PLAYER_JANE_NAME = "Jane Doe";
    private static final String PLAYER_JOHN_NAME = "John Doe";
    private static final String PLAYER_PETE_NAME = "Peter Pumpkin";
    private static final String PLAYER_OLAF_NAME = "Olaf Original";

    // subject under test
    private GameResult sut;

    private Player playerJaneMock;
    private Player playerJohnMock;
    private Player playerPeteMock;
    private Player playerOlafMock;

    /**
     * Create new players and a dealer for each test.
     */
    @BeforeEach
    void setUp() {
        sut = new GameResult();

        playerJaneMock = Mockito.mock(Player.class);
        Mockito.when(playerJaneMock.getName()).thenReturn(PLAYER_JANE_NAME);

        playerJohnMock = Mockito.mock(Player.class);
        Mockito.when(playerJohnMock.getName()).thenReturn(PLAYER_JOHN_NAME);

        playerPeteMock = Mockito.mock(Player.class);
        Mockito.when(playerPeteMock.getName()).thenReturn(PLAYER_PETE_NAME);

        playerOlafMock = Mockito.mock(Player.class);
        Mockito.when(playerOlafMock.getName()).thenReturn(PLAYER_OLAF_NAME);
    }

    /**
     * printWinnerIsNullSafe
     */
    @Test
    void printWinnerIsNullSafe() {
        String empty = "";

        String result = sut.printFinalResult();
        assertEquals(empty, result, "empty string expected for no result.");
    }

    /**
     * addToRanksIsNullSafe
     */
    @Test
    void addToRanksIsNullSafe() {
        sut.addToRanks(null);

        Mockito.when(playerJaneMock.getPokerHand()).thenReturn(null);
        sut.addToRanks(playerJaneMock);

        assertTrue(sut.getRanking().isEmpty(), "Arrive at this point without exception.");
    }

    /**
     * addFirstPlayerToRanks
     */
    @Test
    void addFirstPlayerToRanks() {
        PokerHand janesHand = TestUtils.getPokerHand(STRAIGHT, Arrays.asList(KING),
                Collections.emptyList());
        Mockito.when(playerJaneMock.getPokerHand()).thenReturn(janesHand);

        sut.addToRanks(playerJaneMock);

        assertEquals(playerJaneMock, sut.getRanking().get(janesHand).get(0), "Jane should be in the ranks now.");

        assertEquals(PLAYER_JANE_NAME, sut.getWinner().getName(), "Playing on her own, Jane should have won");

        String printResult = sut.printFinalResult();
        assertEquals(PLAYER_JANE_NAME + " wins.", printResult, "Jane won. Print it.");
    }

    /**
     * addTwoPlayersWithEqualResultToRanks
     */
    @Test
    void addTwoPlayersWithEqualResultToRanks() {
        PokerHand janesHand = TestUtils.getPokerHand(STRAIGHT, Arrays.asList(KING),
                Collections.emptyList());
        Mockito.when(playerJaneMock.getPokerHand()).thenReturn(janesHand);
        Mockito.when(playerJohnMock.getPokerHand()).thenReturn(janesHand);

        sut.addToRanks(playerJaneMock);
        sut.addToRanks(playerJohnMock);

        assertEquals(playerJaneMock, sut.getRanking().get(janesHand).get(0),
                "Jane should be in the ranks now, equal to John.");
        assertEquals(playerJohnMock, sut.getRanking().get(janesHand).get(1),
                "John should be in the ranks now, equal to Jane.");

        assertNull(sut.getWinner(), "Split pot. No sole winner.");
        assertTrue(sut.getPotSplit().contains(playerJaneMock), "Jane and John split the pot.");
        assertTrue(sut.getPotSplit().contains(playerJohnMock), "Jane and John split the pot.");

        String printResult = sut.printFinalResult();

        assertEquals("Players " + PLAYER_JANE_NAME + ", " + PLAYER_JOHN_NAME + " split the pot.", printResult,
                "Three players split the pot. Print expected.");
    }

    /**
     * addFourPlayers
     */
    @Test
    void addFourPlayers() {
        PokerHand janesHand = TestUtils.getPokerHand(STRAIGHT, Arrays.asList(KING),
                Collections.emptyList());
        Mockito.when(playerJaneMock.getPokerHand()).thenReturn(janesHand);

        PokerHand johnsHand = TestUtils.getPokerHand(FULL_HOUSE,
                Arrays.asList(JACK, NINE), Collections.emptyList());
        Mockito.when(playerJohnMock.getPokerHand()).thenReturn(johnsHand);

        PokerHand petesHand = TestUtils.getPokerHand(FOUR_OF_A_KIND, Arrays.asList(FOUR),
                Arrays.asList(TWO));
        Mockito.when(playerPeteMock.getPokerHand()).thenReturn(petesHand);

        PokerHand olafsHand = TestUtils.getPokerHand(TWO_PAIRS,
                Arrays.asList(SIX, FIVE), Arrays.asList(THREE));
        Mockito.when(playerOlafMock.getPokerHand()).thenReturn(olafsHand);

        sut.addToRanks(playerJaneMock);
        sut.addToRanks(playerJohnMock);
        sut.addToRanks(playerPeteMock);
        sut.addToRanks(playerOlafMock);

        Iterator<List<Player>> resultIterator = sut.getRanking().values().iterator();
        assertEquals(PLAYER_PETE_NAME, resultIterator.next().get(0).getName(), "Pete should win this round.");
        assertEquals(PLAYER_JOHN_NAME, resultIterator.next().get(0).getName(), "John should come out second.");
        assertEquals(PLAYER_JANE_NAME, resultIterator.next().get(0).getName(), "Jane should come out third.");
        assertEquals(PLAYER_OLAF_NAME, resultIterator.next().get(0).getName(), "Olaf should come out fourth.");

        assertEquals(PLAYER_PETE_NAME, sut.getWinner().getName(), "Pete should have won this round.");
    }

    /**
     * printPlayerIsNullSafe
     */
    @Test
    void printPlayerIsNullSafe() {
        String empty = "";

        String result = sut.printPlayerAndHand(-1, null);
        assertEquals(empty, result, "No player ought to return an empty String.");

        Mockito.when(playerJaneMock.getPokerHand()).thenReturn(null);
        result = sut.printPlayerAndHand(-1, playerJaneMock);
        assertEquals(empty, result, "No hand ought to return an empty String.");

        Mockito.when(playerJaneMock.getPokerHand()).thenReturn(null);
        result = sut.printPlayerAndHand(-1, playerJaneMock);
        assertEquals(empty, result, "No hand ought to return an empty String.");

        PokerHand handResult = new PokerHand(null, null, null);
        Mockito.when(playerJaneMock.getPokerHand()).thenReturn(handResult);
        result = sut.printPlayerAndHand(-1, playerJaneMock);
        assertEquals(empty, result, "No cards in their hand ought to return an empty String.");

        Mockito.when(playerJaneMock.getPokerHand()).thenReturn(null);
        result = sut.printPlayerAndHand(-1, playerJaneMock);
        assertEquals(empty, result, "Hand with no rank hand ought to return an empty String.");

        PokerHand pokerHandMock = new PokerHand(null, null, null);
        Mockito.when(playerJaneMock.getPokerHand()).thenReturn(pokerHandMock);
        Mockito.when(playerJaneMock.getHand()).thenReturn(null);
        result = sut.printPlayerAndHand(-1, playerJaneMock);
        assertEquals(empty, result, "No Hand of cards ought to return an empty String.");

        HandOfCards handOfCardsMock = Mockito.mock(HandOfCards.class);
        Mockito.when(playerJaneMock.getHand()).thenReturn(handOfCardsMock);
        result = sut.printPlayerAndHand(-1, playerJaneMock);
        assertEquals(empty, result, "A Hand of cards with no content ought to return an empty String.");
    }

    /**
     * printPlayerHappyPath
     *
     * @throws HandExceededException error
     * @throws PokerParseException   error
     */
    @Test
    void printPlayerHandStraight() throws HandExceededException, PokerParseException {
        // Straight
        List<CardValue> values = Arrays.asList(
                EIGHT, NINE, TEN, JACK, QUEEN);
        List<CardSuit> suits = Arrays.asList(
                HEARTS, SPADES, SPADES, DIAMONDS, DIAMONDS);
        HandOfCards hand = TestUtils.getHandSpy(suits, values);

        Mockito.when(playerJohnMock.getHand()).thenReturn(hand);

        PokerHand pokerHand = TestUtils.getPokerHand(
                STRAIGHT, Arrays.asList(QUEEN), Collections.emptyList());

        Mockito.when(playerJohnMock.getPokerHand()).thenReturn(pokerHand);

        String result = sut.printPlayerAndHand(2, playerJohnMock);

        // Expected result:
        // place <tab> name <tab> hand <tab> rank, rank card.
        // so:
        String expected = "2\tJohn Doe\t8H 9S TS JD QD\tSTRAIGHT, QUEEN.";

        assertEquals(expected, result, "Printed result does not match.");
    }

    /**
     * printPlayerFullHouse
     *
     * @throws HandExceededException error
     * @throws PokerParseException   error
     */
    @Test
    void printPlayerFullHouse() throws HandExceededException, PokerParseException {
        // Full House
        List<CardValue> values = Arrays.asList(
                TEN, TEN, FIVE, FIVE, FIVE);
        List<CardSuit> suits = Arrays.asList(
                HEARTS, SPADES, SPADES, DIAMONDS, HEARTS);

        HandOfCards hand = TestUtils.getHandSpy(suits, values);
        Mockito.when(playerJohnMock.getHand()).thenReturn(hand);

        PokerHand pokerHand = TestUtils.getPokerHand(
                FULL_HOUSE, Arrays.asList(FIVE, TEN), Collections.emptyList());

        Mockito.when(playerJohnMock.getPokerHand()).thenReturn(pokerHand);

        String result = sut.printPlayerAndHand(3, playerJohnMock);
        String expected = "3\tJohn Doe\tTH TS 5S 5D 5H\tFULL_HOUSE, FIVE, TEN.";

        assertEquals(expected, result, "Printed result does not match.");
    }

    /**
     * printPlayerOnePair
     *
     * @throws HandExceededException error
     * @throws PokerParseException   error
     */
    @Test
    void printPlayerOnePair() throws HandExceededException, PokerParseException {
        // One Pair
        List<CardValue> values = Arrays.asList(TEN, TEN, THREE, FIVE,
                QUEEN);
        List<CardSuit> suits = Arrays.asList(HEARTS, SPADES, SPADES, DIAMONDS,
                HEARTS);

        HandOfCards hand = TestUtils.getHandSpy(suits, values);
        Mockito.when(playerJohnMock.getHand()).thenReturn(hand);

        PokerHand pokerHand = TestUtils.getPokerHand(
                ONE_PAIR, Arrays.asList(TEN),
                Arrays.asList(QUEEN, FIVE, THREE));

        Mockito.when(playerJohnMock.getPokerHand()).thenReturn(pokerHand);

        String result = sut.printPlayerAndHand(4, playerJohnMock);
        String expected = "4\tJohn Doe\tTH TS 3S 5D QH\tONE_PAIR, TEN. Kickers: QUEEN, FIVE, THREE.";

        assertEquals(expected, result, "Printed result does not match.");
    }

    /**
     * addTwoPlayersToRanksAndPrint
     *
     * @throws HandExceededException error
     */
    @Test
    void addTwoPlayersToRanksAndPrint() throws HandExceededException {
        // Straight
        List<CardValue> janesValues = Arrays.asList(
                EIGHT, NINE, TEN, JACK, QUEEN);
        List<CardSuit> janesSuits = Arrays.asList(
                HEARTS, SPADES, SPADES, DIAMONDS, DIAMONDS);

        HandOfCards janesCards = TestUtils.getHandSpy(janesSuits, janesValues);
        Mockito.when(playerJaneMock.getHand()).thenReturn(janesCards);

        PokerHand janesHand = TestUtils.getPokerHand(STRAIGHT, Arrays.asList(KING),
                Collections.emptyList());
        Mockito.when(playerJaneMock.getPokerHand()).thenReturn(janesHand);

        // Full House
        List<CardValue> johnsValues = Arrays.asList(
                TEN, TEN, FIVE, FIVE, FIVE);
        List<CardSuit> johnsSuits = Arrays.asList(
                HEARTS, SPADES, SPADES, DIAMONDS, HEARTS);

        HandOfCards johnsCards = TestUtils.getHandSpy(johnsSuits, johnsValues);
        Mockito.when(playerJohnMock.getHand()).thenReturn(johnsCards);

        PokerHand johnsHand = TestUtils.getPokerHand(FULL_HOUSE,
                Arrays.asList(JACK, NINE), Collections.emptyList());
        Mockito.when(playerJohnMock.getPokerHand()).thenReturn(johnsHand);

        sut.addToRanks(playerJaneMock);
        sut.addToRanks(playerJohnMock);

        String expected = "1\tJohn Doe\tTH TS 5S 5D 5H\tFULL_HOUSE, JACK, NINE.\n"
                + "2\tJane Doe\t8H 9S TS JD QD\tSTRAIGHT, KING.\n";

        assertEquals(expected, sut.printRanks(), "printed rank result does not meet expectations.");

    }
}
