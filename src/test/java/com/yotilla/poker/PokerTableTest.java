package com.yotilla.poker;

import com.yotilla.poker.card.DeckOfCards;
import com.yotilla.poker.result.GameResult;
import com.yotilla.poker.result.PokerHandRanking;
import com.yotilla.poker.util.LogPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class PokerTableTest {
    private PokerTable sut;
    private Dealer dealer;
    private LogPrinter logPrinter;

    @BeforeEach
    void setUp() {
        dealer = spy(new Dealer(new DeckOfCards()));
        logPrinter = spy(new LogPrinter(Logger.getLogger("PokerTableTest")));
        sut = new PokerTable(logPrinter, dealer);
    }

    @Test
    void clearWinnerIsCorrectlyIdentified() {
        String royalFlush = "JC AC KC QC TC";
        String straight = "6C 5S 7H 9C 8D";
        String twoPairs = "JH 7D JD AS 7S";
        String fullHouse = "KD KH KS 2H 2C";

        GameResult[] capturedGameResults = new GameResult[1];
        doAnswer(invocation ->
                captureGameResult(invocation, capturedGameResults))
                .when(dealer).determineGameResult(anyList());

        sut.playPoker(new String[]{royalFlush, straight, twoPairs, fullHouse});

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(logPrinter, atLeastOnce()).print(captor.capture());
        assertTrue(captor.getAllValues().stream().anyMatch(s -> s.contains("Player 1 wins.")));

        GameResult result = capturedGameResults[0];
        Player winner = result.getWinner();
        assertEquals("Player 1", winner.getName());
        assertEquals(PokerHandRanking.ROYAL_FLUSH, winner.getPokerHand().ranking());
    }

    private static GameResult captureGameResult(InvocationOnMock invocation, GameResult[] capturedGameResults) throws Throwable {
        GameResult result = (GameResult) invocation.callRealMethod();
        capturedGameResults[0] = result;
        return result;
    }

    @Test
    void tiedHandsResultInSplitPot() {
        String straight = "6C 5S 7H 9C 8D";
        String straight2 = "6H 5C 7D 9H 8S";
        String twoPairs = "JH 7D JD AS 7S";
        String onePair = "JH 7D 2D AS 7S";


        GameResult[] capturedGameResults = new GameResult[1];
        doAnswer(invocation ->
                captureGameResult(invocation, capturedGameResults))
                .when(dealer).determineGameResult(anyList());

        sut.playPoker(new String[]{straight2, straight, twoPairs, onePair});
        fail("implement me");
    }

    @Test
    void nullInputIsHandledGracefully() {
        assertDoesNotThrow(() -> sut.playPoker(null));
        verify(logPrinter).print("No hands have been dealt. Quitting.");
    }
}
