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
			if (isValidCharacter(c)) {
				if (startPos == -1) {
					startPos = stringIterator.getCurrentPosition();
				}
				stringIterator.next();
			}
			else {
				stringIterator.next();
				if (startPos != -1) {
					return stringIterator.substring(startPos, stringIterator.getCurrentPosition() - 1).toLowerCase();
				}
			}
		}
		if (startPos != -1) {
			return stringIterator.substring(startPos, stringIterator.getCurrentPosition()).toLowerCase();
		}
		else {
			return null;
		}
	}

	private boolean isValidCharacter(final char c) {
		return Character.isLetterOrDigit(c);
		// [^a-z0-9öäüß]
	}

}
