package com.yotilla.poker;

import com.yotilla.poker.card.DeckOfCards;
import com.yotilla.poker.util.LogPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

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
        fail("implement me!");
    }

    @Test
    void tiedHandsResultInSplitPot() {
        fail("implement me!");
    }

    @Test
    void nullInputIsHandledGracefully() {
        assertDoesNotThrow(() -> sut.playPoker(null));
        verify(logPrinter).print("No hands have been dealt. Quitting.");
    }
}
