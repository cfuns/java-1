package de.benjaminborbe.tools.html;

import de.benjaminborbe.tools.util.StringIterator;

public class HtmlTagParser {

	private static final char TAG_OPEN = '<';

	private static final char TAG_CLOSE = '>';

	public HtmlTag parse(final String content) {
		if (content != null && !content.isEmpty() && content.charAt(0) == TAG_OPEN && content.charAt(content.length() - 1) == TAG_CLOSE) {
			final StringIterator i = new StringIterator(content);
			i.next();
			final boolean open = content.charAt(1) != '/';
			final boolean close = content.charAt(content.length() - 2) == '/' || content.charAt(1) == '/';
			if (!open) {
				i.next();
			}
			final int start = i.getCurrentPosition();
			while (isNameCharacter(i)) {
				i.next();
			}
			final String name = i.substring(start, i.getCurrentPosition());
			final HtmlTag htmlTag = new HtmlTag(name, open, close);
			return htmlTag;
		}
		return null;
	}

	private boolean isNameCharacter(final StringIterator i) {
		final char c = i.getCurrentCharacter();
		return Character.isLetterOrDigit(c);
	}
}
