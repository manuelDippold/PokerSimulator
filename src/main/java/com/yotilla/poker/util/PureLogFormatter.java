package com.yotilla.poker.util;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

/**
 * Description:
 * a log formatter that prints messages without anything around them,
 * except for warning and higher.
 * <br>
 * Date: 29.12.2020
 *
 * @author Manuel
 *
 */
public class PureLogFormatter extends SimpleFormatter {

    @Override
    public String getHead(Handler h) {
        if (h.getLevel().intValue() >= Level.WARNING.intValue()) {
            return super.getHead(h);
        }

        return "";
    }

    @Override
    public String getTail(Handler h) {
        if (h.getLevel().intValue() >= Level.WARNING.intValue()) {
            return super.getTail(h);
        }

        return "";
    }

    @Override
    public String format(LogRecord record) {
        return record.getMessage();
    }

}
