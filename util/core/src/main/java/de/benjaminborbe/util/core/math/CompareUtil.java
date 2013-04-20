package de.benjaminborbe.util.core.math;

import javax.inject.Inject;

public class CompareUtil {

	@Inject
	public CompareUtil() {
	}

	public boolean isLetter(final char currentChar) {
		return currentChar == 'a' || currentChar == 'b' || currentChar == 'c' || currentChar == 'd' || currentChar == 'e' || currentChar == 'f' || currentChar == 'g'
			|| currentChar == 'h' || currentChar == 'i' || currentChar == 'j' || currentChar == 'k' || currentChar == 'l' || currentChar == 'm' || currentChar == 'n'
			|| currentChar == 'o' || currentChar == 'p' || currentChar == 'q' || currentChar == 'r' || currentChar == 's' || currentChar == 't' || currentChar == 'v'
			|| currentChar == 'w' || currentChar == 'x' || currentChar == 'y' || currentChar == 'z';
	}

	public boolean isDigestOrDot(final char currentChar) {
		return currentChar == '.' || isDigest(currentChar);
	}

	public boolean isDigest(final char currentChar) {
		return currentChar == '0' || currentChar == '1' || currentChar == '2' || currentChar == '3' || currentChar == '4' || currentChar == '5' || currentChar == '6'
			|| currentChar == '7' || currentChar == '8' || currentChar == '9';
	}

	public boolean isOperationOrBracket(final char currentChar) {
		return currentChar == '(' || currentChar == ')' || currentChar == '+' || currentChar == '-' || currentChar == '/' || currentChar == '*';
	}

	public boolean isNumber(final String number) {
		final char[] chars = number.toCharArray();
		if (chars.length == 0) {
			return false;
		}
		for (int i = 0; i < chars.length; ++i) {
			final char currentChar = chars[i];
			if (i == 0) {
				if (!isDigest(currentChar)) {
					return false;
				}
			} else if (i == chars.length - 1) {
				if (!isDigest(currentChar)) {
					return false;
				}
			} else {
				if (!isDigestOrDot(currentChar)) {
					return false;
				}
			}
		}
		return true;
	}
}
