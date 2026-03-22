# PokerSimulator

A Java-based poker hand evaluator and game simulator. Parses poker hands from string input, evaluates them against standard poker rankings, and determines winners with full tiebreaker and split-pot support.

## Build & Test

**Requirements:** Java 26, Maven 3.6+

```bash
# Run tests
mvn test

# Build JAR
mvn clean package

# Run
java -jar target/poker-1.0.jar "2D 9C AS AH AC" "3D 6D 7D TD QD" "2C 5C 9D 8S QH"
```

Tests require `JAVA_HOME` pointing to JDK 26. Surefire runs with `forkCount=0`. JaCoCo enforces a minimum 60% cyclomatic complexity coverage — `mvn verify` will fail if this is not met.

## Project Structure

```
com.yotilla.poker
├── PokerTable              Entry point, orchestrates game flow
├── Dealer                  Deals cards from deck to players; delegates parsing and evaluation
├── CardParser              Stateless: parses card strings into Card objects
├── HandEvaluationService   Owns the evaluator chain; returns the best matching PokerHand
├── Player                  POJO: name, HandOfCards, PokerHand
├── card/
│   ├── Card            Immutable value object; comparison is by value only (suit irrelevant)
│   ├── CardSuit        Enum: C, D, H, S
│   ├── CardValue       Enum: 2–A with numerical values (2–14)
│   ├── DeckOfCards     52-card French deck backed by Deque; supports shuffle and targeted draw
│   └── HandOfCards     Up to 5 cards; getCards() returns a defensive copy
├── result/
│   ├── PokerHand       Evaluation result: ranking + rank cards + kicker cards (immutable)
│   ├── PokerHandRanking Enum: HIGH_CARD through ROYAL_FLUSH (scored 1–10)
│   ├── GameResult      Aggregates player results in a ranked TreeMap; resolves winner(s)
│   └── evaluator/
│       ├── PokerHandEvaluator  Interface; default methods for sorting, copying, kicker extraction
│       ├── MultiplesEvaluator  Abstract base for pair/triple/four-of-a-kind patterns
│       └── [10 concrete evaluators, one per hand type]
├── error/
│   ├── DeckException        With DeckExceptionCause enum (DECK_IS_EMPTY, CARD_ALREADY_DRAWN)
│   ├── HandExceededException
│   └── PokerParseException
└── util/
    ├── NullSafeComparator   Base comparator interface; handles null operands
    └── PureLogFormatter     Console logging without headers/footers below WARNING level
```

## Architecture

**Strategy + Chain of Responsibility:** `HandEvaluationService` holds a list of `PokerHandEvaluator` implementations ordered strongest-to-weakest. It iterates them until one returns a non-null result — that is the player's hand.

**Template Method:** `MultiplesEvaluator` provides shared pattern-detection logic extended by `PairEvaluator`, `TripleEvaluator`, and `FourOfKindEvaluator`.

**Evaluation flow:**
1. Parse string input → `Card` objects, draw from deck
2. Fill incomplete hands with random cards from deck
3. Run evaluator chain top-down; first match wins
4. Populate `GameResult` (sorted `TreeMap`) with all players
5. Winner is the first entry; tied first entries = split pot

## Clean Code Philosophy

Prefer self-documenting code over comments. Variable and method names should express intent clearly enough that inline comments become unnecessary. A comment explaining *what* the code does is a signal to rename or extract — only use comments to explain *why* when the reasoning is non-obvious from the code itself.

Do not add Javadoc to any new methods, test or otherwise. Method names should be expressive enough to make documentation redundant.

## Code Style

- 4-space indentation, K&R braces
- `this.field = field` in constructors when parameter name matches field name
- No `arg` prefix on parameters
- Enum fields are `final`
- `getCards()` returns a defensive copy — do not rely on mutating the returned list
- Logging via `java.util.logging` only; no external logging frameworks
- Log levels: `INFO` for normal output, `WARNING` for invalid input, `SEVERE` for internal errors

## Input Format

Card codes are 2 characters: value (`2–9`, `T`, `J`, `Q`, `K`, `A`) + suit (`C`, `D`, `H`, `S`). Case-insensitive. Cards separated by spaces.

- Each card may appear at most once across all players
- Hands with fewer than 5 cards are filled automatically from the deck
- Empty input results in a fully random hand

## Testing

JUnit 5 + Mockito. Test classes mirror the main package structure under `src/test/java`.

`TestUtils` provides shared helpers: `getCardMock`, `getHandSpy`, `getPokerHandSpy`, and `getRandomCardMock` (uses a shared `Random` instance).

**When creating tests:** Create the test class and empty test methods with descriptive names — never write the test body. Always put `Assertions.fail("implement me!")` as the sole body so the test fails visibly until implemented. The developer fills in the implementation. This avoids AI-generated tests that compile and pass but miss edge cases or assert the wrong thing.
