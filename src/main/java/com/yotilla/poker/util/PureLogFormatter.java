package com.yotilla.poker.util;

import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

/**
 * Description:
 * a log formatter that prints messages without anything around them
 * <br>
 * Date: 29.12.2020
 *
 * @author Manuel
 *
 */
public class PureLogFormatter extends SimpleFormatter
{

	@Override
	public String getHead(Handler argH)
	{
		return "";
	}

	@Override
	public String getTail(Handler argH)
	{
		return "";
	}

	@Override
	public String format(LogRecord argRecord)
	{
		return argRecord.getMessage();
	}

}
