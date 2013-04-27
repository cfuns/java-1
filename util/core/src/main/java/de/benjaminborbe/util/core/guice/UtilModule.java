package de.benjaminborbe.util.core.guice;

import com.google.inject.AbstractModule;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;
import de.benjaminborbe.util.api.UtilService;
import de.benjaminborbe.util.core.math.FormularParser;
import de.benjaminborbe.util.core.math.FormularParserImpl;
import de.benjaminborbe.util.core.math.tokenizer.Tokenizer;
import de.benjaminborbe.util.core.math.tokenizer.TokenizerImpl;
import de.benjaminborbe.util.core.service.UtilServiceImpl;
import org.slf4j.Logger;

import javax.inject.Singleton;

public class UtilModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(FormularParser.class).to(FormularParserImpl.class).in(Singleton.class);
		bind(Tokenizer.class).to(TokenizerImpl.class).in(Singleton.class);
		bind(UtilService.class).to(UtilServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
