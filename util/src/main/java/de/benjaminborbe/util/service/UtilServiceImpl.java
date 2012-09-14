package de.benjaminborbe.util.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.util.api.UtilService;
import de.benjaminborbe.util.api.UtilServiceException;
import de.benjaminborbe.util.math.FormularParseException;
import de.benjaminborbe.util.math.FormularParser;

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
		}
		catch (final FormularParseException e) {
			throw new UtilServiceException(e.getClass().getSimpleName(), e);
		}
	}

}
