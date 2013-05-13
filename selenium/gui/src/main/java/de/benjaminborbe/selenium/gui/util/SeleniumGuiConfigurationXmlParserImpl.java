package de.benjaminborbe.selenium.gui.util;

import de.benjaminborbe.selenium.api.SeleniumConfiguration;
import de.benjaminborbe.selenium.api.SeleniumConfigurationDto;
import de.benjaminborbe.selenium.api.SeleniumConfigurationIdentifier;
import de.benjaminborbe.selenium.api.action.SeleniumActionConfiguration;
import de.benjaminborbe.selenium.api.action.SeleniumActionConfigurationGetUrl;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.inject.Inject;
import java.util.ArrayList;

public class SeleniumGuiConfigurationXmlParserImpl implements SeleniumGuiConfigurationXmlParser {

	private final ParseUtil parseUtil;

	@Inject
	public SeleniumGuiConfigurationXmlParserImpl(final ParseUtil parseUtil) {
		this.parseUtil = parseUtil;
	}

	@Override
	public SeleniumConfiguration parse(final String xml) throws ParseException {
		if (xml == null || xml.isEmpty()) {
			throw new ParseException("parse xml failed! empty content");
		}
		final Document document = Jsoup.parse(xml);
		final Elements allElements = document.getAllElements();
		if (allElements.isEmpty()) {
			throw new ParseException("parse xml failed! no element found");
		}
		return parseConfig(allElements.get(0));
	}

	private SeleniumConfiguration parseConfig(final Element element) throws ParseException {
		final SeleniumConfigurationDto seleniumConfiguration = new SeleniumConfigurationDto();
		final ArrayList<SeleniumActionConfiguration> actions = new ArrayList<>();
		seleniumConfiguration.setActions(actions);
		final Element id = getElementByTag(element, "id");
		final Element name = getElementByTag(element, "name");
		seleniumConfiguration.setName(name.text());
		seleniumConfiguration.setId(new SeleniumConfigurationIdentifier(id.text()));

		final Elements actionsElement = element.getElementsByTag("actions");
		if (!actionsElement.isEmpty()) {
			final Elements actionElements = actionsElement.get(0).getElementsByTag("action");
			for (final Element actionElement : actionElements) {
				final Element message = getElementByTag(element, "message");
				final Element url = getElementByTag(element, "url");
				actions.add(new SeleniumActionConfigurationGetUrl(message.text(), parseUtil.parseURL(url.text())));
			}
		}
		return seleniumConfiguration;
	}

	private Element getElementByTag(final Element element, final String tag) throws ParseException {
		final Elements elements = element.getElementsByTag(tag);
		if (elements.isEmpty()) {
			throw new ParseException("element " + tag + " not found");
		} else {
			return elements.get(0);
		}
	}
}
