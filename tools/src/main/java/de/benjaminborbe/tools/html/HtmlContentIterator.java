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
			if (isContentCharacter()) {
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

	private boolean isContentCharacter() {
		if (isLastScriptTag() || isLastStyleTag()) {
			return false;
		}
		return content.getCurrentCharacter() != TAG_OPEN && !isEmptyCharacter();
	}

	private boolean isEmptyCharacter() {
		switch (content.getCurrentCharacter()) {
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
			if (lastTag == null && !isTagCharacter()) {
				lastTag = new HtmlTag(content.substring(openingTag ? tagStartPos + 1 : tagStartPos + 2, content.getCurrentPosition()), openingTag);
				// consumeTagAttributes();
			}
			if (content.getCurrentCharacter() == TAG_CLOSE) {
				return;
			}
			content.next();
		}
	}

	// private void consumeTagAttributes() {
	// int attributeStartPos = -1;
	// int attributeEndPos = -1;
	//
	// boolean equalFound = false;
	//
	// while (content.hasCurrentCharacter()) {
	//
	// // find attr-startpos
	// if (attributeStartPos == -1 && isTagCharacter()) {
	// attributeStartPos = content.getCurrentPosition();
	// }
	//
	// // find attr-endpos
	// if (attributeEndPos == -1 && attributeStartPos != -1 && !isTagCharacter()) {
	// attributeEndPos = content.getCurrentPosition();
	// }
	//
	// if (content.getCurrentCharacter() == '=') {
	// equalFound = true;
	// }
	// if (content.getCurrentCharacter() == '\'') {
	// }
	// if (content.getCurrentCharacter() == '"') {
	// }
	//
	// content.next();
	// }
	// }

	private boolean isLastScriptTag() {
		return lastTag != null && "script".equals(lastTag.getName().toLowerCase()) && lastTag.isOpening();
	}

	private boolean isLastStyleTag() {
		return lastTag != null && "style".equals(lastTag.getName().toLowerCase()) && lastTag.isOpening();
	}

	private boolean isTagCharacter() {
		return Character.isLetterOrDigit(content.getCurrentCharacter());
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
