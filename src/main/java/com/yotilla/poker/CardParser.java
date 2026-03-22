package com.yotilla.poker;

import com.yotilla.poker.card.Card;
import com.yotilla.poker.card.CardSuit;
import com.yotilla.poker.card.CardValue;
import com.yotilla.poker.error.PokerParseException;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: Parses string input into Card objects.
 * <br>
 * Date: 22.03.2026
 *
 * @author Manuel
 */
public class CardParser {

    /**
     * @param input space-separated card codes, e.g. "2D 9C AS"
     * @return list of parsed cards
     * @throws PokerParseException if input is null, empty, or contains unrecognized card codes
     */
    public List<Card> parseCards(final String input) throws PokerParseException {
        if (input == null || input.isEmpty()) {
            throw new PokerParseException("Could not parse poker hand: empty input.");
        }

        List<Card> cards = new ArrayList<>();

        for (String cardString : input.split(" ")) {
            cards.add(parseCard(cardString));
        }

        return cards;
    }

    /**
     * @param input two-character card code, e.g. "AS" for ace of spades
     * @return parsed card
     * @throws PokerParseException if input is null, wrong length, or unrecognized
     */
    public Card parseCard(final String input) throws PokerParseException {
        if (input == null || input.length() != 2) {
            throw new PokerParseException(String.format(
                    "Parse error: A card must consist of two characters for suit and value. Faulty input: %s", input));
        }

        CardValue value = CardValue.getByCode(input.substring(0, 1));
        CardSuit suit = CardSuit.getByCode(input.substring(1, 2));

        if (suit == null || value == null) {
            throw new PokerParseException(String.format("Parse error: Card not recognized: %s", input));
        }

        return new Card(suit, value);
    }
}
