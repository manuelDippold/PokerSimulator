package com.yotilla.poker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.yotilla.poker.card.Card;
import com.yotilla.poker.card.CardSuit;
import com.yotilla.poker.card.CardValue;
import com.yotilla.poker.card.DeckOfCards;
import com.yotilla.poker.card.HandOfCards;
import com.yotilla.poker.error.DeckException;
import com.yotilla.poker.error.HandExceededException;
import com.yotilla.poker.error.PokerParseException;
import com.yotilla.poker.result.GameResult;
import com.yotilla.poker.result.PokerHand;
import com.yotilla.poker.result.PokerHandRanking;

/**
 * Description:
 *
 * <br>
 * Date: 26.12.2020
 *
 * @author Manuel
 *
 */
class DealerTest
{
	private static final String PLAYER_1_NAME = "John Doe";
	private static final String PLAYER_2_NAME = "Jane Doe";
	private static final String PLAYER_3_NAME = "Max Mastermind";

	// sut: subject under test.
	private Dealer sut;
	private DeckOfCards deckSpy;
	private Player playerOneSpy;
	private Player playerTwoSpy;
	private Player playerThreeSpy;

	/**
	 * Summon a new dealer with a fresh deck before each test. <br>
	 * All the Mocks, too.
	 */
	@BeforeEach
	private void setUp()
	{
		deckSpy = Mockito.spy(new DeckOfCards());
		sut = new Dealer(deckSpy);

		playerOneSpy = Mockito.spy(new Player(PLAYER_1_NAME));
		playerTwoSpy = Mockito.spy(new Player(PLAYER_2_NAME));
		playerThreeSpy = Mockito.spy(new Player(PLAYER_3_NAME));
	}

	/**
	 * Get a hand of cards spy with card mocks in it.
	 * suits and values must be of the same size.
	 * suits.get(0) and values.get(0) will make up the first card.
	 *
	 * @param suits  suits for the cards
	 * @param values values for the cards in order with the suits.
	 * @return hand of cards mock returning the cards collection when asked.
	 * @throws HandExceededException error case
	 */
	private HandOfCards getHand(final List<CardSuit> suits, final List<CardValue> values) throws HandExceededException
	{
		HandOfCards hand = Mockito.spy(new HandOfCards());

		for (int i = 0; i < suits.size(); i++)
		{
			Card card = new Card(suits.get(i), values.get(i));
			hand.addCard(card);
		}

		return hand;
	}

	/**
	 * parseHandThrowsExceptionOnNullAndEmpty
	 */
	@Test
	void parseHandThrowsExceptionOnNullAndEmpty()
	{
		assertThrows(PokerParseException.class, () -> {
			sut.parseHandOfCards(null);
		}, "Exception expected on null");

		assertThrows(PokerParseException.class, () -> {
			sut.parseHandOfCards("");
		}, "Exception expected on empty string");
	}

	/**
	 * recognize a single card
	 *
	 * @throws PokerParseException error case
	 */
	@Test
	void recognizeAceOfSpades() throws PokerParseException
	{
		int aceOfSpadesHash = sut.parseCard("AS");
		int aceOfSpadesReferenceHash = new Card(CardSuit.SPADES, CardValue.ACE).hashCode();
		assertEquals(aceOfSpadesReferenceHash, aceOfSpadesHash, "AS should match the ace of spades.");
	}

	/**
	 * parseCardThrowsExceptionOnInvalidString
	 */
	@Test
	void parseCardThrowsExceptionOnInvalidString()
	{
		assertThrows(PokerParseException.class, () -> {
			sut.parseCard(null);
		}, "Exception expected on null");

		assertThrows(PokerParseException.class, () -> {
			sut.parseCard("");
		}, "Exception expected on empty string");

		assertThrows(PokerParseException.class, () -> {
			sut.parseCard("A");
		}, "Exception expected on one character");

		assertThrows(PokerParseException.class, () -> {
			sut.parseCard("AS9");
		}, "Exception expected on three characters");
	}

