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

		for (final String token : tokens) {
			if (isNumber(token)) {
				return new Number(Double.parseDouble(token));
			}
		}

		throw new FormularParseException(formular);
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
