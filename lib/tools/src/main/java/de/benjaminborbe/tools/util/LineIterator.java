package de.benjaminborbe.tools.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class LineIterator implements Iterator<String> {

	private final String content;

	private String line;

	private int end = 0;

	public LineIterator(final String content) {
		this.content = content;
	}

	@Override
	public boolean hasNext() {
		if (line != null) {
			return true;
		}
		if (content == null || end == -1) {
			return false;
		}
		final int newEnd = content.indexOf("\n", end);
		if (newEnd == -1) {
			line = content.substring(end);
			end = -1;
			return true;
		} else {
			line = content.substring(end, newEnd);
			end = newEnd + 1;
			return true;
		}
	}

	@Override
	public String next() {
		if (hasNext()) {
			final String result = line;
			line = null;
			return result;
		} else {
			throw new NoSuchElementException();
		}
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}
