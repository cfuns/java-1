package de.benjaminborbe.tools.util;

import java.io.Serializable;

public class Encoding implements Serializable {

	private static final long serialVersionUID = -6757102447895147170L;

	private final String encoding;

	public Encoding(final String encoding) {
		this.encoding = encoding;
	}

	public String getEncoding() {
		return encoding;
	}
}
