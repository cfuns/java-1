package de.benjaminborbe.selenium.core.action;

import de.benjaminborbe.selenium.api.action.SeleniumActionConfigurationClick;
import de.benjaminborbe.selenium.core.util.SeleniumExecutionProtocolImpl;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SeleniumActionClick implements SeleniumAction<SeleniumActionConfigurationClick> {

	@Override
	public Class<SeleniumActionConfigurationClick> getType() {
		return SeleniumActionConfigurationClick.class;
	}

	@Override
	public boolean execute(
		final WebDriver webDriver, final SeleniumExecutionProtocolImpl seleniumExecutionProtocol, final SeleniumActionConfigurationClick seleniumActionConfiguration
	) {

		final String xpathExpression = "//*[@id=\"themen_aktuell\"]/ol/li[4]/a";
		webDriver.findElement(By.xpath(xpathExpression)).click();
		seleniumExecutionProtocol.addInfo("click element " + xpathExpression);

		return true;
	}
}
