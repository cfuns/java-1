package de.benjaminborbe.selenium.core.configuration;

import de.benjaminborbe.selenium.api.SeleniumConfigurationIdentifier;
import de.benjaminborbe.selenium.core.util.SeleniumExecutionProtocolImpl;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;

import javax.inject.Inject;

public class SeleniumConfigurationSimple implements SeleniumConfigurationAction {

	private final Logger logger;

	@Inject
	public SeleniumConfigurationSimple(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public SeleniumConfigurationIdentifier getId() {
		return new SeleniumConfigurationIdentifier("1");
	}

	@Override
	public String getName() {
		return "simple";
	}

	@Override
	public void run(final WebDriver driver, SeleniumExecutionProtocolImpl seleniumExecutionProtocol) {

		final String url = "http://www.heise.de";
		driver.get(url);
		seleniumExecutionProtocol.addInfo("get " + url);

		logger.debug("title: " + driver.getTitle());
		logger.debug("currentUrl: " + driver.getCurrentUrl());
		logger.debug("pageSource.length: " + driver.getPageSource().length());
		logger.debug("windowHandle: " + driver.getWindowHandle());
		logger.debug("windowHandles: " + driver.getWindowHandles());

		final String xpathExpression = "//*[@id=\"themen_aktuell\"]/ol/li[4]/a";
		driver.findElement(By.xpath(xpathExpression)).click();
		seleniumExecutionProtocol.addInfo("click element " + xpathExpression);

		logger.debug("text: " + driver.findElement(By.xpath("//*[@id=\"mitte_uebersicht\"]/div[1]/h1")).getText());

		logger.debug("title: " + driver.getTitle());
		logger.debug("currentUrl: " + driver.getCurrentUrl());
		logger.debug("pageSource.length: " + driver.getPageSource().length());
		logger.debug("windowHandle: " + driver.getWindowHandle());
		logger.debug("windowHandles: " + driver.getWindowHandles());

		logger.trace("pageSource: " + driver.getPageSource());
		seleniumExecutionProtocol.addInfo("done");
	}
}
