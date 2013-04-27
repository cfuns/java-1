package de.benjaminborbe.util.core.service;

import de.benjaminborbe.util.api.UtilService;
import de.benjaminborbe.util.api.UtilServiceException;
import de.benjaminborbe.util.core.math.FormularParseException;
import de.benjaminborbe.util.core.math.FormularParser;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UtilServiceImpl implements UtilService {

	private final FormularParser expressionParser;

	@Inject
	public UtilServiceImpl(final FormularParser expressionParser) {
		this.expressionParser = expressionParser;
	}

	@Override
	public double calc(final String expression) throws UtilServiceException {
		try {
			return expressionParser.parse(expression).getValue();
		} catch (final FormularParseException e) {
			throw new UtilServiceException(e.getClass().getSimpleName(), e);
		}
	}

}
