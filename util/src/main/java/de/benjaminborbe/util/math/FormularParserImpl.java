package de.benjaminborbe.util.math;

import java.util.List;
import com.google.inject.Inject;

public class FormularParserImpl implements FormularParser {

	private final static String BRACKET_OPEN = "(";

	private final static String BRACKET_CLOSE = ")";

	private final Tokenizer tokenizer;

	private final CompareUtil compareUtil;

	private final Functions functions;

	private final Operations operations;

	private final Constants constants;

	@Inject
	public FormularParserImpl(final Tokenizer tokenizer, final CompareUtil compareUtil, final Functions functions, final Operations operations, final Constants constants) {
		this.tokenizer = tokenizer;
		this.compareUtil = compareUtil;
		this.functions = functions;
		this.operations = operations;
		this.constants = constants;
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
		HasValue value = null;
		boolean firstElement = true;
		for (int i = posStart; i <= posEnd; ++i) {
			final String token = tokens.get(i);
			if (isNumber(token)) {
				value = new Number(token);
			}
			else if (isConstant(token)) {
				value = getConstant(token);
			}
			else if (isBracketOpen(token)) {
				return parse(tokens, i + 1, posEnd);
			}
			else if (isBracketClose(token) && !firstElement) {
			}
			else if (isFunction(token)) {
			}
			else if (isOperation(token) && value != null) {
				final HasValue valueA = value;
				final HasValue valueB = parse(tokens, i + 1, posEnd);
				value = getOperation(valueA, token, valueB);
			}
			else {
				throw new FormularParseException("can't parse token: " + token);
			}
			firstElement = false;
		}
		return value;
	}

	private HasValue getOperation(final HasValue valueA, final String token, final HasValue valueB) {
		return operations.getByName(token, valueA, valueB);
	}

	private HasValue getConstant(final String token) {
		return constants.getByName(token);
	}

	private boolean isNumber(final String token) {
		return compareUtil.isNumber(token);
	}

	private boolean isOperation(final String token) {
		return operations.existsByName(token);
	}

	private boolean isFunction(final String token) {
		return functions.existsByName(token);
	}

	private boolean isBracketOpen(final String token) {
		return BRACKET_OPEN.equals(token);
	}

	private boolean isBracketClose(final String token) {
		return BRACKET_CLOSE.equals(token);
	}

	private boolean isConstant(final String token) {
		return constants.existsByName(token);
	}

}