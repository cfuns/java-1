package de.benjaminborbe.tools.html;

import de.benjaminborbe.api.IteratorWithException;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.StringIterator;

import java.util.NoSuchElementException;

public class HtmlTokenIterator implements IteratorWithException<String, ParseException> {

	private static final char TAG_OPEN = '<';

	private static final char TAG_CLOSE = '>';

	private final StringIterator content;

	private String nextContent;

	public HtmlTokenIterator(final String htmlContent) {
		content = new StringIterator(htmlContent);
	}

	@Override
	public boolean hasNext() throws ParseException {
		if (nextContent != null) {
			return true;
		}
		if (!content.hasCurrentCharacter()) {
			return false;
		}

		int contentStart = -1;
		int tagStart = -1;
		// until next found or pos == htmlContent.length()
		while (nextContent == null && content.hasCurrentCharacter()) {
			if (contentStart == -1 && tagStart == -1) {
				if (isOpenTag()) {
					tagStart = content.getCurrentPosition();
				} else if (isValidCharacter()) {
					contentStart = content.getCurrentPosition();
				}
			} else if (contentStart != -1) {
				if (!isValidCharacter() || isOpenTag()) {
					nextContent = content.substring(contentStart, content.getCurrentPosition());
					contentStart = -1;
					if (isOpenTag()) {
						tagStart = content.getCurrentPosition();
						return true;
					}
				}
			} else if (tagStart != -1) {
				if (isCloseTag()) {
					nextContent = content.substring(tagStart, content.getCurrentPosition() + 1);
					tagStart = -1;
				}
			}
			content.next();
		}

		if (contentStart != -1) {
			nextContent = content.substring(contentStart);
		}
		if (tagStart != -1) {
			nextContent = content.substring(tagStart);
		}
		return nextContent != null;
	}

	private boolean isCloseTag() {
		return TAG_CLOSE == content.getCurrentCharacter();
	}

	private boolean isOpenTag() {
		return TAG_OPEN == content.getCurrentCharacter();
	}

	private boolean isValidCharacter() {
		switch (content.getCurrentCharacter()) {
			case ' ':
				return false;
			case '\n':
				return false;
			case '\r':
				return false;
			case '\t':
				return false;
			default:
				return true;
		}
	}

	@Override
	public String next() throws ParseException {
		if (hasNext()) {
			final String result = nextContent;
			nextContent = null;
			return result;
		} else {
			throw new NoSuchElementException();
		}
	}
}
