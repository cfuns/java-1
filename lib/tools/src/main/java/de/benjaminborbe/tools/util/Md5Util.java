package de.benjaminborbe.tools.util;

import javax.inject.Inject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Util {

	@Inject
	public Md5Util() {
	}

	public byte[] getMd5(final byte[] content) throws NoSuchAlgorithmException {
		final MessageDigest md = MessageDigest.getInstance("MD5");
		return md.digest(content);
	}
}
