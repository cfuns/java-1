package de.benjaminborbe.tools.mapper.stringobject;

import java.net.URL;

import de.benjaminborbe.tools.util.ParseUtil;

public class StringObjectMapperUrl<T> extends StringObjectMapperBase<T, URL> {

	private final ParseUtil parseUtil;

	public StringObjectMapperUrl(final String name, final ParseUtil parseUtil) {
		super(name);
		this.parseUtil = parseUtil;
	}

	@Override
	public URL fromString(final String parse) {
		return parseUtil.parseURL(parse, null);
	}

	@Override
	public String toString(final URL parse) {
		return parse != null ? parse.toExternalForm() : null;
	}

}
