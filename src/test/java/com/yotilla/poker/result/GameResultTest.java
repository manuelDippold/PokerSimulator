package com.yotilla.poker.result;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.yotilla.poker.Player;
import com.yotilla.poker.TestUtils;
import com.yotilla.poker.card.CardSuit;
import com.yotilla.poker.card.CardValue;
import com.yotilla.poker.card.HandOfCards;
import com.yotilla.poker.error.HandExceededException;
import com.yotilla.poker.error.PokerParseException;

/**
 * Description:
 *
 * <br>
 * Date: 30.12.2020
 *
 * @author Manuel
 *
 */
class GameResultTest
{
	private static final String PLAYER_JANE_NAME = "Jane Doe";
	private static final String PLAYER_JOHN_NAME = "John Doe";
	private static final String PLAYER_PETE_NAME = "Peter Pumnpkin";
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
	void setUp()
	{
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
	void printWinnerIsNullSafe()
	{
		String empty = "";

		String result = sut.printFinalResult();
		assertEquals(empty, result, "empty string expected for no result.");
	}

	/**
	 * addToRanksIsNullSafe
	 */
	@Test
	void addToRanksIsNullSafe()
	{
		sut.addToRanks(null);

		Mockito.when(playerJaneMock.getPokerHand()).thenReturn(null);
		sut.addToRanks(playerJaneMock);

		assertTrue(sut.getRanking().isEmpty(), "Arrive at this point without exception.");
	}

	/**
	 * addFirstPlayerToRanks
	 */
	@Test
	void addFirstPlayerToRanks()
	{
		PokerHand janesHand = TestUtils.getPokerHandSpy(PokerHandRanking.STRAIGHT, Arrays.asList(CardValue.KING),
				Collections.emptyList());
		Mockito.when(playerJaneMock.getPokerHand()).thenReturn(janesHand);

		sut.addToRanks(playerJaneMock);

		assertEquals(playerJaneMock, sut.getRanking().get(janesHand).get(0), "Jane should be in the ranks now.");

		sut.determineWinners();
		assertEquals(PLAYER_JANE_NAME, sut.getWinner().getName(), "Playing on her own, Jane should have won");

		String printResult = sut.printFinalResult();
		assertEquals(PLAYER_JANE_NAME + " wins.", printResult, "Jane won. Print it.");
	}

	/**
	 * addTwoPlayersWithEqualResultToRanks
	 */
	@Test
	void addTwoPlayersWithEqualResultToRanks()
	{
		PokerHand janesHand = TestUtils.getPokerHandSpy(PokerHandRanking.STRAIGHT, Arrays.asList(CardValue.KING),
				Collections.emptyList());
		Mockito.when(playerJaneMock.getPokerHand()).thenReturn(janesHand);
		Mockito.when(playerJohnMock.getPokerHand()).thenReturn(janesHand);

		sut.addToRanks(playerJaneMock);
		sut.addToRanks(playerJohnMock);

		assertEquals(playerJaneMock, sut.getRanking().get(janesHand).get(0),
				"Jane should be in the ranks now, equal to John.");
		assertEquals(playerJohnMock, sut.getRanking().get(janesHand).get(1),
				"John should be in the ranks now, equal to Jane.");

		sut.determineWinners();
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
	void addFourPlayers()
	{
		PokerHand janesHand = TestUtils.getPokerHandSpy(PokerHandRanking.STRAIGHT, Arrays.asList(CardValue.KING),
				Collections.emptyList());
		Mockito.when(playerJaneMock.getPokerHand()).thenReturn(janesHand);

		PokerHand johnsHand = TestUtils.getPokerHandSpy(PokerHandRanking.FULL_HOUSE,
				Arrays.asList(CardValue.JACK, CardValue.NINE), Collections.emptyList());
		Mockito.when(playerJohnMock.getPokerHand()).thenReturn(johnsHand);

		PokerHand petesHand = TestUtils.getPokerHandSpy(PokerHandRanking.FOUR_OF_A_KIND, Arrays.asList(CardValue.FOUR),
				Arrays.asList(CardValue.TWO));
		Mockito.when(playerPeteMock.getPokerHand()).thenReturn(petesHand);

		PokerHand olafsHand = TestUtils.getPokerHandSpy(PokerHandRanking.TWO_PAIRS,
				Arrays.asList(CardValue.SIX, CardValue.FIVE), Arrays.asList(CardValue.THREE));
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

		sut.determineWinners();
		assertEquals(PLAYER_PETE_NAME, sut.getWinner().getName(), "Pete should have won this round.");
	}

	/**
	 * printPlayerIsNullSafe
	 */
	@Test
	void printPlayerIsNullSafe()
	{
		String empty = "";

		String result = sut.printPlayerAndHand(-1, null);
		assertEquals(empty, result, "No player ought to return an empty String.");

		Mockito.when(playerJaneMock.getPokerHand()).thenReturn(null);
		result = sut.printPlayerAndHand(-1, playerJaneMock);
		assertEquals(empty, result, "No hand ought to return an empty String.");

		Mockito.when(playerJaneMock.getPokerHand()).thenReturn(null);
		result = sut.printPlayerAndHand(-1, playerJaneMock);
		assertEquals(empty, result, "No hand ought to return an empty String.");

		PokerHand handResult = Mockito.mock(PokerHand.class);
		Mockito.when(playerJaneMock.getPokerHand()).thenReturn(handResult);
		result = sut.printPlayerAndHand(-1, playerJaneMock);
		assertEquals(empty, result, "No cards in their hand ought to return an empty String.");

		Mockito.when(playerJaneMock.getPokerHand()).thenReturn(null);
		result = sut.printPlayerAndHand(-1, playerJaneMock);
		assertEquals(empty, result, "Hand with no rank hand ought to return an empty String.");

		PokerHand pokerHandMock = Mockito.mock(PokerHand.class);
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
	void printPlayerHandStraight() throws HandExceededException, PokerParseException
	{
		// Straight
		List<CardValue> values = Arrays.asList(
				CardValue.EIGHT, CardValue.NINE, CardValue.TEN, CardValue.JACK, CardValue.QUEEN);
		List<CardSuit> suits = Arrays.asList(
				CardSuit.HEARTS, CardSuit.SPADES, CardSuit.SPADES, CardSuit.DIAMONDS, CardSuit.DIAMONDS);
		HandOfCards hand = TestUtils.getHandSpy(suits, values);

		Mockito.when(playerJohnMock.getHand()).thenReturn(hand);

		PokerHand pokerHand = TestUtils.getPokerHandSpy(
				PokerHandRanking.STRAIGHT, Arrays.asList(CardValue.QUEEN), Collections.emptyList());

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
	void printPlayerFullHouse() throws HandExceededException, PokerParseException
	{
		// Full House
		List<CardValue> values = Arrays.asList(
				CardValue.TEN, CardValue.TEN, CardValue.FIVE, CardValue.FIVE, CardValue.FIVE);
		List<CardSuit> suits = Arrays.asList(
				CardSuit.HEARTS, CardSuit.SPADES, CardSuit.SPADES, CardSuit.DIAMONDS, CardSuit.HEARTS);

		HandOfCards hand = TestUtils.getHandSpy(suits, values);
		Mockito.when(playerJohnMock.getHand()).thenReturn(hand);

		PokerHand pokerHand = TestUtils.getPokerHandSpy(
				PokerHandRanking.FULL_HOUSE, Arrays.asList(CardValue.FIVE, CardValue.TEN), Collections.emptyList());

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
	void printPlayerOnePair() throws HandExceededException, PokerParseException
	{
		// One Pair
		List<CardValue> values = Arrays.asList(CardValue.TEN, CardValue.TEN, CardValue.THREE, CardValue.FIVE,
				CardValue.QUEEN);
		List<CardSuit> suits = Arrays.asList(CardSuit.HEARTS, CardSuit.SPADES, CardSuit.SPADES, CardSuit.DIAMONDS,
				CardSuit.HEARTS);

		HandOfCards hand = TestUtils.getHandSpy(suits, values);
		Mockito.when(playerJohnMock.getHand()).thenReturn(hand);

		PokerHand pokerHand = TestUtils.getPokerHandSpy(
				PokerHandRanking.ONE_PAIR, Arrays.asList(CardValue.TEN),
				Arrays.asList(CardValue.QUEEN, CardValue.FIVE, CardValue.THREE));

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
	void addTwoPlayersToRanksAndPrint() throws HandExceededException
	{
		// Straight
		List<CardValue> janesValues = Arrays.asList(
				CardValue.EIGHT, CardValue.NINE, CardValue.TEN, CardValue.JACK, CardValue.QUEEN);
		List<CardSuit> janesSuits = Arrays.asList(
				CardSuit.HEARTS, CardSuit.SPADES, CardSuit.SPADES, CardSuit.DIAMONDS, CardSuit.DIAMONDS);

		HandOfCards janesCards = TestUtils.getHandSpy(janesSuits, janesValues);
		Mockito.when(playerJaneMock.getHand()).thenReturn(janesCards);

		PokerHand janesHand = TestUtils.getPokerHandSpy(PokerHandRanking.STRAIGHT, Arrays.asList(CardValue.KING),
				Collections.emptyList());
		Mockito.when(playerJaneMock.getPokerHand()).thenReturn(janesHand);

		// Full House
		List<CardValue> johnsValues = Arrays.asList(
				CardValue.TEN, CardValue.TEN, CardValue.FIVE, CardValue.FIVE, CardValue.FIVE);
		List<CardSuit> johnsSuits = Arrays.asList(
				CardSuit.HEARTS, CardSuit.SPADES, CardSuit.SPADES, CardSuit.DIAMONDS, CardSuit.HEARTS);

		HandOfCards johnsCards = TestUtils.getHandSpy(johnsSuits, johnsValues);
		Mockito.when(playerJohnMock.getHand()).thenReturn(johnsCards);

		PokerHand johnsHand = TestUtils.getPokerHandSpy(PokerHandRanking.FULL_HOUSE,
				Arrays.asList(CardValue.JACK, CardValue.NINE), Collections.emptyList());
		Mockito.when(playerJohnMock.getPokerHand()).thenReturn(johnsHand);

		sut.addToRanks(playerJaneMock);
		sut.addToRanks(playerJohnMock);

		String expected = "1\tJohn Doe\tTH TS 5S 5D 5H\tFULL_HOUSE, JACK, NINE.\n"
				+ "2\tJane Doe\t8H 9S TS JD QD\tSTRAIGHT, KING.\n";

		assertEquals(expected, sut.printRanks(), "printed rank result does not meet expectations.");

	}
}
