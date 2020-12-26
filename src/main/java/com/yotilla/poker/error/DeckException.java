package com.yotilla.poker.error;

/**
 * Description:
 *
 * <br>
 * Date: 26.12.2020
 *
 * @author Manuel
 *
 */
public class DeckException extends Exception
{
	private static final long serialVersionUID = 6491164068858261591L;

	private final DeckExceptionCause deckExceptionCause;

	/**
	 * @param argCause
	 */
	public DeckException(DeckExceptionCause argDeckExceptionCause)
	{
		super();
		deckExceptionCause = argDeckExceptionCause;
	}

	/**
	 * @return the deckExceptionCause
	 */
	public DeckExceptionCause getDeckExceptionCause()
	{
		return deckExceptionCause;
	}

}
