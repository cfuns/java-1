package de.benjaminborbe.util.guice;

import org.slf4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.tools.log.LoggerSlf4Provider;
import de.benjaminborbe.util.api.UtilService;
import de.benjaminborbe.util.math.FormularParser;
import de.benjaminborbe.util.math.FormularParserImpl;
import de.benjaminborbe.util.math.Tokenizer;
import de.benjaminborbe.util.math.TokenizerImpl;
import de.benjaminborbe.util.service.UtilServiceImpl;

public class UtilModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(FormularParser.class).to(FormularParserImpl.class).in(Singleton.class);
		bind(Tokenizer.class).to(TokenizerImpl.class).in(Singleton.class);
		bind(UtilService.class).to(UtilServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
