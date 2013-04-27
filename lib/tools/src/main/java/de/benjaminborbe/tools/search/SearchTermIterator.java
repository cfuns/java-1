package de.benjaminborbe.tools.search;

import de.benjaminborbe.tools.iterator.IteratorWithoutExceptionBase;
import de.benjaminborbe.tools.util.StringIterator;

public class SearchTermIterator extends IteratorWithoutExceptionBase<String> {

	private final StringIterator stringIterator;

	public SearchTermIterator(final String content) {
		this.stringIterator = new StringIterator(content);
	}

	@Override
	protected String buildNext() {
		int startPos = -1;
		while (stringIterator.hasCurrentCharacter()) {
			final char c = stringIterator.getCurrentCharacter();
			if (startPos == -1 && isValidBeginnCharacter(c) || startPos != -1 && isValidMiddleCharacter(c)) {
				if (startPos == -1) {
					startPos = stringIterator.getCurrentPosition();
				}
				stringIterator.next();
			} else {
				stringIterator.next();
				if (startPos != -1) {
					return stringIterator.substring(startPos, stringIterator.getCurrentPosition() - 1);
				}
			}
		}
		if (startPos != -1) {
			return stringIterator.substring(startPos, stringIterator.getCurrentPosition());
		} else {
			return null;
		}
	}

	// a-z0-9öäüß
	private boolean isValidBeginnCharacter(final char c) {
		return Character.isLetterOrDigit(c);
	}

	// a-z0-9öäüß'
	private boolean isValidMiddleCharacter(final char c) {
		if (Character.isLetterOrDigit(c)) {
			return true;
		}
		if ('\'' == c) {
			return true;
		}
		return false;
	}

}
