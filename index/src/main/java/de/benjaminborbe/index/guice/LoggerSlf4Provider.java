package de.benjaminborbe.index.guice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Provider;
import com.google.inject.Singleton;

@Singleton
public class LoggerSlf4Provider implements Provider<Logger> {

	private final org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());

	public Logger get() {
		return logger;
	}

}
