package de.benjaminborbe.loggly.util;

import java.util.Map;

public interface LogglyConnector {

	void log(LogLevel logLevel, String message, Map<String, String> data);

	void trace(String message, Map<String, String> data);

	void debug(String message, Map<String, String> data);

	void info(String message, Map<String, String> data);

	void warn(String message, Map<String, String> data);

	void error(String message, Map<String, String> data);

	void fatal(String message, Map<String, String> data);

	void log(LogLevel logLevel, String message);

	void trace(String message);

	void debug(String message);

	void info(String message);

	void warn(String message);

	void error(String message);

	void fatal(String message);

	void close();
}
