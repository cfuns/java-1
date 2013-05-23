package de.benjaminborbe.selenium.core.util;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class SeleniumLocalWebDriverCreator {

	public WebDriver create() {
//		final String chromepath = "/Applications/Google Chrome.app/Contents/MacOS/Google Chrome";
//		System.setProperty("webdriver.chrome.driver", chromepath);
//		final DesiredCapabilities capabilities = DesiredCapabilities.chrome();
//		capabilities.setCapability("chrome.switches", Arrays.asList("--start-maximized","--no-sandbox","--allow-running-insecure-content"));
//		capabilities.setCapability("chrome.binary", chromepath);
//		return new ChromeDriver(capabilities);

		return new FirefoxDriver();
	}
}
