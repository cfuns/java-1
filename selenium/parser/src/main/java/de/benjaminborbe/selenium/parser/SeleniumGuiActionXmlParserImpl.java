package de.benjaminborbe.selenium.parser;

import de.benjaminborbe.selenium.api.action.SeleniumActionConfiguration;
import de.benjaminborbe.selenium.api.action.SeleniumActionConfigurationClick;
import de.benjaminborbe.selenium.api.action.SeleniumActionConfigurationExpectText;
import de.benjaminborbe.selenium.api.action.SeleniumActionConfigurationExpectUrl;
import de.benjaminborbe.selenium.api.action.SeleniumActionConfigurationFollowAttribute;
import de.benjaminborbe.selenium.api.action.SeleniumActionConfigurationGetUrl;
import de.benjaminborbe.selenium.api.action.SeleniumActionConfigurationPageContent;
import de.benjaminborbe.selenium.api.action.SeleniumActionConfigurationPageInfo;
import de.benjaminborbe.selenium.api.action.SeleniumActionConfigurationSelect;
import de.benjaminborbe.selenium.api.action.SeleniumActionConfigurationSendKeys;
import de.benjaminborbe.selenium.api.action.SeleniumActionConfigurationSleep;
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

public class SeleniumGuiActionXmlParserImpl implements SeleniumGuiActionXmlParser {

	private final Logger logger;

	private final ParseUtil parseUtil;

	@Inject
	public SeleniumGuiActionXmlParserImpl(
		final Logger logger,
		final ParseUtil parseUtil
	) {
		this.logger = logger;
		this.parseUtil = parseUtil;
	}

	@Override
	public SeleniumActionConfiguration parse(final String xml) throws ParseException {
		try {
			logger.trace("parse xml: " + xml);
			if (xml == null || xml.isEmpty()) {
				throw new ParseException("parse xml failed! empty content");
			}
			final SAXBuilder sb = new SAXBuilder();
			final Document doc = sb.build(new StringReader(xml));
			return parse(doc.getRootElement());

		} catch (JDOMException | IOException e) {
			throw new ParseException(e);
		}
	}

	@Override
	public SeleniumActionConfiguration parse(final Element actionElement) throws ParseException {
		final Attribute nameAttribute = actionElement.getAttribute("name");
		final String name = nameAttribute.getValue();
		if ("Click".equals(name)) {
			return new SeleniumActionConfigurationClick(actionElement.getChildText("message"), actionElement.getChildText("xpath"));
		} else if ("FollowAttribute".equals(name)) {
			return new SeleniumActionConfigurationFollowAttribute(actionElement.getChildText("message"), actionElement.getChildText("xpath"), actionElement.getChildText("attribute"));
		} else if ("SendKeys".equals(name)) {
			return new SeleniumActionConfigurationSendKeys(actionElement.getChildText("message"), actionElement.getChildText("xpath"), actionElement.getChildText("keys"));
		} else if ("GetUrl".equals(name)) {
			return new SeleniumActionConfigurationGetUrl(actionElement.getChildText("message"), parseUtil.parseURL(actionElement.getChildText("url")));
		} else if ("ExpectUrl".equals(name)) {
			return new SeleniumActionConfigurationExpectUrl(actionElement.getChildText("message"), parseUtil.parseURL(actionElement.getChildText("url")));
		} else if ("ExpectText".equals(name)) {
			return new SeleniumActionConfigurationExpectText(actionElement.getChildText("message"), actionElement.getChildText("xpath"), actionElement.getChildText("text"));
		} else if ("Select".equals(name)) {
			return new SeleniumActionConfigurationSelect(actionElement.getChildText("message"), actionElement.getChildText("xpath"), actionElement.getChildText("value"));
		} else if ("PageContent".equals(name)) {
			return new SeleniumActionConfigurationPageContent(actionElement.getChildText("message"));
		} else if ("PageInfo".equals(name)) {
			return new SeleniumActionConfigurationPageInfo(actionElement.getChildText("message"));
		} else if ("Sleep".equals(name)) {
			return new SeleniumActionConfigurationSleep(actionElement.getChildText("message"), parseUtil.parseLong(actionElement.getChildText("duration")));
		} else {
			throw new ParseException("illegal action-nameAttribute: " + name);
		}
	}
}
