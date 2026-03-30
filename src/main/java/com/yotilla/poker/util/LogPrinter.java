package com.yotilla.poker.util;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LogPrinter {
    private final Logger log;

    public LogPrinter(Logger log) {
        this.log = log;
    }

    public Logger getLogger() {
        return log;
    }

    public void print(final String message) {
        log.log(Level.INFO, message);
        log.log(Level.INFO, "\n");
    }
}
