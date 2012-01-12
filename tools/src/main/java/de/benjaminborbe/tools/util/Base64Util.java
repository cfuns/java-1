package de.benjaminborbe.tools.util;

public interface Base64Util {

	byte[] decode(String content);

	String encode(byte[] binaryData);
}
