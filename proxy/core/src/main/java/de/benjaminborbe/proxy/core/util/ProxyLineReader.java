package de.benjaminborbe.proxy.core.util;

import java.io.IOException;
import java.io.InputStream;

public class ProxyLineReader {

	public ProxyLineReader() {
	}

	public String readLine(final InputStream is) throws IOException {
		StringBuffer line = new StringBuffer();
		int i;
		char c = 0x00;
		i = is.read();
		if (i == -1)
			return null;
		while (i > -1 && i != 10 && i != 13) {
			// Convert the int to a char
			c = (char) (i & 0xFF);
			line = line.append(c);
			i = is.read();
		}
		if (i == 13) { // 10 is unix LF, but DOS does 13+10, so read the 10 if we got 13
			i = is.read();
		}

		return line.toString();
	}
}
