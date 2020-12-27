package com.yotilla.poker.result;

/**
 * Description: The possible poker hands, along with a numerical score for
 * comparison. <br>
 * Date: 27.12.2020
 *
 * @author Manuel
 *
 */
public enum PokerHandRanking
{
	HIGH_CARD(1),
	ONE_PAIR(2),
	TWO_PAIRS(3),
	THREE_OF_A_KIND(4),
	STRAIGHT(5),
	FLUSH(6),
	FULL_HOUSE(7),
	FOUR_OF_A_KIND(8),
	STRAIGHT_FLUSH(9),
	ROYAL_FLUSH(10);

	private final int score;

	/**
	 * @param argScore
	 */
	private PokerHandRanking(int argScore)
	{
		score = argScore;
	}

	/**
	 * @return the score
	 */
	public int getScore()
	{
		return score;
	}

}
