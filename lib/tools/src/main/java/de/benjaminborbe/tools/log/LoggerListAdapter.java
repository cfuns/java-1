package de.benjaminborbe.tools.log;

import org.slf4j.Logger;
import org.slf4j.Marker;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class LoggerListAdapter implements Logger {

	private final Logger primaryLogger;

	private final List<Logger> loggers = new ArrayList<>();

	@Inject
	public LoggerListAdapter(final Logger primaryLogger, final Logger... loggers) {
		this.primaryLogger = primaryLogger;
		addLogger(primaryLogger);
		addLogger(loggers);
	}

	public void addLogger(final Logger... loggers) {
		for (final Logger logger : loggers) {
			this.loggers.add(logger);
		}
	}

	@Override
	public String getName() {
		return primaryLogger.getName();
	}

	@Override
	public boolean isTraceEnabled() {
		return primaryLogger.isTraceEnabled();
	}

	@Override
	public void trace(final String msg) {
		for (final Logger logger : loggers) {
			logger.trace(msg);
		}
	}

	@Override
	public void trace(final String format, final Object arg) {
		for (final Logger logger : loggers) {
			logger.trace(format, arg);
		}
	}

	@Override
	public void trace(final String format, final Object arg1, final Object arg2) {
		for (final Logger logger : loggers) {
			logger.trace(format, arg1, arg2);
		}
	}

	@Override
	public void trace(final String format, final Object[] argArray) {
		for (final Logger logger : loggers) {
			logger.trace(format, argArray);
		}
	}

	@Override
	public void trace(final String msg, final Throwable t) {
		for (final Logger logger : loggers) {
			logger.trace(msg, t);
		}
	}

	@Override
	public boolean isTraceEnabled(final Marker marker) {
		return primaryLogger.isTraceEnabled(marker);
	}

	@Override
	public void trace(final Marker marker, final String msg) {
		for (final Logger logger : loggers) {
			logger.trace(marker, msg);
		}
	}

	@Override
	public void trace(final Marker marker, final String format, final Object arg) {
		for (final Logger logger : loggers) {
			logger.trace(marker, format, arg);
		}
	}

	@Override
	public void trace(final Marker marker, final String format, final Object arg1, final Object arg2) {
		for (final Logger logger : loggers) {
			logger.trace(marker, format, arg1, arg2);
		}
	}

	@Override
	public void trace(final Marker marker, final String format, final Object[] argArray) {
		for (final Logger logger : loggers) {
			logger.trace(marker, format, argArray);
		}
	}

	@Override
	public void trace(final Marker marker, final String msg, final Throwable t) {
		for (final Logger logger : loggers) {
			logger.trace(marker, msg, t);
		}
	}

	@Override
	public boolean isDebugEnabled() {
		return primaryLogger.isDebugEnabled();
	}

	@Override
	public void debug(final String msg) {
		for (final Logger logger : loggers) {
			logger.debug(msg);
		}
	}

	@Override
	public void debug(final String format, final Object arg) {
		for (final Logger logger : loggers) {
			logger.debug(format, arg);
		}
	}

	@Override
	public void debug(final String format, final Object arg1, final Object arg2) {
		for (final Logger logger : loggers) {
			logger.debug(format, arg1, arg2);
		}
	}

	@Override
	public void debug(final String format, final Object[] argArray) {
		for (final Logger logger : loggers) {
			logger.debug(format, argArray);
		}
	}

	@Override
	public void debug(final String msg, final Throwable t) {
		for (final Logger logger : loggers) {
			logger.debug(msg, t);
		}
	}

	@Override
	public final boolean isDebugEnabled(final Marker marker) {
		return primaryLogger.isDebugEnabled(marker);
	}

	@Override
	public void debug(final Marker marker, final String msg) {
		for (final Logger logger : loggers) {
			logger.debug(marker, msg);
		}
	}

	@Override
	public void debug(final Marker marker, final String format, final Object arg) {
		for (final Logger logger : loggers) {
			logger.debug(marker, format, arg);
		}
	}

	@Override
	public void debug(final Marker marker, final String format, final Object arg1, final Object arg2) {
		for (final Logger logger : loggers) {
			logger.debug(marker, format, arg1, arg2);
		}
	}

	@Override
	public void debug(final Marker marker, final String format, final Object[] argArray) {
		for (final Logger logger : loggers) {
			logger.debug(marker, format, argArray);
		}
	}

	@Override
	public void debug(final Marker marker, final String msg, final Throwable t) {
		for (final Logger logger : loggers) {
			logger.debug(marker, msg, t);
		}
	}

	@Override
	public boolean isInfoEnabled() {
		return primaryLogger.isInfoEnabled();
	}

	@Override
	public void info(final String msg) {
		for (final Logger logger : loggers) {
			logger.info(msg);
		}
	}

	@Override
	public void info(final String format, final Object arg) {
		for (final Logger logger : loggers) {
			logger.info(format, arg);
		}
	}

	@Override
	public void info(final String format, final Object arg1, final Object arg2) {
		for (final Logger logger : loggers) {
			logger.info(format, arg1, arg2);
		}
	}

	@Override
	public void info(final String format, final Object[] argArray) {
		for (final Logger logger : loggers) {
			logger.info(format, argArray);
		}
	}

	@Override
	public void info(final String msg, final Throwable t) {
		for (final Logger logger : loggers) {
			logger.info(msg, t);
		}
	}

	@Override
	public boolean isInfoEnabled(final Marker marker) {
		return primaryLogger.isInfoEnabled(marker);
	}

	@Override
	public void info(final Marker marker, final String msg) {
		for (final Logger logger : loggers) {
			logger.info(marker, msg);
		}
	}

	@Override
	public void info(final Marker marker, final String format, final Object arg) {
		for (final Logger logger : loggers) {
			logger.info(marker, format, arg);
		}
	}

	@Override
	public void info(final Marker marker, final String format, final Object arg1, final Object arg2) {
		for (final Logger logger : loggers) {
			logger.info(marker, format, arg1, arg2);
		}
	}

	@Override
	public void info(final Marker marker, final String format, final Object[] argArray) {
		for (final Logger logger : loggers) {
			logger.info(marker, format, argArray);
		}
	}

	@Override
	public void info(final Marker marker, final String msg, final Throwable t) {
		for (final Logger logger : loggers) {
			logger.info(marker, msg, t);
		}
	}

	@Override
	public boolean isWarnEnabled() {
		return primaryLogger.isWarnEnabled();
	}

	@Override
	public void warn(final String msg) {
		for (final Logger logger : loggers) {
			logger.warn(msg);
		}
	}

	@Override
	public void warn(final String format, final Object arg) {
		for (final Logger logger : loggers) {
			logger.warn(format, arg);
		}
	}

	@Override
	public void warn(final String format, final Object[] argArray) {
		for (final Logger logger : loggers) {
			logger.warn(format, argArray);
		}
	}

	@Override
	public void warn(final String format, final Object arg1, final Object arg2) {
		for (final Logger logger : loggers) {
			logger.warn(format, arg1, arg2);
		}
	}

	@Override
	public void warn(final String msg, final Throwable t) {
		for (final Logger logger : loggers) {
			logger.warn(msg, t);
		}
	}

	@Override
	public boolean isWarnEnabled(final Marker marker) {
		return primaryLogger.isWarnEnabled(marker);
	}

	@Override
	public void warn(final Marker marker, final String msg) {
		for (final Logger logger : loggers) {
			logger.warn(marker, msg);
		}
	}

	@Override
	public void warn(final Marker marker, final String format, final Object arg) {
		for (final Logger logger : loggers) {
			logger.warn(marker, format, arg);
		}
	}

	@Override
	public void warn(final Marker marker, final String format, final Object arg1, final Object arg2) {
		for (final Logger logger : loggers) {
			logger.warn(marker, format, arg1, arg2);
		}
	}

	@Override
	public void warn(final Marker marker, final String format, final Object[] argArray) {
		for (final Logger logger : loggers) {
			logger.warn(marker, format, argArray);
		}
	}

	@Override
	public void warn(final Marker marker, final String msg, final Throwable t) {
		for (final Logger logger : loggers) {
			logger.warn(marker, msg, t);
		}
	}

	@Override
	public boolean isErrorEnabled() {
		return primaryLogger.isErrorEnabled();
	}

	@Override
	public void error(final String msg) {
		for (final Logger logger : loggers) {
			logger.error(msg);
		}
	}

	@Override
	public void error(final String format, final Object arg) {
		for (final Logger logger : loggers) {
			logger.error(format, arg);
		}
	}

	@Override
	public void error(final String format, final Object arg1, final Object arg2) {
		for (final Logger logger : loggers) {
			logger.error(format, arg1, arg2);
		}
	}

	@Override
	public void error(final String format, final Object[] argArray) {
		for (final Logger logger : loggers) {
			logger.error(format, argArray);
		}
	}

	@Override
	public void error(final String msg, final Throwable t) {
		for (final Logger logger : loggers) {
			logger.error(msg, t);
		}
	}

	@Override
	public boolean isErrorEnabled(final Marker marker) {
		return primaryLogger.isErrorEnabled(marker);
	}

	@Override
	public void error(final Marker marker, final String msg) {
		for (final Logger logger : loggers) {
			logger.error(marker, msg);
		}
	}

	@Override
	public void error(final Marker marker, final String format, final Object arg) {
		for (final Logger logger : loggers) {
			logger.error(marker, format, arg);
		}
	}

	@Override
	public void error(final Marker marker, final String format, final Object arg1, final Object arg2) {
		for (final Logger logger : loggers) {
			logger.error(marker, format, arg1, arg2);
		}
	}

	@Override
	public void error(final Marker marker, final String format, final Object[] argArray) {
		for (final Logger logger : loggers) {
			logger.error(marker, format, argArray);
		}
	}

	@Override
	public void error(final Marker marker, final String msg, final Throwable t) {
		for (final Logger logger : loggers) {
			logger.error(marker, msg, t);
		}
	}

}
