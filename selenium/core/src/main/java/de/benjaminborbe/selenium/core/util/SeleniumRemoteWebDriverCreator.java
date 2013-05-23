package de.benjaminborbe.selenium.core.util;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

public class SeleniumRemoteWebDriverCreator {

	public WebDriver create(final URL url) {
		return new RemoteWebDriver(url, DesiredCapabilities.firefox());
	}
}
