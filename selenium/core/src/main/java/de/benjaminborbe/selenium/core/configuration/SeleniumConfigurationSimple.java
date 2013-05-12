package de.benjaminborbe.selenium.core.configuration;

import de.benjaminborbe.selenium.api.SeleniumConfigurationIdentifier;
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
	public void run(final WebDriver driver) {

		driver.get("http://www.heise.de");

		logger.debug("title: " + driver.getTitle());
		logger.debug("currentUrl: " + driver.getCurrentUrl());
		logger.debug("pageSource.length: " + driver.getPageSource().length());
		logger.debug("windowHandle: " + driver.getWindowHandle());
		logger.debug("windowHandles: " + driver.getWindowHandles());

		driver.findElement(By.xpath("//*[@id=\"themen_aktuell\"]/ol/li[4]/a")).click();

		logger.debug("text: " + driver.findElement(By.xpath("//*[@id=\"mitte_uebersicht\"]/div[1]/h1")).getText());

		logger.debug("title: " + driver.getTitle());
		logger.debug("currentUrl: " + driver.getCurrentUrl());
		logger.debug("pageSource.length: " + driver.getPageSource().length());
		logger.debug("windowHandle: " + driver.getWindowHandle());
		logger.debug("windowHandles: " + driver.getWindowHandles());

		logger.trace("pageSource: " + driver.getPageSource());
	}
}
