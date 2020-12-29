package com.yotilla.poker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

	/**
	 * Summon a new dealer with a fresh deck before each test
	 */
	@BeforeEach
	private void setUp()
	{
		deckSpy = Mockito.spy(new DeckOfCards());

		sut = new Dealer(deckSpy);
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

		Player player = new Player(PLAYER_NAME);
		sut.parseInputAndDealHand(input, player);

		HandOfCards playerHand = player.getHand();

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
}
