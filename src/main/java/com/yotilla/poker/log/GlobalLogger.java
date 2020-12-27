package com.yotilla.poker.log;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Description: Simple logging to a file. <br>
 * Date: 27.12.2020
 *
 * @author Manuel
 *
 */
public class GlobalLogger
{
	private static final String LOGFILE_PATH = "log.log";
	private static final int FIVE_MEGABYTE = 5000000;

	public static void setup() throws IOException
	{

		Logger globalLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
		globalLogger.setLevel(Level.INFO);
		globalLogger.getParent().setLevel(Level.INFO);

		// Log to both file and console for now. Might remove one of the two later.
		globalLogger.addHandler(new FileHandler(LOGFILE_PATH, FIVE_MEGABYTE, 5));
		globalLogger.addHandler(new ConsoleHandler());
	}

	/**
	 * private Constructor, empty.
	 */
	private GlobalLogger()
	{

	}
}
