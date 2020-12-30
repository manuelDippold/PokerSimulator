package com.yotilla.poker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
import com.yotilla.poker.card.TestCardUtils;
import com.yotilla.poker.error.DeckException;
import com.yotilla.poker.error.HandExceededException;
import com.yotilla.poker.error.PokerParseException;
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
	private static final String PLAYER_NAME = "John Doe";

	// sut: subject under test.
	private Dealer sut;
	private DeckOfCards deckSpy;
	private Player playerSpy;

	/**
	 * Summon a new dealer with a fresh deck before each test. <br>
	 * All the Mocks, too.
	 */
	@BeforeEach
	private void setUp()
	{
		deckSpy = Mockito.spy(new DeckOfCards());
		sut = new Dealer(deckSpy);

		playerSpy = Mockito.spy(new Player(PLAYER_NAME));
	}

	/**
	 * Get a hand of cards spy with card mocks in it.
	 * suits and values must be of the same size.
	 * suits.get(0) and values.get(0) will make up the first card.
	 *
	 * @param suits  suits for the cards
	 * @param values values for the cards in order with the suits.
	 * @return hand of cards mock returning the card mocks collection when asked.
	 * @throws HandExceededException error case
	 */
	private HandOfCards getHand(final List<CardSuit> suits, final List<CardValue> values) throws HandExceededException
	{
		HandOfCards hand = Mockito.spy(new HandOfCards());

		for (int i = 0; i < suits.size(); i++)
		{
			Card cardMock = TestCardUtils.getCardMock(suits.get(i), values.get(i));
			hand.addCard(cardMock);
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
		Card aceOfSpades = sut.parseCard("AS");
		Card aceOfSpadesReference = new Card(CardSuit.SPADES, CardValue.ACE);
		assertEquals(aceOfSpadesReference, aceOfSpades, "AS should match the ace of spades.");
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

		sut.parseInputAndDealHand(input, playerSpy);
		HandOfCards playerHand = playerSpy.getHand();

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
		Player player = new Player(PLAYER_NAME);

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
		Player player = new Player(PLAYER_NAME);

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

		Mockito.when(playerSpy.getHand()).thenReturn(null);

		assertThrows(PokerParseException.class, () -> {
			sut.evaluatePlayerHand(playerSpy);
		}, "Exception expected on dealing with a player wihtout a hand.");

		HandOfCards emptyHand = Mockito.mock(HandOfCards.class);
		Mockito.when(emptyHand.isEmpty()).thenReturn(true);
		Mockito.when(emptyHand.getCards()).thenReturn(Collections.emptyList());
		Mockito.when(playerSpy.getHand()).thenReturn(emptyHand);

		assertThrows(PokerParseException.class, () -> {
			sut.evaluatePlayerHand(playerSpy);
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
		Mockito.when(playerSpy.getPokerHand()).thenReturn(null);

		assertThrows(PokerParseException.class, () -> {
			sut.evaluatePlayerHand(playerSpy);
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
		Mockito.when(playerSpy.getHand()).thenReturn(hand);

		sut.evaluatePlayerHand(playerSpy);

		PokerHand result = playerSpy.getPokerHand();

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

	}
}
