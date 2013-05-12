package de.benjaminborbe.selenium.core.util;

import de.benjaminborbe.selenium.core.config.SeleniumCoreConfig;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;
import org.easymock.EasyMock;
import org.junit.Test;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class SeleniumCoreWebDriverProviderUnitTest {

	@Test
	public void testGetSuccess() throws Exception {
		final String hostname = "localhost";
		final Integer port = 4444;
		final String urlString = "http://localhost:4444/wd/hub";
		final URL url = new URL(urlString);

		final RemoteWebDriver remoteWebDriver = EasyMock.createMock(RemoteWebDriver.class);
		final SeleniumCoreConfig seleniumCoreConfig = EasyMock.createMock(SeleniumCoreConfig.class);
		final ParseUtil parseUtil = EasyMock.createMock(ParseUtil.class);
		final SeleniumRemoteWebDriverCreator seleniumRemoteWebDriverCreator = EasyMock.createMock(SeleniumRemoteWebDriverCreator.class);

		EasyMock.expect(seleniumCoreConfig.getSeleniumRemoteHost()).andReturn(hostname);
		EasyMock.expect(seleniumCoreConfig.getSeleniumRemotePort()).andReturn(port);
		EasyMock.expect(parseUtil.parseURL(urlString)).andReturn(url);
		EasyMock.expect(seleniumRemoteWebDriverCreator.create(EasyMock.anyObject(URL.class), EasyMock.anyObject(Capabilities.class))).andReturn(remoteWebDriver);

		final Object[] mocks = new Object[]{seleniumCoreConfig, parseUtil, seleniumRemoteWebDriverCreator, remoteWebDriver};
		EasyMock.replay(mocks);

		final SeleniumCoreWebDriverProvider seleniumCoreWebDriverProvider = new SeleniumCoreWebDriverProvider(seleniumCoreConfig, parseUtil, seleniumRemoteWebDriverCreator);
		final WebDriver webDriver = seleniumCoreWebDriverProvider.get();
		assertThat(webDriver, is(notNullValue()));

		EasyMock.verify(mocks);
	}

	@Test(expected = SeleniumCoreWebDriverCreationException.class)
	public void testGetFailure() throws Exception {
		final String hostname = null;
		final Integer port = null;
		final String url = "http://null:null/wd/hub";

		final SeleniumCoreConfig seleniumCoreConfig = EasyMock.createMock(SeleniumCoreConfig.class);
		final ParseUtil parseUtil = EasyMock.createMock(ParseUtil.class);
		final SeleniumRemoteWebDriverCreator seleniumRemoteWebDriverCreator = EasyMock.createMock(SeleniumRemoteWebDriverCreator.class);

		EasyMock.expect(seleniumCoreConfig.getSeleniumRemoteHost()).andReturn(hostname);
		EasyMock.expect(seleniumCoreConfig.getSeleniumRemotePort()).andReturn(port);
		EasyMock.expect(parseUtil.parseURL(url)).andThrow(new ParseException("fail"));

		final Object[] mocks = new Object[]{seleniumCoreConfig, parseUtil, seleniumRemoteWebDriverCreator};
		EasyMock.replay(mocks);

		final SeleniumCoreWebDriverProvider seleniumCoreWebDriverProvider = new SeleniumCoreWebDriverProvider(seleniumCoreConfig, parseUtil, seleniumRemoteWebDriverCreator);
		final WebDriver webDriver = seleniumCoreWebDriverProvider.get();
		assertThat(webDriver, is(notNullValue()));

		EasyMock.verify(mocks);
	}
}
