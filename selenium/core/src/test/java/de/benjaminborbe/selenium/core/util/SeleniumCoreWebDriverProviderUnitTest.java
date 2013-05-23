package de.benjaminborbe.selenium.core.util;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.net.URL;

import org.easymock.EasyMock;
import org.junit.Test;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;

import de.benjaminborbe.selenium.core.config.SeleniumCoreConfig;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;

public class SeleniumCoreWebDriverProviderUnitTest {

    @Test
    public void testGetSuccess() throws Exception {
        final String hostname = "localhost";
        final Integer port = 4444;
        final String urlString = "http://localhost:4444/wd/hub";
        final URL url = new URL(urlString);

        final Logger logger = EasyMock.createNiceMock(Logger.class);
        final RemoteWebDriver remoteWebDriver = EasyMock.createMock(RemoteWebDriver.class);
        final SeleniumCoreConfig seleniumCoreConfig = EasyMock.createMock(SeleniumCoreConfig.class);
        final ParseUtil parseUtil = EasyMock.createMock(ParseUtil.class);
        final SeleniumRemoteWebDriverCreator seleniumRemoteWebDriverCreator = EasyMock.createMock(SeleniumRemoteWebDriverCreator.class);
        final SeleniumCoreWebDriverRegistry seleniumCoreWebDriverRegistry = EasyMock.createNiceMock(SeleniumCoreWebDriverRegistry.class);
        final SeleniumLocalFirefoxWebDriverCreator seleniumLocalFirefoxWebDriverCreator = EasyMock.createNiceMock(SeleniumLocalFirefoxWebDriverCreator.class);
        final SeleniumLocalChromeWebDriverCreator seleniumLocalChromeWebDriverCreator = EasyMock.createNiceMock(SeleniumLocalChromeWebDriverCreator.class);

        EasyMock.expect(seleniumCoreConfig.getSeleniumRemoteHost()).andReturn(hostname);
        EasyMock.expect(seleniumCoreConfig.getSeleniumRemotePort()).andReturn(port);
        EasyMock.expect(parseUtil.parseURL(urlString)).andReturn(url);
        EasyMock.expect(seleniumRemoteWebDriverCreator.create(EasyMock.anyObject(URL.class))).andReturn(remoteWebDriver);
        EasyMock.expect(seleniumCoreConfig.getSeleniumLocal()).andReturn(false);

        final Object[] mocks = new Object[] { logger, seleniumCoreConfig, parseUtil, seleniumRemoteWebDriverCreator, remoteWebDriver,
                seleniumCoreWebDriverRegistry, seleniumLocalFirefoxWebDriverCreator, seleniumLocalChromeWebDriverCreator };
        EasyMock.replay(mocks);

        final SeleniumCoreWebDriverProvider seleniumCoreWebDriverProvider = new SeleniumCoreWebDriverProvider(logger, seleniumCoreConfig, parseUtil,
                seleniumRemoteWebDriverCreator, seleniumCoreWebDriverRegistry, seleniumLocalFirefoxWebDriverCreator, seleniumLocalChromeWebDriverCreator);
        final SeleniumCoreWebDriver webDriver = seleniumCoreWebDriverProvider.get();
        assertThat(webDriver, is(notNullValue()));

        EasyMock.verify(mocks);
    }

    @Test(expected = SeleniumCoreWebDriverCreationException.class)
    public void testGetFailure() throws Exception {
        final String hostname = null;
        final Integer port = null;
        final String url = "http://null:null/wd/hub";

        final Logger logger = EasyMock.createNiceMock(Logger.class);
        final SeleniumCoreConfig seleniumCoreConfig = EasyMock.createMock(SeleniumCoreConfig.class);
        final ParseUtil parseUtil = EasyMock.createMock(ParseUtil.class);
        final SeleniumRemoteWebDriverCreator seleniumRemoteWebDriverCreator = EasyMock.createMock(SeleniumRemoteWebDriverCreator.class);
        final SeleniumCoreWebDriverRegistry seleniumCoreWebDriverRegistry = EasyMock.createMock(SeleniumCoreWebDriverRegistry.class);
        final SeleniumLocalFirefoxWebDriverCreator seleniumLocalFirefoxWebDriverCreator = EasyMock.createNiceMock(SeleniumLocalFirefoxWebDriverCreator.class);
        final SeleniumLocalChromeWebDriverCreator seleniumLocalChromeWebDriverCreator = EasyMock.createNiceMock(SeleniumLocalChromeWebDriverCreator.class);

        EasyMock.expect(seleniumCoreConfig.getSeleniumRemoteHost()).andReturn(hostname);
        EasyMock.expect(seleniumCoreConfig.getSeleniumRemotePort()).andReturn(port);
        EasyMock.expect(parseUtil.parseURL(url)).andThrow(new ParseException("fail"));
        EasyMock.expect(seleniumCoreConfig.getSeleniumLocal()).andReturn(false);

        final Object[] mocks = new Object[] { seleniumCoreConfig, parseUtil, seleniumRemoteWebDriverCreator, seleniumCoreWebDriverRegistry,
                seleniumLocalFirefoxWebDriverCreator, logger, seleniumLocalChromeWebDriverCreator };
        EasyMock.replay(mocks);

        final SeleniumCoreWebDriverProvider seleniumCoreWebDriverProvider = new SeleniumCoreWebDriverProvider(logger, seleniumCoreConfig, parseUtil,
                seleniumRemoteWebDriverCreator, seleniumCoreWebDriverRegistry, seleniumLocalFirefoxWebDriverCreator, seleniumLocalChromeWebDriverCreator);
        final SeleniumCoreWebDriver webDriver = seleniumCoreWebDriverProvider.get();
        assertThat(webDriver, is(notNullValue()));

        EasyMock.verify(mocks);
    }
}
