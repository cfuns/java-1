package de.benjaminborbe.websearch.core.util;

import de.benjaminborbe.httpdownloader.api.HttpHeader;
import de.benjaminborbe.httpdownloader.tools.HttpHeaderDto;
import de.benjaminborbe.tools.json.JSONArraySimple;
import de.benjaminborbe.tools.json.JSONObjectSimple;
import de.benjaminborbe.tools.json.JSONParseException;
import de.benjaminborbe.tools.json.JSONParserSimple;
import de.benjaminborbe.tools.mapper.MapException;
import de.benjaminborbe.tools.mapper.Mapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MapperHttpHeader implements Mapper<HttpHeader> {

	@Override
	public HttpHeader fromString(final String string) throws MapException {
		try {
			if (string == null) {
				return null;
			}

			final HttpHeaderDto httpHeader = new HttpHeaderDto();

			final JSONParserSimple jsonParserSimple = new JSONParserSimple();
			final Object object = jsonParserSimple.parse(string);
			if (object instanceof JSONObjectSimple) {
				final JSONObjectSimple jsonObjectSimple = (JSONObjectSimple) object;
				for (final String key : jsonObjectSimple.keySet()) {
					final List<String> vs = new ArrayList<String>();
					final Object values = jsonObjectSimple.get(key);
					if (values instanceof JSONArraySimple) {
						final JSONArraySimple jsonArraySimple = (JSONArraySimple) values;
						final Iterator<Object> i = jsonArraySimple.iterator();
						while (i.hasNext()) {
							final Object value = i.next();
							vs.add((String) value);
						}
					}
					httpHeader.setHeader(key, vs);
				}
			}
			return httpHeader;
		} catch (JSONParseException e) {
			throw new MapException(e);
		}
	}

	@Override
	public String toString(final HttpHeader httpHeader) throws MapException {
		if (httpHeader == null) {
			return null;
		}
		final JSONObjectSimple jsonObjectSimple = new JSONObjectSimple();
		for (final String key : httpHeader.getKeys()) {
			if (key != null) {
				final JSONArraySimple jsonArraySimple = new JSONArraySimple();
				for (final String value : httpHeader.getValues(key)) {
					jsonArraySimple.add(value);
				}
				jsonObjectSimple.put(key, jsonArraySimple);
			}
		}
		return jsonObjectSimple.toString();
	}
}