	/**
	 * parseCardThrowsExceptionOnNonCardString
	 */
	@Test
	void parseCardThrowsExceptionOnNonCardString()
	{
		assertThrows(PokerParseException.class, () -> {
			sut.parseCard("ZZ");
		}, "Exception expected on ZZ");

		assertThrows(PokerParseException.class, () -> {
			sut.parseCard("3Z");
		}, "Exception expected on 3Z");

		assertThrows(PokerParseException.class, () -> {
			sut.parseCard("ZC");
		}, "Exception expected on ZC");
	}

	/**
	 * recognize an exemplary hand
	 *
	 * @throws PokerParseException   in case of an error
	 * @throws HandExceededException in case of too many cards
	 * @throws DeckException         if a card was already drawn.
	 */
	@Test
	void recognizeHand() throws PokerParseException, HandExceededException, DeckException
	{
		// Input hand: two of diamonds, three of clubs, Queen of hearts, Jack of Spades, Ace of Clubs
		String input = "2D 3C QH JS AC";
		HandOfCards hand = sut.parseHandOfCards(input);

		// reference
		Card twoOfDiamonds = new Card(CardSuit.DIAMONDS, CardValue.TWO);
		Card threeOfClubs = new Card(CardSuit.CLUBS, CardValue.THREE);
		Card queenOfHearts = new Card(CardSuit.HEARTS, CardValue.QUEEN);
		Card jackOfSpades = new Card(CardSuit.SPADES, CardValue.JACK);
		Card aceOfClubs = new Card(CardSuit.CLUBS, CardValue.ACE);

		assertEquals(twoOfDiamonds, hand.getCards().get(0), "Cards must match after parse.");
		assertEquals(threeOfClubs, hand.getCards().get(1), "Cards must match after parse.");
		assertEquals(queenOfHearts, hand.getCards().get(2), "Cards must match after parse.");
		assertEquals(jackOfSpades, hand.getCards().get(3), "Cards must match after parse.");
		assertEquals(aceOfClubs, hand.getCards().get(4), "Cards must match after parse.");
	}

