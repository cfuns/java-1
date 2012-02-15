package de.benjaminborbe.tools.crypt;

import javax.crypto.*;
import javax.crypto.spec.*;

/**
 * This program generates a AES key, retrieves its raw bytes, and
 * then reinstantiates a AES key from the key bytes.
 * The reinstantiated key is used to initialize a AES cipher for
 * encryption and decryption.
 */

public class AES {

	/**
	 * Turns array of bytes into string
	 * 
	 * @param buf
	 *          Array of bytes to convert to hex string
	 * @return Generated hex string
	 */
	public static String asHex(final byte buf[]) {
		final StringBuffer strbuf = new StringBuffer(buf.length * 2);
		int i;

		for (i = 0; i < buf.length; i++) {
			if ((buf[i] & 0xff) < 0x10)
				strbuf.append("0");

			strbuf.append(Long.toString(buf[i] & 0xff, 16));
		}

		return strbuf.toString();
	}

	public static void main(final String[] args) throws Exception {

		final String message = "Hello World";

		// Get the KeyGenerator
		final KeyGenerator kgen = KeyGenerator.getInstance("AES");
		kgen.init(128); // 192 and 256 bits may not be available

		// Generate the secret key specs.
		final SecretKey skey = kgen.generateKey();
		final byte[] raw = skey.getEncoded();

		final SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");

		// Instantiate the cipher

		final Cipher cipher = Cipher.getInstance("AES");

		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

		final byte[] encrypted = cipher.doFinal(message.getBytes());
		System.out.println("encrypted string: " + asHex(encrypted));

		cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		final byte[] original = cipher.doFinal(encrypted);
		final String originalString = new String(original);
		System.out.println("Original string: " + originalString + " " + asHex(original));
	}
}
