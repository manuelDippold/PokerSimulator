package com.yotilla.poker;

import org.junit.jupiter.api.BeforeEach;

import com.yotilla.poker.card.DeckOfCards;

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
	// sut: subject under test.
	private Dealer sut;
	private DeckOfCards deck;

	/**
	 * Spin up a fresh deck and dealer before each test
	 */
	@BeforeEach
	private void setUp()
	{
		deck = new DeckOfCards();
		sut = new Dealer();
	}

}
