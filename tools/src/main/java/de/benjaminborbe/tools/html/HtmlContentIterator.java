package de.benjaminborbe.tools.html;

import java.util.NoSuchElementException;

import org.apache.commons.lang.StringEscapeUtils;

import de.benjaminborbe.api.IteratorBase;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.StringIterator;

public class HtmlContentIterator implements IteratorBase<String, ParseException> {

	private static final char TAG_OPEN = '<';

	private static final char TAG_CLOSE = '>';

	private String nextContent;

	private HtmlTag lastTag;

	private final StringIterator content;

	public HtmlContentIterator(final String htmlContent) {
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

		// until next found or pos == htmlContent.length()
		while (nextContent == null && content.hasCurrentCharacter()) {
			if (isContentCharacter(content.getCurrentCharacter())) {
				if (contentStart == -1) {
					contentStart = content.getCurrentPosition();
				}
			}
			else {
				if (contentStart != -1) {
					nextContent = content.substring(contentStart, content.getCurrentPosition());
					contentStart = -1;
				}
				if (content.getCurrentCharacter() == TAG_OPEN) {
					consumeTag();
				}
			}
			content.next();
		}

		if (contentStart != -1) {
			nextContent = content.substring(contentStart);
		}

		return nextContent != null;
	}

	private boolean isContentCharacter(final char c) {
		if (isLastScriptTag()) {

		}
		return c != TAG_OPEN && !isEmptyCharacter(c);
	}

	private boolean isEmptyCharacter(final char c) {
		switch (c) {
		case ' ':
			return true;
		case '\n':
			return true;
		case '\r':
			return true;
		case '\t':
			return true;
		default:
			return false;
		}
	}

	private void consumeTag() {
		lastTag = null;
		final int tagStartPos = content.getCurrentPosition();
		final boolean openingTag;
		content.next();
		if (!content.hasCurrentCharacter()) {
			return;
		}
		if (content.getCurrentCharacter() == '/') {
			openingTag = false;
			content.next();
		}
		else {
			openingTag = true;
		}

		while (content.hasCurrentCharacter()) {
			if (lastTag == null && !isTagCharacter(content.getCurrentCharacter())) {
				lastTag = new HtmlTag(content.substring(openingTag ? tagStartPos + 1 : tagStartPos + 2, content.getCurrentPosition()));
			}
			if (content.getCurrentCharacter() == TAG_CLOSE) {
				return;
			}
			content.next();
		}
	}

	private boolean isLastScriptTag() {
		return lastTag != null && "script".equals(lastTag.getName().toLowerCase());
	}

	private boolean isTagCharacter(final char c) {
		return Character.isLetterOrDigit(c);
	}

	@Override
	public String next() throws ParseException {
		if (hasNext()) {
			final String result = nextContent;
			nextContent = null;
			return StringEscapeUtils.unescapeHtml(result);
		}
		else {
			throw new NoSuchElementException();
		}
	}
}
