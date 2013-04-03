package de.benjaminborbe.tools.html;

import com.google.inject.Inject;
import de.benjaminborbe.tools.util.StringIterator;
import org.slf4j.Logger;

public class HtmlTagParser {

	private static final char TAG_OPEN = '<';

	private static final char TAG_CLOSE = '>';

	private final Logger logger;

	@Inject
	public HtmlTagParser(final Logger logger) {
		this.logger = logger;
	}

	public HtmlTag parse(final String content) {
		if (content != null && !content.isEmpty() && content.charAt(0) == TAG_OPEN && content.charAt(content.length() - 1) == TAG_CLOSE) {
			if (logger.isTraceEnabled())
				logger.trace("parse content: " + content);
			final StringIterator i = new StringIterator(content);
			i.next();
			final boolean open = content.charAt(1) != '/';
			final boolean close = content.charAt(content.length() - 2) == '/' || content.charAt(1) == '/';
			if (!open) {
				i.next();
			}
			final int startName = i.getCurrentPosition();
			while (isNameCharacter(i)) {
				i.next();
			}
			final String name = i.substring(startName, i.getCurrentPosition());
			final HtmlTag htmlTag = new HtmlTag(name, open, close);

			while (i.hasCurrentCharacter()) {
				while (isEmptyCharacter(i)) {
					i.next();
				}
				final int startKey = i.getCurrentPosition();
				while (isNameCharacter(i)) {
					i.next();
				}
				final String key = i.substring(startKey, i.getCurrentPosition());
				while (isEmptyCharacter(i)) {
					i.next();
				}
				if (i.getCurrentCharacter() == '=') {
					i.next();
				}
				while (isEmptyCharacter(i)) {
					i.next();
				}
				final Character valueQuote;
				if (i.getCurrentCharacter() == '\'' || i.getCurrentCharacter() == '"') {
					valueQuote = i.getCurrentCharacter();
					i.next();
				} else {
					valueQuote = null;
				}
				final int valueStart = i.getCurrentPosition();
				while (i.hasCurrentCharacter() && isValueCharacter(i, valueQuote)) {
					i.next();
				}
				final String value = i.substring(valueStart, i.getCurrentPosition());
				htmlTag.addAttribute(key, value);
				i.next();
			}
			return htmlTag;
		}
		return null;
	}

	private boolean isValueCharacter(final StringIterator i, final Character valueQuote) {
		if (valueQuote == null) {
			return isNameCharacter(i);
		} else {
			return i.getCurrentCharacter() != valueQuote;
		}
	}

	private boolean isEmptyCharacter(final StringIterator i) {
		switch (i.getCurrentCharacter()) {
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

	private boolean isNameCharacter(final StringIterator i) {
		final char c = i.getCurrentCharacter();
		return Character.isLetterOrDigit(c);
	}
}
