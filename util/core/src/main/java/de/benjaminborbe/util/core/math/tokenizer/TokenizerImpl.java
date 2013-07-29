package de.benjaminborbe.util.core.math.tokenizer;

import de.benjaminborbe.util.core.math.CompareUtil;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class TokenizerImpl implements Tokenizer {

	private final CompareUtil compareUtil;

	@Inject
	public TokenizerImpl(final CompareUtil compareUtil) {
		this.compareUtil = compareUtil;
	}

	@Override
	public List<String> tokenize(final String formular) {
		return tokenize(formular != null ? formular.toCharArray() : new char[0]);
	}

	public List<String> tokenize(final char[] chars) {
		final List<String> result = new ArrayList<String>();

		int numberStart = -1;
		int numberEnd = -1;
		int wordStart = -1;
		int wordEnd = -1;
		for (int currentPos = 0; currentPos <= chars.length; ++currentPos) {
			if (currentPos == chars.length) {
				if (numberStart != -1) {
					numberEnd = currentPos;
				}
				if (wordStart != -1) {
					wordEnd = currentPos;
				}
			} else {
				final char currentChar = chars[currentPos];
				if (numberStart == -1 && wordStart == -1) {
					if (compareUtil.isDigest(currentChar)) {
						numberStart = currentPos;
					}
					if (compareUtil.isLetter(currentChar)) {
						wordStart = currentPos;
					}
				} else {
					if (numberStart != -1) {
						if (!compareUtil.isDigestOrDot(currentChar)) {
							numberEnd = currentPos;
						}
					}
					if (wordStart != -1) {
						if (!compareUtil.isLetter(currentChar)) {
							wordEnd = currentPos;
						}
					}
				}
			}
			if (wordStart != -1 && wordEnd != -1) {
				result.add(new String(chars, wordStart, wordEnd - wordStart));
				wordStart = -1;
				wordEnd = -1;
			}
			if (numberStart != -1 && numberEnd != -1) {
				result.add(new String(chars, numberStart, numberEnd - numberStart));
				numberStart = -1;
				numberEnd = -1;
			}
			if (currentPos != chars.length) {
				final char currentChar = chars[currentPos];
				if (compareUtil.isOperationOrBracket(currentChar)) {
					result.add(String.valueOf(currentChar));
				}
			}
		}
		return result;
	}
}
