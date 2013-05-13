package de.benjaminborbe.selenium.gui.util;

import de.benjaminborbe.selenium.api.SeleniumConfiguration;
import de.benjaminborbe.selenium.api.SeleniumConfigurationDto;
import de.benjaminborbe.selenium.api.SeleniumConfigurationIdentifier;
import de.benjaminborbe.selenium.api.action.SeleniumActionConfiguration;
import de.benjaminborbe.selenium.api.action.SeleniumActionConfigurationGetUrl;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import javax.inject.Inject;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class SeleniumGuiConfigurationXmlParserImpl implements SeleniumGuiConfigurationXmlParser {

	private final ParseUtil parseUtil;

	@Inject
	public SeleniumGuiConfigurationXmlParserImpl(final ParseUtil parseUtil) {
		this.parseUtil = parseUtil;
	}

	@Override
	public SeleniumConfiguration parse(final String xml) throws ParseException {
		try {
			if (xml == null || xml.isEmpty()) {
				throw new ParseException("parse xml failed! empty content");
			}
			final SAXBuilder sb = new SAXBuilder();
			final Document doc = sb.build(new StringReader(xml));
			Element rootElement = doc.getRootElement();

			if (!"config".equals(rootElement.getName())) {
				throw new ParseException("rootElement element != config");
			} else {
				final SeleniumConfigurationDto seleniumConfiguration = new SeleniumConfigurationDto();
				seleniumConfiguration.setId(new SeleniumConfigurationIdentifier(rootElement.getChildText("id")));
				seleniumConfiguration.setName(rootElement.getChildText("name"));
				final ArrayList<SeleniumActionConfiguration> list = new ArrayList<>();
				seleniumConfiguration.setActions(list);

				Element actionsElement = rootElement.getChild("actions");
				if (actionsElement != null) {
					final List<Element> children = actionsElement.getChildren("action");
					for (final Element actionElement : children) {
						list.add(new SeleniumActionConfigurationGetUrl(actionElement.getChildText("message"), parseUtil.parseURL(actionElement.getChildText("url"))));
					}
				}

				return seleniumConfiguration;
			}
		} catch (JDOMException | IOException e) {
			throw new ParseException(e);
		}
	}
}
