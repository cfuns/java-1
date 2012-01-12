package de.benjaminborbe.tools.util;

import org.apache.commons.codec.binary.Base64;

public class Base64UtilImpl implements Base64Util {

	@Override
	public String encode(final byte[] binaryData) {
		return Base64.encodeBase64String(binaryData);
	}

	@Override
	public byte[] decode(final String base64String) {
		return Base64.decodeBase64(base64String);
	}

}