	/**
	 * dealHandToPlayer
	 *
	 * @throws PokerParseException   in case of an error
	 * @throws HandExceededException in case of too many cards
	 * @throws DeckException         if a card was already drawn.
	 */
	@Test
	void dealHandToPlayer() throws PokerParseException, HandExceededException, DeckException
	{
		// Input hand: two of diamonds, three of clubs, Queen of hearts, Jack of Spades, Ace of Clubs
		String input = "2D 3C QH JS AC";

		// reference
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
	void dealHandToNullPlayer() throws PokerParseException, HandExceededException
	{
		// Input hand: two of diamonds, three of clubs, Queen of hearts, Jack of Spades, Ace of Clubs
		String input = "2D 3C QH JS AC";

		assertThrows(PokerParseException.class, () -> {
			sut.parseInputAndDealHand(input, null);
		}, "Exception expected on null player");
	}

	/**
	 * dealingTheSameCardTwiceThrowsException
	 */
	@Test
	void dealingTheSameCardTwiceThrowsException()
	{
		// Input hand: two of diamonds, two of diamonds, Queen of hearts, Jack of Spades, Ace of Clubs
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
	void smallHandsAreFilledFromTheDeck() throws PokerParseException, HandExceededException, DeckException
	{
		// Input hand: two of diamonds, three of clubs, Queen of hearts, two cards missing
		String input = "2D 3C QH";
		Player player = new Player(PLAYER_1_NAME);

		// reference
		Card twoOfDiamonds = new Card(CardSuit.DIAMONDS, CardValue.TWO);
		Card threeOfClubs = new Card(CardSuit.CLUBS, CardValue.THREE);
		Card queenOfHearts = new Card(CardSuit.HEARTS, CardValue.QUEEN);

		sut.parseInputAndDealHand(input, player);
		HandOfCards playerHand = player.getHand();

		assertEquals(twoOfDiamonds, playerHand.getCards().get(0), "Cards must match after parse and deal.");
		assertEquals(threeOfClubs, playerHand.getCards().get(1), "Cards must match after parse and deal.");
		assertEquals(queenOfHearts, playerHand.getCards().get(2), "Cards must match after parse and deal.");
		assertNotNull(playerHand.getCards().get(3), "Missing cards are filled from deck.");
		assertNotNull(playerHand.getCards().get(4), "Missing cards are filled from deck.");

		Mockito.verify(deckSpy, Mockito.times(2)).drawNextCard();
	}

	/**
	 * evaluateHandThrowsExceptionOnNull
	 */
	@Test
	void evaluateHandThrowsExceptionOnNull()
	{
		assertThrows(PokerParseException.class, () -> {
			sut.evaluatePlayerHand(null);
		}, "Exception expected on dealing with a null player");

		Mockito.when(playerOneSpy.getHand()).thenReturn(null);

		assertThrows(PokerParseException.class, () -> {
			sut.evaluatePlayerHand(playerOneSpy);
		}, "Exception expected on dealing with a player wihtout a hand.");

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
	void evaluateHandThrowsExceptionWhenNothingIsFound() throws HandExceededException
	{
		// This player stubbornly pretends they don't hold anything.
		Mockito.when(playerOneSpy.getPokerHand()).thenReturn(null);

		assertThrows(PokerParseException.class, () -> {
			sut.evaluatePlayerHand(playerOneSpy);
		}, "Exception expected on dealing with a player with only one card");
	}

	/**
	 * evaluateHandRecognizesRoyalFlush
	 *
	 * @throws HandExceededException error
	 * @throws PokerParseException   error
	 */
	@Test
	void evaluateHandRecognizesRoyalFlush() throws HandExceededException, PokerParseException
	{
		// Royal Flush
		List<CardValue> values = Arrays.asList(CardValue.TEN, CardValue.JACK, CardValue.QUEEN, CardValue.KING,
				CardValue.ACE);
		List<CardSuit> suits = Arrays.asList(CardSuit.HEARTS, CardSuit.HEARTS, CardSuit.HEARTS, CardSuit.HEARTS,
				CardSuit.HEARTS);

		HandOfCards hand = getHand(suits, values);
		Mockito.when(playerOneSpy.getHand()).thenReturn(hand);

		sut.evaluatePlayerHand(playerOneSpy);

		PokerHand result = playerOneSpy.getPokerHand();

		assertEquals(PokerHandRanking.ROYAL_FLUSH, result.getRanking(), "Result royal flush expected");
	}

	/**
	 * evaluateHandRecognizesFullHouse
	 *
	 * @throws HandExceededException error
	 * @throws PokerParseException   error
	 */
	@Test
	void evaluateHandRecognizesFullHouse() throws HandExceededException, PokerParseException
	{
		// Full House
		List<CardValue> values = Arrays.asList(CardValue.TEN, CardValue.TEN, CardValue.FIVE, CardValue.FIVE,
				CardValue.FIVE);
		List<CardSuit> suits = Arrays.asList(CardSuit.HEARTS, CardSuit.SPADES, CardSuit.SPADES, CardSuit.DIAMONDS,
				CardSuit.HEARTS);

		HandOfCards hand = getHand(suits, values);
		Mockito.when(playerOneSpy.getHand()).thenReturn(hand);

		sut.evaluatePlayerHand(playerOneSpy);
		PokerHand result = playerOneSpy.getPokerHand();

		assertEquals(PokerHandRanking.FULL_HOUSE, result.getRanking(), "Result Full house expected");
		assertEquals(CardValue.FIVE, result.getRankCards().get(0), "First Rank card is the triple, five.");
		assertEquals(CardValue.TEN, result.getRankCards().get(1), "First Rank card is the pair, ten.");
	}

	/**
	 * evaluateHandRecognizesStraight
	 *
	 * @throws HandExceededException error
	 * @throws PokerParseException   error
	 */
	@Test
	void evaluateHandRecognizesStraight() throws HandExceededException, PokerParseException
	{
		// Straight
		List<CardValue> values = Arrays.asList(CardValue.EIGHT, CardValue.NINE, CardValue.TEN, CardValue.JACK,
				CardValue.QUEEN);
		List<CardSuit> suits = Arrays.asList(CardSuit.HEARTS, CardSuit.SPADES, CardSuit.SPADES, CardSuit.DIAMONDS,
				CardSuit.DIAMONDS);

		HandOfCards hand = getHand(suits, values);
		Mockito.when(playerOneSpy.getHand()).thenReturn(hand);

		sut.evaluatePlayerHand(playerOneSpy);
		PokerHand result = playerOneSpy.getPokerHand();

		assertEquals(PokerHandRanking.STRAIGHT, result.getRanking(), "Result straight expected");
		assertEquals(CardValue.QUEEN, result.getRankCards().get(0), "Queen is the rank card.");
	}

	/**
	 * evaluateHandRecognizesHighCard
	 *
	 * @throws HandExceededException error
	 * @throws PokerParseException   error
	 */
	@Test
	void evaluateHandRecognizesHighCard() throws HandExceededException, PokerParseException
	{
		// Noting.
		List<CardValue> values = Arrays.asList(CardValue.TWO, CardValue.FOUR, CardValue.TEN, CardValue.FIVE,
				CardValue.JACK);
		List<CardSuit> suits = Arrays.asList(CardSuit.HEARTS, CardSuit.SPADES, CardSuit.SPADES, CardSuit.DIAMONDS,
				CardSuit.DIAMONDS);

		HandOfCards hand = getHand(suits, values);
		Mockito.when(playerOneSpy.getHand()).thenReturn(hand);

		sut.evaluatePlayerHand(playerOneSpy);
		PokerHand result = playerOneSpy.getPokerHand();

		assertEquals(PokerHandRanking.HIGH_CARD, result.getRanking(), "Result high card expected");
		assertEquals(CardValue.JACK, result.getRankCards().get(0), "Rank card 1, jack.");
		assertEquals(CardValue.TEN, result.getRankCards().get(1), "Rank card 2, ten.");
		assertEquals(CardValue.FIVE, result.getRankCards().get(2), "Rank card 3, five.");
		assertEquals(CardValue.FOUR, result.getRankCards().get(3), "Rank card 4, four.");
		assertEquals(CardValue.TWO, result.getRankCards().get(4), "Rank card 5, two.");
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

		Mockito.when(playerOneSpy.getPokerHand()).thenReturn(null);
		result = sut.printPlayerAndHand(-1, null);
		assertEquals(empty, result, "No hand ought to return an empty String.");

		Mockito.when(playerOneSpy.getPokerHand()).thenReturn(null);
		result = sut.printPlayerAndHand(-1, null);
		assertEquals(empty, result, "No hand ought to return an empty String.");

		PokerHand handResult = Mockito.mock(PokerHand.class);
		result = sut.printPlayerAndHand(-1, null);
		Mockito.when(playerOneSpy.getPokerHand()).thenReturn(handResult);

		Mockito.when(playerOneSpy.getPokerHand()).thenReturn(null);
		result = sut.printPlayerAndHand(-1, null);
		assertEquals(empty, result, "Hand with no rank hand ought to return an empty String.");

		PokerHand pokerHandMock = Mockito.mock(PokerHand.class);
		Mockito.when(playerOneSpy.getPokerHand()).thenReturn(pokerHandMock);
		Mockito.when(playerOneSpy.getHand()).thenReturn(null);
		result = sut.printPlayerAndHand(-1, null);
		assertEquals(empty, result, "No Hand of cards ought to return an empty String.");

		HandOfCards handOfCardsMock = Mockito.mock(HandOfCards.class);
		Mockito.when(playerOneSpy.getHand()).thenReturn(handOfCardsMock);
		result = sut.printPlayerAndHand(-1, null);
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
		List<CardValue> values = Arrays.asList(CardValue.EIGHT, CardValue.NINE, CardValue.TEN, CardValue.JACK,
				CardValue.QUEEN);
		List<CardSuit> suits = Arrays.asList(CardSuit.HEARTS, CardSuit.SPADES, CardSuit.SPADES, CardSuit.DIAMONDS,
				CardSuit.DIAMONDS);

		HandOfCards hand = getHand(suits, values);
		Mockito.when(playerOneSpy.getHand()).thenReturn(hand);
		sut.evaluatePlayerHand(playerOneSpy);
		String result = sut.printPlayerAndHand(2, playerOneSpy);

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
		List<CardValue> values = Arrays.asList(CardValue.TEN, CardValue.TEN, CardValue.FIVE, CardValue.FIVE,
				CardValue.FIVE);
		List<CardSuit> suits = Arrays.asList(CardSuit.HEARTS, CardSuit.SPADES, CardSuit.SPADES, CardSuit.DIAMONDS,
				CardSuit.HEARTS);

		HandOfCards hand = getHand(suits, values);
		Mockito.when(playerOneSpy.getHand()).thenReturn(hand);

		sut.evaluatePlayerHand(playerOneSpy);
		String result = sut.printPlayerAndHand(3, playerOneSpy);
		String expected = "3\tJohn Doe\tTH TS 5S 5D 5H\tFULL_HOUSE, FIVE, TEN.";

		assertEquals(expected, result, "Printed result does not match.");
	}

	/**
	 * printPlayerFullHouse
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

		HandOfCards hand = getHand(suits, values);
		Mockito.when(playerOneSpy.getHand()).thenReturn(hand);

		sut.evaluatePlayerHand(playerOneSpy);
		String result = sut.printPlayerAndHand(4, playerOneSpy);
		String expected = "4\tJohn Doe\tTH TS 3S 5D QH\tONE_PAIR, TEN. Kickers: QUEEN, FIVE, THREE.";

		assertEquals(expected, result, "Printed result does not match.");
	}

	/**
	 * determineGameResultIsNullSafe
	 *
	 * @throws PokerParseException error
	 */
	@Test
	void determineGameResultIsNullSafe() throws PokerParseException
	{
		GameResult result = sut.determineGameResult(null);
		assertNull(result, "no result expected.");
	}

	@Test
	void determineGameResultThrowsExceptionOnPlayerWithNoPokerHand()
	{
		// Assume two of the three players only hold a hand. This should result in an exception.
		PokerHand handOne = TestUtils.getPokerHandMock(PokerHandRanking.ONE_PAIR, Arrays.asList(CardValue.TEN),
				Arrays.asList(CardValue.FIVE, CardValue.THREE, CardValue.TWO));
		Mockito.when(playerOneSpy.getPokerHand()).thenReturn(handOne);

		PokerHand handTwo = TestUtils.getPokerHandMock(PokerHandRanking.ONE_PAIR, Arrays.asList(CardValue.NINE),
				Arrays.asList(CardValue.JACK, CardValue.FOUR, CardValue.TWO));
		Mockito.when(playerTwoSpy.getPokerHand()).thenReturn(handTwo);

		List<Player> players = Arrays.asList(playerOneSpy, playerThreeSpy, playerTwoSpy);

		assertThrows(PokerParseException.class, () -> {
			sut.determineGameResult(players);
		}, "One player hand no poker hand- exception expected.");
	}

	/**
	 * determineGameResultWithClearWinner
	 *
	 * @throws PokerParseException
	 */
	@Test
	void determineGameResultWithClearWinner() throws PokerParseException
	{
		PokerHand handOne = TestUtils.getPokerHandMock(PokerHandRanking.ONE_PAIR, Arrays.asList(CardValue.TEN),
				Arrays.asList(CardValue.FIVE, CardValue.THREE, CardValue.TWO));
		Mockito.when(playerOneSpy.getPokerHand()).thenReturn(handOne);

		PokerHand handTwo = TestUtils.getPokerHandMock(PokerHandRanking.ONE_PAIR, Arrays.asList(CardValue.NINE),
				Arrays.asList(CardValue.JACK, CardValue.FOUR, CardValue.TWO));
		Mockito.when(playerTwoSpy.getPokerHand()).thenReturn(handTwo);

		PokerHand handThree = TestUtils.getPokerHandMock(PokerHandRanking.FLUSH,
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
	void determineGameResultWithWinnerByRankCard() throws PokerParseException
	{
		PokerHand handOne = TestUtils.getPokerHandMock(PokerHandRanking.ONE_PAIR, Arrays.asList(CardValue.TEN),
				Arrays.asList(CardValue.FIVE, CardValue.THREE, CardValue.TWO));
		Mockito.when(playerOneSpy.getPokerHand()).thenReturn(handOne);

		PokerHand handTwo = TestUtils.getPokerHandMock(PokerHandRanking.FLUSH,
				Arrays.asList(CardValue.JACK, CardValue.KING, CardValue.NINE, CardValue.FOUR, CardValue.THREE),
				Collections.emptyList());
		Mockito.when(playerTwoSpy.getPokerHand()).thenReturn(handTwo);

		PokerHand handThree = TestUtils.getPokerHandMock(PokerHandRanking.FLUSH,
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
	void determineGameResultWithWinnerByHighCard() throws PokerParseException
	{
		PokerHand handOne = TestUtils.getPokerHandMock(PokerHandRanking.ONE_PAIR, Arrays.asList(CardValue.TEN),
				Arrays.asList(CardValue.FIVE, CardValue.THREE, CardValue.TWO));
		Mockito.when(playerOneSpy.getPokerHand()).thenReturn(handOne);

		PokerHand handTwo = TestUtils.getPokerHandMock(PokerHandRanking.TWO_PAIRS,
				Arrays.asList(CardValue.JACK, CardValue.JACK, CardValue.NINE, CardValue.NINE, CardValue.THREE),
				Collections.emptyList());
		Mockito.when(playerTwoSpy.getPokerHand()).thenReturn(handTwo);

		PokerHand handThree = TestUtils.getPokerHandMock(PokerHandRanking.TWO_PAIRS,
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
	void determineGameResultWithSplitPotBetweenTwo() throws PokerParseException
	{
		PokerHand handOne = TestUtils.getPokerHandMock(PokerHandRanking.ONE_PAIR, Arrays.asList(CardValue.TEN),
				Arrays.asList(CardValue.FIVE, CardValue.THREE, CardValue.TWO));
		Mockito.when(playerOneSpy.getPokerHand()).thenReturn(handOne);

		PokerHand handTwo = TestUtils.getPokerHandMock(PokerHandRanking.TWO_PAIRS,
				Arrays.asList(CardValue.JACK, CardValue.JACK, CardValue.NINE, CardValue.NINE, CardValue.THREE),
				Collections.emptyList());
		Mockito.when(playerTwoSpy.getPokerHand()).thenReturn(handTwo);

		PokerHand handThree = TestUtils.getPokerHandMock(PokerHandRanking.TWO_PAIRS,
				Arrays.asList(CardValue.JACK, CardValue.JACK, CardValue.NINE, CardValue.NINE, CardValue.THREE),
				Collections.emptyList());
		Mockito.when(playerThreeSpy.getPokerHand()).thenReturn(handThree);

		List<Player> players = Arrays.asList(playerOneSpy, playerThreeSpy, playerTwoSpy);
		GameResult result = sut.determineGameResult(players);

		assertNull(result.getWinner(), "There is no winner when the pot is split");

		assertTrue(result.getPotSplit().contains(playerTwoSpy),
				"Pot should have been split between players two and three.");
		assertTrue(result.getPotSplit().contains(playerThreeSpy),
				"Pot should have been split between players two and three.");
	}

	/**
	 * determineGameResultWithSplitPotBetweenThree
	 *
	 * @throws PokerParseException error
	 */
	@Test
	void determineGameResultWithSplitPotBetweenThree() throws PokerParseException
	{
		PokerHand handOne = TestUtils.getPokerHandMock(PokerHandRanking.HIGH_CARD,
				Arrays.asList(CardValue.QUEEN, CardValue.TEN, CardValue.NINE, CardValue.EIGHT, CardValue.FIVE),
				Collections.emptyList());
		Mockito.when(playerOneSpy.getPokerHand()).thenReturn(handOne);

		PokerHand handTwo = TestUtils.getPokerHandMock(PokerHandRanking.HIGH_CARD,
				Arrays.asList(CardValue.QUEEN, CardValue.TEN, CardValue.NINE, CardValue.EIGHT, CardValue.FIVE),
				Collections.emptyList());
		Mockito.when(playerTwoSpy.getPokerHand()).thenReturn(handTwo);

		PokerHand handThree = TestUtils.getPokerHandMock(PokerHandRanking.HIGH_CARD,
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
