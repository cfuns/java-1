package de.benjaminborbe.selenium.gui.util;

import de.benjaminborbe.selenium.api.SeleniumConfiguration;
import de.benjaminborbe.tools.util.ParseException;

public interface SeleniumGuiConfigurationXmlParser {

	SeleniumConfiguration parse(String xml) throws ParseException;
}
