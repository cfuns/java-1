package de.benjaminborbe.util.math;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.inject.Inject;

public class FormularParserImpl implements FormularParser {

	private final static String BRACKET_OPEN = "(";

	private final static String BRACKET_CLOSE = ")";

	private final Set<String> operations = new HashSet<String>(Arrays.asList("+", "/", "*", "-"));

	private final Set<String> functions = new HashSet<String>(Arrays.asList("add"));

	private final Set<String> constants = new HashSet<String>(Arrays.asList("pi"));

	private final Set<String> brackets = new HashSet<String>(Arrays.asList("(", ")"));

	private final Tokenizer tokenizer;

	private final CompareUtil compareUtil;

	@Inject
	public FormularParserImpl(final Tokenizer tokenizer, final CompareUtil compareUtil) {
		this.tokenizer = tokenizer;
		this.compareUtil = compareUtil;
	}

	@Override
	public HasValue parse(final String formular) throws FormularParseException {
		if (formular == null) {
			throw new FormularParseException(formular);
		}
		final List<String> tokens = tokenizer.tokenize(formular);
		final HasValue result = parse(tokens);
		if (result == null) {
			throw new FormularParseException(formular);
		}
		else {
			return result;
		}
	}

	public HasValue parse(final List<String> tokens) throws FormularParseException {
		return parse(tokens, 0, tokens.size() - 1);
	}

	public HasValue parse(final List<String> tokens, final int posStart, final int posEnd) throws FormularParseException {
		if (posEnd < posStart) {
			return null;
		}
		for (int i = posStart; i <= posEnd; ++i) {
			final String token = tokens.get(i);
			if (isBracketOpen(token)) {
				return parse(tokens, i + 1, posEnd);
			}
			if (isBracketClose(token)) {
				return parseExpression(tokens.subList(posStart, i));
			}
		}
		if (posEnd == posStart) {
			return null;
		}
		return parseExpression(tokens.subList(posStart, posEnd));
	}

	public HasValue parseExpression(final List<String> tokens) {
		// System.err.println("parseExpression: " + StringUtils.join(tokens, " # "));
		for (int i = 0; i <= tokens.size(); ++i) {
			final String token = tokens.get(i);
			if (isNumber(token)) {
				return new Number(token);
			}
		}
		throw null;
	}

	private boolean isNumber(final String token) {
		return compareUtil.isNumber(token);
	}

	private boolean isOperation(final String token) {
		return operations.contains(token);
	}

	private boolean isFunction(final String token) {
		return functions.contains(token);
	}

	private boolean isBracketOpen(final String token) {
		return BRACKET_OPEN.equals(token);
	}

	private boolean isBracketClose(final String token) {
		return BRACKET_CLOSE.equals(token);
	}

	private boolean isConstant(final String token) {
		return constants.contains(token);
	}

}
