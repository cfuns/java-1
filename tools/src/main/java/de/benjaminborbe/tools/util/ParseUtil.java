package de.benjaminborbe.tools.util;

public interface ParseUtil {

	double parseDouble(final String number) throws ParseException;

	double parseDouble(final String number, double defaultValue);

	float parseFloat(final String number) throws ParseException;

	float parseFloat(final String number, float defaultValue);

	long parseLong(final String number) throws ParseException;

	long parseLong(final String number, long defaultValue);

	int parseInt(final String number) throws ParseException;

	int parseInt(final String number, int defaultValue);

	boolean parseBoolean(String value) throws ParseException;

	boolean parseBoolean(String value, boolean defaultValue);

	<T extends Enum<T>> T parseEnum(Class<T> enumClazz, String value) throws ParseException;

	<T extends Enum<T>> T parseEnum(Class<T> enumClazz, String value, T defaultValue);

}
