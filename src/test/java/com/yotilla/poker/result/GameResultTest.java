package com.yotilla.poker.result;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.yotilla.poker.Player;

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

	private GameResult sut;
	private Player playerJaneMock;
	private Player playerJohnMock;
	private Player playerPeteMock;

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
	}

	/**
	 * printWinnerIsNullSafe
	 */
	@Test
	void printWinnerIsNullSafe()
	{
		String empty = "";

		String result = sut.toString();
		assertEquals(empty, result, "empty string expected for no result.");
	}

	/**
	 * printWinner
	 */
	@Test
	void printWinner()
	{
		sut.setWinner(playerJaneMock);

		String result = sut.toString();

		assertEquals(PLAYER_JANE_NAME + " wins.", result, "Jane won. Print it.");
	}

	/**
	 * printSplitPot
	 */
	@Test
	void printSplitPot()
	{
		List<Player> splitPot = Arrays.asList(playerJaneMock, playerJohnMock, playerPeteMock);

		sut.setPotSplit(splitPot);
		String result = sut.toString();

		assertEquals(
				"Players " + PLAYER_JANE_NAME + ", " + PLAYER_JOHN_NAME + ", " + PLAYER_PETE_NAME + " split the pot.",
				result, "Three players split the pot. Print expected.");
	}
}
