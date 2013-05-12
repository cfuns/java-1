package de.benjaminborbe.selenium.configuration.simple.service;

import de.benjaminborbe.selenium.api.SeleniumConfiguration;
import de.benjaminborbe.selenium.api.SeleniumConfigurationIdentifier;
import de.benjaminborbe.selenium.api.action.SeleniumActionConfiguration;
import de.benjaminborbe.selenium.api.action.SeleniumActionConfigurationClick;
import de.benjaminborbe.selenium.api.action.SeleniumActionConfigurationExpectText;
import de.benjaminborbe.selenium.api.action.SeleniumActionConfigurationGetUrl;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SeleniumConfigurationSimple implements SeleniumConfiguration {

	private final Logger logger;

	@Inject
	public SeleniumConfigurationSimple(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public SeleniumConfigurationIdentifier getId() {
		return new SeleniumConfigurationIdentifier(getClass().getName());
	}

	@Override
	public String getName() {
		return "simple";
	}

	@Override
	public List<SeleniumActionConfiguration> getActionConfigurations() {
		try {
			final List<SeleniumActionConfiguration> list = new ArrayList<>();
			list.add(new SeleniumActionConfigurationGetUrl("open heise", new URL("http://www.heise.de")));
			list.add(new SeleniumActionConfigurationClick("click themen_aktuell", "//*[@id=\"themen_aktuell\"]/ol/li[4]/a"));
			list.add(new SeleniumActionConfigurationExpectText("find headline mitte_uebersicht", "//*[@id=\"mitte_uebersicht\"]/div[1]/h1", "Facebook – nicht nur eine Erfolgsgeschichte"));
			return list;
		} catch (MalformedURLException e) {
			return null;
		}
	}

}
