package de.benjaminborbe.util.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.util.api.UtilService;
import de.benjaminborbe.util.api.UtilServiceException;
import de.benjaminborbe.util.math.ExpressionParseException;
import de.benjaminborbe.util.math.ExpressionParser;

@Singleton
public class UtilServiceImpl implements UtilService {

	private final ExpressionParser expressionParser;

	@Inject
	public UtilServiceImpl(final ExpressionParser expressionParser) {
		this.expressionParser = expressionParser;
	}

	@Override
	public double calc(final String expression) throws UtilServiceException {
		try {
			return expressionParser.parse(expression);
		}
		catch (final ExpressionParseException e) {
			throw new UtilServiceException(e.getClass().getSimpleName(), e);
		}
	}

}
