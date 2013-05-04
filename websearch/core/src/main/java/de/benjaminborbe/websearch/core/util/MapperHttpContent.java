package de.benjaminborbe.websearch.core.util;

import de.benjaminborbe.httpdownloader.api.HttpContent;
import de.benjaminborbe.httpdownloader.tools.HttpContentByteArray;
import de.benjaminborbe.tools.mapper.MapException;
import de.benjaminborbe.tools.mapper.Mapper;
import de.benjaminborbe.tools.mapper.MapperByteArray;

import javax.inject.Inject;

public class MapperHttpContent implements Mapper<HttpContent> {

	private final MapperByteArray mapperByteArray;

	@Inject
	public MapperHttpContent(final MapperByteArray mapperByteArray) {
		this.mapperByteArray = mapperByteArray;
	}

	@Override
	public HttpContent fromString(final String string) throws MapException {
		final byte[] content = mapperByteArray.fromString(string);
		if (content != null) {
			return new HttpContentByteArray(content);
		} else {
			return null;
		}
	}

	@Override
	public String toString(final HttpContent object) throws MapException {
		if (object != null) {
			final byte[] content = object.getContent();
			if (content != null) {
				return mapperByteArray.toString(content);
			}
		}
		return null;
	}
}
