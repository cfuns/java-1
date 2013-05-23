package de.benjaminborbe.selenium.parser;

import de.benjaminborbe.selenium.api.SeleniumConfiguration;
import de.benjaminborbe.tools.util.ParseException;
import org.jdom2.Element;

public interface SeleniumGuiConfigurationXmlParser {

	SeleniumConfiguration parse(final Element rootElement) throws ParseException;

	SeleniumConfiguration parse(String xml) throws ParseException;
}
