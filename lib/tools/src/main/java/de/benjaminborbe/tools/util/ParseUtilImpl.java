package de.benjaminborbe.tools.util;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.net.URL;

@Singleton
public class ParseUtilImpl implements ParseUtil {

	@Inject
	public ParseUtilImpl() {

	}

	@Override
	public Double parseDouble(final String number) throws ParseException {
		try {
			return Double.parseDouble(number);
		} catch (final Exception e) {
			throw new ParseException(e.getClass().getSimpleName(), e);
		}
	}

	@Override
	public Double parseDouble(final String number, final Double defaultValue) {
		try {
			return Double.parseDouble(number);
		} catch (final Exception e) {
			return defaultValue;
		}
	}

	@Override
	public Float parseFloat(final String number) throws ParseException {
		try {
			return Float.parseFloat(number);
		} catch (final Exception e) {
			throw new ParseException(e.getClass().getSimpleName(), e);
		}
	}

	@Override
	public Float parseFloat(final String number, final Float defaultValue) {
		try {
			return Float.parseFloat(number);
		} catch (final Exception e) {
			return defaultValue;
		}
	}

	@Override
	public Long parseLong(final String number) throws ParseException {
		try {
			if (number.charAt(0) == '+') {
				return Long.parseLong(number.substring(1));
			} else {
				return Long.parseLong(number);
			}
		} catch (final Exception e) {
			throw new ParseException("parseLong of " + number + " failed", e);
		}
	}

	@Override
	public Long parseLong(final String number, final Long defaultValue) {
		try {
			if (number.charAt(0) == '+') {
				return Long.parseLong(number.substring(1));
			} else {
				return Long.parseLong(number);
			}
		} catch (final Exception e) {
			return defaultValue;
		}
	}

	@Override
	public Integer parseInt(final String number) throws ParseException {
		try {
			if (number.charAt(0) == '+') {
				return Integer.parseInt(number.substring(1));
			} else {
				return Integer.parseInt(number);
			}
		} catch (final Exception e) {
			throw new ParseException("parseInt " + number + " failed", e);
		}
	}

	@Override
	public Integer parseInt(final String number, final Integer defaultValue) {
		try {
			if (number.charAt(0) == '+') {
				return Integer.parseInt(number.substring(1));
			} else {
				return Integer.parseInt(number);
			}
		} catch (final Exception e) {
			return defaultValue;
		}
	}

	@Override
	public <T extends Enum<T>> T parseEnum(final Class<T> enumClazz, final String value) throws ParseException {
		try {
			return Enum.valueOf(enumClazz, value);
		} catch (final Exception e) {
			throw new ParseException(e.getClass().getSimpleName(), e);
		}
	}

	@Override
	public <T extends Enum<T>> T parseEnum(final Class<T> enumClazz, final String value, final T defaultValue) {
		try {
			return Enum.valueOf(enumClazz, value);
		} catch (final Exception e) {
			return defaultValue;
		}
	}

	@Override
	public Boolean parseBoolean(final String value) throws ParseException {
		if ("true".equalsIgnoreCase(value) || "1".equalsIgnoreCase(value)) {
			return true;
		}
		if ("false".equalsIgnoreCase(value) || "0".equalsIgnoreCase(value)) {
			return false;
		}
		throw new ParseException("parse " + value + " as boolean failed");
	}

	@Override
	public Boolean parseBoolean(final String value, final Boolean defaultValue) {
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
		} catch (final Exception e) {
			throw new ParseException("parse url '" + value + "' failed", e);
		}
	}

	@Override
	public URL parseURL(final String value, final URL defaultValue) {
		try {
			return new URL(value);
		} catch (final Exception e) {
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
