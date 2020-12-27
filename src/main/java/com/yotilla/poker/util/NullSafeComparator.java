package com.yotilla.poker.util;

import java.util.Comparator;

/**
 * Description: Handy comparator that
 *
 * <br>
 * Date: 27.12.2020
 *
 * @author Manuel
 *
 */
public interface NullSafeComparator<T> extends Comparator<T>
{
	/**
	 * Compares the two objects null - safe, where null is considered less than
	 * anything else. Two null values are considered equal.<br>
	 * When overriding this method be aware that you will not profit from null
	 * safety.
	 *
	 * If both objects are non - null, calls {@code compareNonNull}
	 */
	@Override
	public default int compare(T oneObject, T otherObject)
	{
		if (oneObject == null && otherObject == null)
		{
			return 0;
		}
		else if (oneObject != null && otherObject == null)
		{
			return 1;
		}
		else if (oneObject == null)
		{
			return -1;
		}

		return compareNonNull(oneObject, otherObject);

	}

	/**
	 * Compare two non - null objects. The null check is already taken care of at
	 * this point.
	 *
	 * @param oneObject   object, safe to be not null.
	 * @param otherObject object, safe to be not null.
	 * @return integer
	 */
	public int compareNonNull(T oneObject, T otherObject);
}
