package de.benjaminborbe.tools.util;

import java.net.URL;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ParseUtilImpl implements ParseUtil {

	@Inject
	public ParseUtilImpl() {

	}

	@Override
	public double parseDouble(final String number) throws ParseException {
		try {
			return Double.parseDouble(number);
		}
		catch (final Exception e) {
			throw new ParseException(e.getClass().getSimpleName(), e);
		}
	}

	@Override
	public double parseDouble(final String number, final double defaultValue) {
		try {
			return Double.parseDouble(number);
		}
		catch (final Exception e) {
			return defaultValue;
		}
	}

	@Override
	public float parseFloat(final String number) throws ParseException {
		try {
			return Float.parseFloat(number);
		}
		catch (final Exception e) {
			throw new ParseException(e.getClass().getSimpleName(), e);
		}
	}

	@Override
	public float parseFloat(final String number, final float defaultValue) {
		try {
			return Float.parseFloat(number);
		}
		catch (final Exception e) {
			return defaultValue;
		}
	}

	@Override
	public long parseLong(final String number) throws ParseException {
		try {
			if (number.charAt(0) == '+') {
				return Long.parseLong(number.substring(1));
			}
			else {
				return Long.parseLong(number);
			}
		}
		catch (final Exception e) {
			throw new ParseException(e.getClass().getSimpleName(), e);
		}
	}

	@Override
	public long parseLong(final String number, final long defaultValue) {
		try {
			if (number.charAt(0) == '+') {
				return Long.parseLong(number.substring(1));
			}
			else {
				return Long.parseLong(number);
			}
		}
		catch (final Exception e) {
			return defaultValue;
		}
	}

	@Override
	public int parseInt(final String number) throws ParseException {
		try {
			if (number.charAt(0) == '+') {
				return Integer.parseInt(number.substring(1));
			}
			else {
				return Integer.parseInt(number);
			}
		}
		catch (final Exception e) {
			throw new ParseException(e.getClass().getSimpleName(), e);
		}
	}

	@Override
	public int parseInt(final String number, final int defaultValue) {
		try {
			if (number.charAt(0) == '+') {
				return Integer.parseInt(number.substring(1));
			}
			else {
				return Integer.parseInt(number);
			}
		}
		catch (final Exception e) {
			return defaultValue;
		}
	}

	@Override
	public <T extends Enum<T>> T parseEnum(final Class<T> enumClazz, final String value) throws ParseException {
		try {
			return Enum.valueOf(enumClazz, value);
		}
		catch (final Exception e) {
			throw new ParseException(e.getClass().getSimpleName(), e);
		}
	}

	@Override
	public <T extends Enum<T>> T parseEnum(final Class<T> enumClazz, final String value, final T defaultValue) {
		try {
			return Enum.valueOf(enumClazz, value);
		}
		catch (final Exception e) {
			return defaultValue;
		}
	}

	@Override
	public boolean parseBoolean(final String value) throws ParseException {
		if ("true".equalsIgnoreCase(value) || "1".equalsIgnoreCase(value)) {
			return true;
		}
		if ("false".equalsIgnoreCase(value) || "0".equalsIgnoreCase(value)) {
			return false;
		}
		throw new ParseException("parse " + value + " as boolean failed");
	}

	@Override
	public boolean parseBoolean(final String value, final boolean defaultValue) {
		if ("true".equalsIgnoreCase(value) || "1".equalsIgnoreCase(value)) {
			return true;
		}
		if ("false".equalsIgnoreCase(value) || "0".equalsIgnoreCase(value)) {
			return false;
		}
		return defaultValue;
	}

	@Override
	public URL parseURL(final String value) throws ParseException {
		try {
			return new URL(value);
		}
		catch (final Exception e) {
			throw new ParseException(e.getClass().getSimpleName(), e);
		}
	}

	@Override
	public URL parseURL(final String value, final URL defaultValue) {
		try {
			return new URL(value);
		}
		catch (final Exception e) {
			return defaultValue;
		}
	}

	@Override
	public int indexOf(final String content, final String search) throws ParseException {
		return indexOf(content, search, 0);
	}

	@Override
	public int indexOf(final String content, final String search, final int startPos) throws ParseException {
		if (startPos < 0) {
			throw new ParseException("can't search with negativ startPos");
		}
		if (content == null) {
			throw new ParseException("can't search without content");
		}
		if (search == null) {
			throw new ParseException("can't search after null");
		}
		final int result = content.indexOf(search, startPos);
		if (result == -1) {
			throw new ParseException("can't find '" + search + "' in '" + content + "'");
		}
		return result;
	}

}
