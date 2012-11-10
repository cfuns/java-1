package de.benjaminborbe.tools.mapper;

import de.benjaminborbe.tools.util.Base64Util;

public class SingleMapByteArray<T> extends SingleMapBase<T, byte[]> {

	private final Base64Util base64Util;

	public SingleMapByteArray(final String name, final Base64Util base64Util) {
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
