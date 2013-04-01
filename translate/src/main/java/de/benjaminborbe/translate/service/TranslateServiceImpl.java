package de.benjaminborbe.translate.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.benjaminborbe.translate.api.TranslateService;
import org.slf4j.Logger;

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
