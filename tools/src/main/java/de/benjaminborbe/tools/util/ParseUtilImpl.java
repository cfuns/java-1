package de.benjaminborbe.tools.util;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ParseUtilImpl implements ParseUtil {

	@Inject
	public ParseUtilImpl() {

	}

	public double parseDouble(final String number) throws ParseException {
		try {
			return Double.parseDouble(number);
		}
		catch (final Exception e) {
			throw new ParseException(e.getClass().getSimpleName(), e);
		}
	}

	public double parseDouble(final String number, final double defaultValue) {
		try {
			return Double.parseDouble(number);
		}
		catch (final Exception e) {
			return defaultValue;
		}
	}

	public float parseFloat(final String number) throws ParseException {
		try {
			return Float.parseFloat(number);
		}
		catch (final Exception e) {
			throw new ParseException(e.getClass().getSimpleName(), e);
		}
	}

	public float parseFloat(final String number, final float defaultValue) {
		try {
			return Float.parseFloat(number);
		}
		catch (final Exception e) {
			return defaultValue;
		}
	}

	public long parseLong(final String number) throws ParseException {
		try {
			return Long.parseLong(number);
		}
		catch (final Exception e) {
			throw new ParseException(e.getClass().getSimpleName(), e);
		}
	}

	public long parseLong(final String number, final long defaultValue) {
		try {
			return Long.parseLong(number);
		}
		catch (final Exception e) {
			return defaultValue;
		}
	}

	public int parseInt(final String number) throws ParseException {
		try {
			return Integer.parseInt(number);
		}
		catch (final Exception e) {
			throw new ParseException(e.getClass().getSimpleName(), e);
		}
	}

	public int parseInt(final String number, final int defaultValue) {
		try {
			return Integer.parseInt(number);
		}
		catch (final Exception e) {
			return defaultValue;
		}
	}

	public <T extends Enum<T>> T parseEnum(final Class<T> enumClazz, final String value) throws ParseException {
		try {
			return Enum.valueOf(enumClazz, value);
		}
		catch (final Exception e) {
			throw new ParseException(e.getClass().getSimpleName(), e);
		}
	}

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

}
