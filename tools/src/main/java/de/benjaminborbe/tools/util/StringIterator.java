package de.benjaminborbe.tools.util;

public class StringIterator {

	private final String content;

	private int pos;

	public StringIterator(final String content) {
		this.content = content;
	}

	public boolean hasCurrentCharacter() {
		return content != null && pos < content.length();
	}

	public char getCurrentCharacter() {
		return content.charAt(pos);
	}

	public boolean hasNextCharacter() {
		return content != null && pos + 1 < content.length();
	}

	public char getNextCharacter() {
		return content.charAt(pos + 1);
	}

	public void next() {
		pos++;
	}

	public int getCurrentPosition() {
		return pos;
	}

	public String substring(final int from, final int to) {
		return content.substring(from, to);
	}

	public String substring(final int from) {
		return content.substring(from);
	}
}
