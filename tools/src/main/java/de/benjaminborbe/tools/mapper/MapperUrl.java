package de.benjaminborbe.tools.mapper;

import java.net.URL;

import com.google.inject.Inject;

import de.benjaminborbe.tools.util.ParseUtil;

public class MapperUrl implements Mapper<URL> {

	private final ParseUtil parseUtil;

	@Inject
	public MapperUrl(final ParseUtil parseUtil) {
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
