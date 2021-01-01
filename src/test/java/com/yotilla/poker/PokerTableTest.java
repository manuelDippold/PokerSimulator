package com.yotilla.poker;

import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import com.yotilla.poker.error.DeckException;
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
class PokerTableTest
{
	private Dealer dealerMock;
	private Logger loggerMock;

	@BeforeEach
	void setUp()
	{
		dealerMock = Mockito.mock(Dealer.class);
		loggerMock = Mockito.mock(Logger.class);
	}

	/**
	 * pokerGameOrderIsRight
	 *
	 * @throws DeckException         class
	 * @throws HandExceededException class
	 * @throws PokerParseException   class
	 */
	@Test
	void pokerGameOrderIsRight() throws PokerParseException, HandExceededException, DeckException
	{
		String[] hands = new String[] { "2D 9C AS AH AC", "3D 6D 7D TD QD", "2C 5C 7C 8S QH" };

		PokerTable sut = new PokerTable(loggerMock, dealerMock);

		InOrder order = Mockito.inOrder(dealerMock);

		sut.playPoker(hands);

		// three players parsed and evaluated
		order.verify(dealerMock).parseInputAndDealHand(Mockito.anyString(), Mockito.any(Player.class));
		order.verify(dealerMock).evaluatePlayerHand(Mockito.any(Player.class));

		order.verify(dealerMock).parseInputAndDealHand(Mockito.anyString(), Mockito.any(Player.class));
		order.verify(dealerMock).evaluatePlayerHand(Mockito.any(Player.class));

		order.verify(dealerMock).parseInputAndDealHand(Mockito.anyString(), Mockito.any(Player.class));
		order.verify(dealerMock).evaluatePlayerHand(Mockito.any(Player.class));

		// result
		order.verify(dealerMock).determineGameResult(Mockito.anyList());
	}
}
