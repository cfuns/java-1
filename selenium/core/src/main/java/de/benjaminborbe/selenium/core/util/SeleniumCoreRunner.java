package de.benjaminborbe.selenium.core.util;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;

import javax.inject.Inject;

public class SeleniumCoreRunner {

	private final Logger logger;

	private final SeleniumCoreWebDriverProvider seleniumCoreWebDriverProvider;

	@Inject
	public SeleniumCoreRunner(final Logger logger, SeleniumCoreWebDriverProvider seleniumCoreWebDriverProvider) {
		this.logger = logger;
		this.seleniumCoreWebDriverProvider = seleniumCoreWebDriverProvider;
	}

	public void run() {
		WebDriver driver = null;
		try {
			driver = seleniumCoreWebDriverProvider.get();

			// http://docs.seleniumhq.org/docs/04_webdriver_advanced.jsp
			// driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			driver.manage().window().maximize();
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

		} catch (Exception e) {
			logger.error("Exception", e);
		} finally {
			if (driver != null)
				driver.close();
		}
	}
}
