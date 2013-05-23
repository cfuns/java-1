package de.benjaminborbe.selenium.gui.guice;

import com.google.inject.AbstractModule;
import de.benjaminborbe.selenium.parser.SeleniumGuiActionXmlParser;
import de.benjaminborbe.selenium.parser.SeleniumGuiActionXmlParserImpl;
import de.benjaminborbe.selenium.parser.SeleniumGuiConfigurationXmlParser;
import de.benjaminborbe.selenium.parser.SeleniumGuiConfigurationXmlParserImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;
import org.slf4j.Logger;

import javax.inject.Singleton;

public class SeleniumGuiModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(SeleniumGuiActionXmlParser.class).to(SeleniumGuiActionXmlParserImpl.class);
		bind(SeleniumGuiConfigurationXmlParser.class).to(SeleniumGuiConfigurationXmlParserImpl.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
	}
}
