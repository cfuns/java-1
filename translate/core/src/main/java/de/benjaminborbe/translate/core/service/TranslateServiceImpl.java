package de.benjaminborbe.translate.core.service;

import de.benjaminborbe.translate.api.TranslateService;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TranslateServiceImpl implements TranslateService {

	private final Logger logger;

	@Inject
	public TranslateServiceImpl(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public String translate(final String text) {
		logger.trace("translate");
		return text;
	}

}
