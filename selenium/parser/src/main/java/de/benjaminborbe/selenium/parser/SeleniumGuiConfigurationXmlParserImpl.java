package de.benjaminborbe.selenium.parser;

import de.benjaminborbe.selenium.api.SeleniumConfiguration;
import de.benjaminborbe.selenium.api.SeleniumConfigurationDto;
import de.benjaminborbe.selenium.api.SeleniumConfigurationIdentifier;
import de.benjaminborbe.selenium.api.action.SeleniumActionConfiguration;
import de.benjaminborbe.selenium.api.action.SeleniumActionConfigurationClick;
import de.benjaminborbe.selenium.api.action.SeleniumActionConfigurationExpectText;
import de.benjaminborbe.selenium.api.action.SeleniumActionConfigurationGetUrl;
import de.benjaminborbe.selenium.api.action.SeleniumActionConfigurationPageContent;
import de.benjaminborbe.selenium.api.action.SeleniumActionConfigurationPageInfo;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class SeleniumGuiConfigurationXmlParserImpl implements SeleniumGuiConfigurationXmlParser {

	private final Logger logger;

	private final ParseUtil parseUtil;

	@Inject
	public SeleniumGuiConfigurationXmlParserImpl(final Logger logger, final ParseUtil parseUtil) {
		this.logger = logger;
		this.parseUtil = parseUtil;
	}

	@Override
	public SeleniumConfiguration parse(final String xml) throws ParseException {
		try {
			logger.trace("parse xml: " + xml);
			if (xml == null || xml.isEmpty()) {
				throw new ParseException("parse xml failed! empty content");
			}
			final SAXBuilder sb = new SAXBuilder();
			final Document doc = sb.build(new StringReader(xml));
			final Element rootElement = doc.getRootElement();

			if (!"config".equals(rootElement.getName())) {
				throw new ParseException("rootElement element != config");
			} else {
				final SeleniumConfigurationDto seleniumConfiguration = new SeleniumConfigurationDto();
				seleniumConfiguration.setId(new SeleniumConfigurationIdentifier(rootElement.getChildText("id")));
				seleniumConfiguration.setName(rootElement.getChildText("name"));
				final ArrayList<SeleniumActionConfiguration> list = new ArrayList<>();
				seleniumConfiguration.setActions(list);

				final Element actionsElement = rootElement.getChild("actions");
				if (actionsElement != null) {
					final List<Element> children = actionsElement.getChildren("action");
					for (final Element actionElement : children) {
						final Attribute name = actionElement.getAttribute("name");
						if ("Click".equals(name.getValue())) {
							list.add(new SeleniumActionConfigurationClick(actionElement.getChildText("message"), actionElement.getChildText("xpath")));
						}
						if ("GetUrl".equals(name.getValue())) {
							list.add(new SeleniumActionConfigurationGetUrl(actionElement.getChildText("message"), parseUtil.parseURL(actionElement.getChildText("url"))));
						}
						if ("ExpectText".equals(name.getValue())) {
							list.add(new SeleniumActionConfigurationExpectText(actionElement.getChildText("message"), actionElement.getChildText("xpath"), actionElement.getChildText("text")));
						}
						if ("PageContent".equals(name.getValue())) {
							list.add(new SeleniumActionConfigurationPageContent(actionElement.getChildText("message")));
						}
						if ("PageInfo".equals(name.getValue())) {
							list.add(new SeleniumActionConfigurationPageInfo(actionElement.getChildText("message")));
						}
					}
				}

				return seleniumConfiguration;
			}
		} catch (JDOMException | IOException e) {
			throw new ParseException(e);
		}
	}
}
