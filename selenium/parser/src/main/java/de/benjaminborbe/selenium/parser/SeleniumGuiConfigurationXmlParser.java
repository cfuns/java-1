package de.benjaminborbe.selenium.parser;

import de.benjaminborbe.selenium.api.SeleniumConfiguration;
import de.benjaminborbe.tools.util.ParseException;

public interface SeleniumGuiConfigurationXmlParser {

	SeleniumConfiguration parse(String xml) throws ParseException;
}
