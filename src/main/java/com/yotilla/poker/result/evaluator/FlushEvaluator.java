package com.yotilla.poker.result.evaluator;

import com.yotilla.poker.card.Card;
import com.yotilla.poker.card.CardSuit;
import com.yotilla.poker.card.HandOfCards;
import com.yotilla.poker.result.PokerHand;
import com.yotilla.poker.result.PokerHandRanking;

import java.util.Collections;

/**
 * Description:
 *
 * <br>
 * Date: 28.12.2020
 *
 * @author Manuel
 *
 */
public class FlushEvaluator implements PokerHandEvaluator {

    @Override
    public PokerHand evaluate(HandOfCards hand) {
        if (hand != null) {
            CardSuit runningSuit = null;
            for (Card card : hand.getCards()) {
                CardSuit thisSuit = card.cardSuit();

                if (runningSuit != null && runningSuit != thisSuit) {
                    // there are differing suits in this hand. This is not a flush.
                    return null;
                }
                runningSuit = thisSuit;
            }
            return new PokerHand(PokerHandRanking.FLUSH, cardsToSortedCardValues(hand.getCards()),
                    Collections.emptyList());
        }
        return null;
    }
}
