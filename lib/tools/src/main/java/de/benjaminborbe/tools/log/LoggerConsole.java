package de.benjaminborbe.tools.log;

import org.slf4j.Logger;
import org.slf4j.Marker;

public class LoggerConsole implements Logger {

	@Override
	public String getName() {
		return null;
	}

	@Override
	public boolean isTraceEnabled() {
		return false;
	}

	@Override
	public void trace(final String msg) {
		System.err.println("TRACE: " + msg);
	}

	@Override
	public void trace(final String format, final Object arg) {
	}

	@Override
	public void trace(final String format, final Object arg1, final Object arg2) {
	}

	@Override
	public void trace(final String format, final Object[] argArray) {
	}

	@Override
	public void trace(final String msg, final Throwable t) {
		System.err.println("TRACE: " + msg);
		t.printStackTrace();
	}

	@Override
	public boolean isTraceEnabled(final Marker marker) {
		return false;
	}

	@Override
	public void trace(final Marker marker, final String msg) {
	}

	@Override
	public void trace(final Marker marker, final String format, final Object arg) {
	}

	@Override
	public void trace(final Marker marker, final String format, final Object arg1, final Object arg2) {
	}

	@Override
	public void trace(final Marker marker, final String format, final Object[] argArray) {
	}

	@Override
	public void trace(final Marker marker, final String msg, final Throwable t) {
	}

	@Override
	public boolean isDebugEnabled() {
		return false;
	}

	@Override
	public void debug(final String msg) {
		System.err.println("DEBUG: " + msg);
	}

	@Override
	public void debug(final String format, final Object arg) {
	}

	@Override
	public void debug(final String format, final Object arg1, final Object arg2) {
	}

	@Override
	public void debug(final String format, final Object[] argArray) {
	}

	@Override
	public void debug(final String msg, final Throwable t) {
		System.err.println("DEBUG: " + msg);
		t.printStackTrace();
	}

	@Override
	public boolean isDebugEnabled(final Marker marker) {
		return false;
	}

	@Override
	public void debug(final Marker marker, final String msg) {
	}

	@Override
	public void debug(final Marker marker, final String format, final Object arg) {
	}

	@Override
	public void debug(final Marker marker, final String format, final Object arg1, final Object arg2) {
	}

	@Override
	public void debug(final Marker marker, final String format, final Object[] argArray) {
	}

	@Override
	public void debug(final Marker marker, final String msg, final Throwable t) {
	}

	@Override
	public boolean isInfoEnabled() {
		return false;
	}

	@Override
	public void info(final String msg) {
		System.err.println("INFO: " + msg);
	}

	@Override
	public void info(final String format, final Object arg) {
	}

	@Override
	public void info(final String format, final Object arg1, final Object arg2) {
	}

	@Override
	public void info(final String format, final Object[] argArray) {
	}

	@Override
	public void info(final String msg, final Throwable t) {
		System.err.println("INFO: " + msg);
		t.printStackTrace();
	}

	@Override
	public boolean isInfoEnabled(final Marker marker) {
		return false;
	}

	@Override
	public void info(final Marker marker, final String msg) {
	}

	@Override
	public void info(final Marker marker, final String format, final Object arg) {
	}

	@Override
	public void info(final Marker marker, final String format, final Object arg1, final Object arg2) {
	}

	@Override
	public void info(final Marker marker, final String format, final Object[] argArray) {
	}

	@Override
	public void info(final Marker marker, final String msg, final Throwable t) {
	}

	@Override
	public boolean isWarnEnabled() {
		return false;
	}

	@Override
	public void warn(final String msg) {
		System.err.println("WARN: " + msg);
	}

	@Override
	public void warn(final String format, final Object arg) {
	}

	@Override
	public void warn(final String format, final Object[] argArray) {
	}

	@Override
	public void warn(final String format, final Object arg1, final Object arg2) {
	}

	@Override
	public void warn(final String msg, final Throwable t) {
		System.err.println("WARN: " + msg);
		t.printStackTrace();
	}

	@Override
	public boolean isWarnEnabled(final Marker marker) {
		return false;
	}

	@Override
	public void warn(final Marker marker, final String msg) {
	}

	@Override
	public void warn(final Marker marker, final String format, final Object arg) {
	}

	@Override
	public void warn(final Marker marker, final String format, final Object arg1, final Object arg2) {
	}

	@Override
	public void warn(final Marker marker, final String format, final Object[] argArray) {
	}

	@Override
	public void warn(final Marker marker, final String msg, final Throwable t) {
	}

	@Override
	public boolean isErrorEnabled() {
		return false;
	}

	@Override
	public void error(final String msg) {
		System.err.println("ERROR: " + msg);
	}

	@Override
	public void error(final String format, final Object arg) {
	}

	@Override
	public void error(final String format, final Object arg1, final Object arg2) {
	}

	@Override
	public void error(final String format, final Object[] argArray) {
	}

	@Override
	public void error(final String msg, final Throwable t) {
		System.err.println("ERROR: " + msg);
		t.printStackTrace();
	}

	@Override
	public boolean isErrorEnabled(final Marker marker) {
		return false;
	}

	@Override
	public void error(final Marker marker, final String msg) {
	}

	@Override
	public void error(final Marker marker, final String format, final Object arg) {
	}

	@Override
	public void error(final Marker marker, final String format, final Object arg1, final Object arg2) {
	}

	@Override
	public void error(final Marker marker, final String format, final Object[] argArray) {
	}

	@Override
	public void error(final Marker marker, final String msg, final Throwable t) {
	}

}
