package de.benjaminborbe.selenium.parser;

import de.benjaminborbe.selenium.api.action.SeleniumActionConfiguration;
import de.benjaminborbe.tools.util.ParseException;
import org.jdom2.Element;

public interface SeleniumGuiActionXmlParser {

	SeleniumActionConfiguration parse(String xml) throws ParseException;

	SeleniumActionConfiguration parse(Element actionElement) throws ParseException;
}
