package de.benjaminborbe.util.core.math;

import com.google.inject.Inject;
import de.benjaminborbe.util.core.math.constant.Constants;
import de.benjaminborbe.util.core.math.function.Functions;
import de.benjaminborbe.util.core.math.operation.Operations;
import de.benjaminborbe.util.core.math.tokenizer.Tokenizer;

import java.util.List;

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
		if (tokens.size() == 0) {
			throw new FormularParseException(formular);
		}
		final HasValue result = parse(tokens);
		if (result == null) {
			throw new FormularParseException(formular);
		}
		return result;
	}

	public HasValue parse(final List<String> tokens) throws FormularParseException {
		return parse(tokens, 0, tokens.size() - 1);
	}

	public HasValue parse(final List<String> tokens, final int posStart, final int posEnd) throws FormularParseException {
		HasValue value = null;
		boolean firstElement = true;
		for (int i = posEnd; i >= posStart; --i) {
			final String token = tokens.get(i);
			if (isNumber(token)) {
				value = new NumberValue(token);
			} else if (isOperation(token) && value != null) {
				final HasValue valueA = parse(tokens, posStart, i - 1);
				final HasValue valueB = value;
				return getOperation(token, valueA, valueB);
			} else if (isConstant(token)) {
				value = getConstant(token);
			} else if (isBracketOpen(token) && !firstElement) {
			} else if (isBracketClose(token)) {
				return parse(tokens, posStart, i - 1);
			} else if (isFunction(token)) {
			} else {
				throw new FormularParseException("can't parse token: " + token);
			}
			firstElement = false;
		}
		return value;
	}

	private HasValue getOperation(final String operation, final HasValue valueA, final HasValue valueB) {
		return operations.get(operation, valueA, valueB);
	}

	private HasValue getConstant(final String constant) {
		return constants.get(constant);
	}

	private boolean isNumber(final String token) {
		return compareUtil.isNumber(token);
	}

	private boolean isOperation(final String token) {
		return operations.exists(token);
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
		return constants.exists(token);
	}

}
