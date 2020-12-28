package com.yotilla.poker.resultevaluator;

import org.junit.jupiter.api.BeforeEach;

import com.yotilla.poker.card.DeckOfCards;

/**
 * Description:
 * Abstract test class providing the fresh decks for each evaluator below
 * <br>
 * Date: 28.12.2020
 *
 * @author Manuel
 *
 */
abstract class AbstractEvaluatorTest
{
	protected DeckOfCards deck;

	/**
	 * Spin up a fresh deck and dealer before each test
	 */
	@BeforeEach
	protected void setUp()
	{
		deck = new DeckOfCards();
	}
}
