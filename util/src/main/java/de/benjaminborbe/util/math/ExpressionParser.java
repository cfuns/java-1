package de.benjaminborbe.util.math;

public class ExpressionParser {

	public ExpressionParser() {
	}

	public double parse(final String expression) throws ExpressionParseException {
		if (expression == null) {
			throw new ExpressionParseException(expression);
		}
		Double value = null;
		final char[] chars = expression.toCharArray();
		int numberStartPos = -1;
		int numberEndPos = -1;
		boolean foundMultiply = false;
		boolean foundDivide = false;
		boolean foundAdd = false;
		boolean foundSubtract = false;
		for (int currentPos = 0; currentPos <= chars.length; currentPos++) {
			if (currentPos == chars.length) {
				numberEndPos = currentPos;
			}
			else {
				final char currentChar = chars[currentPos];
				final boolean currentIsNumber = currentChar == '0' || currentChar == '1' || currentChar == '2' || currentChar == '3' || currentChar == '4' || currentChar == '5'
						|| currentChar == '6' || currentChar == '7' || currentChar == '8' || currentChar == '9';
				if (currentIsNumber && numberStartPos == -1) {
					numberStartPos = currentPos;
				}
				if (!currentIsNumber && numberStartPos != -1) {
					numberEndPos = currentPos;
				}
				if (currentChar == '+') {
					foundAdd = true;
				}
				if (currentChar == '*') {
					foundMultiply = true;
				}
				if (currentChar == '/') {
					foundDivide = true;
				}
				if (currentChar == '-') {
					foundSubtract = true;
				}
			}
			if (numberStartPos != -1 && numberEndPos != -1) {
				final double v = Double.parseDouble(expression.substring(numberStartPos, numberEndPos));
				if (value == null) {
					value = v;
				}
				else {
					if (foundMultiply) {
						value = value * v;
					}
					else if (foundDivide) {
						value = value / v;
					}
					else if (foundAdd) {
						value = value + v;
					}
					else if (foundSubtract) {
						value = value - v;
					}
				}
				numberStartPos = -1;
				numberEndPos = -1;
			}
		}
		if (value != null) {
			return value;
		}
		else {
			throw new ExpressionParseException(expression);
		}
	}
}
