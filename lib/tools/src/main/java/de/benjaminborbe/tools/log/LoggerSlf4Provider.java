package de.benjaminborbe.tools.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Provider;
import javax.inject.Singleton;

@Singleton
public class LoggerSlf4Provider implements Provider<Logger> {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final Logger loggerConsole = new LoggerConsole();

	@Override
	public Logger get() {
		if (isConsoleLog()) {
			return loggerConsole;
		}
		else {
			return logger;
		}
	}

	private boolean isConsoleLog() {
		return "true".equals(System.getProperty("logConsole"));
	}

}
