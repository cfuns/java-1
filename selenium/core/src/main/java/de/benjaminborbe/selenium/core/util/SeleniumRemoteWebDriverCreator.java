package de.benjaminborbe.selenium.core.util;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

public class SeleniumRemoteWebDriverCreator {

	public RemoteWebDriver create(final URL url, final Capabilities capability) {
		return new RemoteWebDriver(url, capability);
	}
}
