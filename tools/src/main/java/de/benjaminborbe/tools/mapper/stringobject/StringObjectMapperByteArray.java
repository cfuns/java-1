package de.benjaminborbe.tools.mapper.stringobject;

import de.benjaminborbe.tools.util.Base64Util;

public class StringObjectMapperByteArray<T> extends StringObjectMapperBase<T, byte[]> {

	private final Base64Util base64Util;

	public StringObjectMapperByteArray(final String name, final Base64Util base64Util) {
		super(name);
		this.base64Util = base64Util;
	}

	@Override
	public String toString(final byte[] value) {
		if (value == null) {
			return null;
		}
		return base64Util.encode(value);
	}

	@Override
	public byte[] fromString(final String value) {
		if (value == null) {
			return null;
		}
		return base64Util.decode(value);
	}
}
