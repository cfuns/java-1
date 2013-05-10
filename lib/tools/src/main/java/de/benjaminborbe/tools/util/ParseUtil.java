package de.benjaminborbe.tools.util;

import java.net.URL;

public interface ParseUtil {

	Double parseDouble(final String number) throws ParseException;

	Double parseDouble(final String number, Double defaultValue);

	Float parseFloat(final String number) throws ParseException;

	Float parseFloat(final String number, Float defaultValue);

	Long parseLong(final String number) throws ParseException;

	Long parseLong(final String number, Long defaultValue);

	Integer parseInt(final String number) throws ParseException;

	Integer parseInt(final String number, Integer defaultValue);

	Boolean parseBoolean(String value) throws ParseException;

	Boolean parseBoolean(String value, Boolean defaultValue);

	<T extends Enum<T>> T parseEnum(Class<T> enumClazz, String value) throws ParseException;

	<T extends Enum<T>> T parseEnum(Class<T> enumClazz, String value, T defaultValue);

	URL parseURL(String value) throws ParseException;

	URL parseURL(String value, URL defaultValue);

	int indexOf(String content, String search) throws ParseException;

	int indexOf(String content, String search, int startPos) throws ParseException;
}
