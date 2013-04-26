package de.benjaminborbe.websearch.core.util;

import de.benjaminborbe.httpdownloader.api.HttpHeader;
import de.benjaminborbe.httpdownloader.api.HttpHeaderDto;
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

			HttpHeaderDto httpHeader = new HttpHeaderDto();

			JSONParserSimple jsonParserSimple = new JSONParserSimple();
			final Object object = jsonParserSimple.parse(string);
			if (object instanceof JSONObjectSimple) {
				JSONObjectSimple jsonObjectSimple = (JSONObjectSimple) object;
				for (String key : jsonObjectSimple.keySet()) {
					List<String> vs = new ArrayList<>();
					Object values = jsonObjectSimple.get(key);
					if (values instanceof JSONArraySimple) {
						JSONArraySimple jsonArraySimple = (JSONArraySimple) values;
						Iterator<Object> i = jsonArraySimple.iterator();
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
		JSONObjectSimple jsonObjectSimple = new JSONObjectSimple();
		for (String key : httpHeader.getKeys()) {
			JSONArraySimple jsonArraySimple = new JSONArraySimple();
			for (String value : httpHeader.getValues(key)) {
				jsonArraySimple.add(value);
			}
			jsonObjectSimple.put(key, jsonArraySimple);
		}
		return jsonObjectSimple.toString();
	}
}
