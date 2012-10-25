package de.benjaminborbe.tools.lua;

public class LuaContent {

	private final char[] content;

	private int pos = -1;

	public LuaContent(final String content) {
		this(content.toCharArray());
	}

	public int getCurrentPos() {
		return pos;
	}

	public LuaContent(final char[] content) {
		this.content = content;
	}

	public boolean existsCurrent() {
		return pos >= 0 && pos < content.length;
	}

	public boolean hasNext() {
		return pos < content.length - 1;
	}

	public char getCurrentChar() {
		return content[pos];
	}

	public void next() {
		pos++;
	}

	public String substring(final int posStart, final int offset) {
		return new String(content, posStart, offset);
	}
}
