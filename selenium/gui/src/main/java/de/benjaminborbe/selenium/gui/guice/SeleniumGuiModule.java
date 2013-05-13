package de.benjaminborbe.selenium.gui.guice;

import com.google.inject.AbstractModule;
import de.benjaminborbe.selenium.gui.util.SeleniumGuiConfigurationXmlParser;
import de.benjaminborbe.selenium.gui.util.SeleniumGuiConfigurationXmlParserImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;
import org.slf4j.Logger;

import javax.inject.Singleton;

public class SeleniumGuiModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(SeleniumGuiConfigurationXmlParser.class).to(SeleniumGuiConfigurationXmlParserImpl.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
