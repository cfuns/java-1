package de.benjaminborbe.selenium.parser;

import de.benjaminborbe.selenium.api.SeleniumConfiguration;
import de.benjaminborbe.selenium.api.action.SeleniumActionConfiguration;
import de.benjaminborbe.selenium.api.action.SeleniumActionConfigurationClick;
import de.benjaminborbe.selenium.api.action.SeleniumActionConfigurationExpectUrl;
import de.benjaminborbe.selenium.api.action.SeleniumActionConfigurationGetUrl;
import de.benjaminborbe.selenium.api.action.SeleniumActionConfigurationSendKeys;
import de.benjaminborbe.selenium.api.action.SeleniumActionConfigurationSleep;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;
import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import java.net.URL;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class SeleniumGuiConfigurationXmlParserImplUnitTest {

	@Test(expected = ParseException.class)
	public void testParseNull() throws Exception {
		final ParseUtil parseUtil = EasyMock.createMock(ParseUtil.class);
		final Logger logger = EasyMock.createNiceMock(Logger.class);

		final Object[] mocks = new Object[]{parseUtil, logger};
		EasyMock.replay(mocks);

		final SeleniumGuiConfigurationXmlParser seleniumGuiConfigurationXmlParser = new SeleniumGuiConfigurationXmlParserImpl(logger, parseUtil);
		seleniumGuiConfigurationXmlParser.parse(null);

		EasyMock.verify(mocks);
	}

	@Test(expected = ParseException.class)
	public void testParseEmpty() throws Exception {
		final ParseUtil parseUtil = EasyMock.createMock(ParseUtil.class);
		final Logger logger = EasyMock.createNiceMock(Logger.class);

		final Object[] mocks = new Object[]{parseUtil, logger};
		EasyMock.replay(mocks);

		final SeleniumGuiConfigurationXmlParser seleniumGuiConfigurationXmlParser = new SeleniumGuiConfigurationXmlParserImpl(logger, parseUtil);
		seleniumGuiConfigurationXmlParser.parse("");

		EasyMock.verify(mocks);
	}

	@Test(expected = ParseException.class)
	public void testParseIllegal() throws Exception {
		final ParseUtil parseUtil = EasyMock.createMock(ParseUtil.class);
		final Logger logger = EasyMock.createNiceMock(Logger.class);

		final Object[] mocks = new Object[]{parseUtil, logger};
		EasyMock.replay(mocks);

		final SeleniumGuiConfigurationXmlParser seleniumGuiConfigurationXmlParser = new SeleniumGuiConfigurationXmlParserImpl(logger, parseUtil);
		seleniumGuiConfigurationXmlParser.parse("<asdf>");

		EasyMock.verify(mocks);
	}

	@Test
	public void testParseConfig() throws Exception {
		final ParseUtil parseUtil = EasyMock.createMock(ParseUtil.class);
		final Logger logger = EasyMock.createNiceMock(Logger.class);

		final String id = "test";
		final String name = "Test Configuration";
		final StringBuilder sb = new StringBuilder();
		sb.append("<config>");
		sb.append("  <id>" + id + "</id>");
		sb.append("  <name>" + name + "</name>");
		sb.append("</config>");

		final Object[] mocks = new Object[]{parseUtil, logger};
		EasyMock.replay(mocks);

		final SeleniumGuiConfigurationXmlParser seleniumGuiConfigurationXmlParser = new SeleniumGuiConfigurationXmlParserImpl(logger, parseUtil);
		final SeleniumConfiguration seleniumConfiguration = seleniumGuiConfigurationXmlParser.parse(sb.toString());
		assertThat(seleniumConfiguration.getId(), is(notNullValue()));
		assertThat(seleniumConfiguration.getId().getId(), is(id));
		assertThat(seleniumConfiguration.getName(), is(notNullValue()));
		assertThat(seleniumConfiguration.getName(), is(name));
		assertThat(seleniumConfiguration.getActionConfigurations(), is(notNullValue()));
		assertThat(seleniumConfiguration.getActionConfigurations().isEmpty(), is(true));

		EasyMock.verify(mocks);
	}

	@Test
	public void testParseGetUrl() throws Exception {
		final String id = "test";
		final String name = "Test Configuration";
		final String message = "test message";
		final String url = "http://www.benjamin-borbe.de";
		final StringBuilder sb = new StringBuilder();
		sb.append("<config>");
		sb.append("  <id>" + id + "</id>");
		sb.append("  <name>" + name + "</name>");
		sb.append("  <actions>");
		sb.append("    <action name=\"GetUrl\">");
		sb.append("      <message>" + message + "</message>");
		sb.append("      <url>" + url + "</url>");
		sb.append("    </action>");
		sb.append("  </actions>");
		sb.append("</config>");

		final ParseUtil parseUtil = EasyMock.createMock(ParseUtil.class);
		final Logger logger = EasyMock.createNiceMock(Logger.class);

		EasyMock.expect(parseUtil.parseURL(url)).andReturn(new URL(url));

		final Object[] mocks = new Object[]{parseUtil, logger};
		EasyMock.replay(mocks);

		final SeleniumGuiConfigurationXmlParser seleniumGuiConfigurationXmlParser = new SeleniumGuiConfigurationXmlParserImpl(logger, parseUtil);
		final SeleniumConfiguration seleniumConfiguration = seleniumGuiConfigurationXmlParser.parse(sb.toString());
		assertThat(seleniumConfiguration.getId(), is(notNullValue()));
		assertThat(seleniumConfiguration.getId().getId(), is(id));
		assertThat(seleniumConfiguration.getName(), is(notNullValue()));
		assertThat(seleniumConfiguration.getName(), is(name));
		assertThat(seleniumConfiguration.getActionConfigurations(), is(notNullValue()));
		assertThat(seleniumConfiguration.getActionConfigurations().isEmpty(), is(false));
		final SeleniumActionConfiguration seleniumActionConfiguration = seleniumConfiguration.getActionConfigurations().get(0);

		assertThat(seleniumActionConfiguration.getClass().getName(), is(SeleniumActionConfigurationGetUrl.class.getName()));
		final SeleniumActionConfigurationGetUrl seleniumActionConfigurationGetUrl = (SeleniumActionConfigurationGetUrl) seleniumActionConfiguration;
		assertThat(seleniumActionConfigurationGetUrl.getMessage(), is(notNullValue()));
		assertThat(seleniumActionConfigurationGetUrl.getMessage(), is(message));
		assertThat(seleniumActionConfigurationGetUrl.getUrl(), is(notNullValue()));
		assertThat(seleniumActionConfigurationGetUrl.getUrl().toExternalForm(), is(url));

		EasyMock.verify(mocks);
	}

	@Test
	public void testParseClick() throws Exception {
		final String id = "test";
		final String name = "Test Configuration";
		final String message = "test message";
		final String xpath = "myxpath";
		final StringBuilder sb = new StringBuilder();
		sb.append("<config>");
		sb.append("  <id>" + id + "</id>");
		sb.append("  <name>" + name + "</name>");
		sb.append("  <actions>");
		sb.append("    <action name=\"Click\">");
		sb.append("      <message>" + message + "</message>");
		sb.append("      <xpath>" + xpath + "</xpath>");
		sb.append("    </action>");
		sb.append("  </actions>");
		sb.append("</config>");

		final ParseUtil parseUtil = EasyMock.createMock(ParseUtil.class);
		final Logger logger = EasyMock.createNiceMock(Logger.class);

		final Object[] mocks = new Object[]{parseUtil, logger};
		EasyMock.replay(mocks);

		final SeleniumGuiConfigurationXmlParser seleniumGuiConfigurationXmlParser = new SeleniumGuiConfigurationXmlParserImpl(logger, parseUtil);
		final SeleniumConfiguration seleniumConfiguration = seleniumGuiConfigurationXmlParser.parse(sb.toString());
		assertThat(seleniumConfiguration.getId(), is(notNullValue()));
		assertThat(seleniumConfiguration.getId().getId(), is(id));
		assertThat(seleniumConfiguration.getName(), is(notNullValue()));
		assertThat(seleniumConfiguration.getName(), is(name));
		assertThat(seleniumConfiguration.getActionConfigurations(), is(notNullValue()));
		assertThat(seleniumConfiguration.getActionConfigurations().isEmpty(), is(false));
		final SeleniumActionConfiguration seleniumActionConfiguration = seleniumConfiguration.getActionConfigurations().get(0);

		assertThat(seleniumActionConfiguration.getClass().getName(), is(SeleniumActionConfigurationClick.class.getName()));
		final SeleniumActionConfigurationClick configuration = (SeleniumActionConfigurationClick) seleniumActionConfiguration;
		assertThat(configuration.getMessage(), is(notNullValue()));
		assertThat(configuration.getMessage(), is(message));
		assertThat(configuration.getXpath(), is(notNullValue()));
		assertThat(configuration.getXpath(), is(xpath));

		EasyMock.verify(mocks);
	}

	@Test
	public void testParseSleep() throws Exception {
		final String id = "test";
		final String name = "Test Configuration";
		final String message = "test message";
		final long duration = 5000;
		final StringBuilder sb = new StringBuilder();
		sb.append("<config>");
		sb.append("  <id>" + id + "</id>");
		sb.append("  <name>" + name + "</name>");
		sb.append("  <actions>");
		sb.append("    <action name=\"Sleep\">");
		sb.append("      <message>" + message + "</message>");
		sb.append("      <duration>" + duration + "</duration>");
		sb.append("    </action>");
		sb.append("  </actions>");
		sb.append("</config>");

		final ParseUtil parseUtil = EasyMock.createMock(ParseUtil.class);
		EasyMock.expect(parseUtil.parseLong(String.valueOf(duration))).andReturn(duration);
		final Logger logger = EasyMock.createNiceMock(Logger.class);

		final Object[] mocks = new Object[]{parseUtil, logger};
		EasyMock.replay(mocks);

		final SeleniumGuiConfigurationXmlParser seleniumGuiConfigurationXmlParser = new SeleniumGuiConfigurationXmlParserImpl(logger, parseUtil);
		final SeleniumConfiguration seleniumConfiguration = seleniumGuiConfigurationXmlParser.parse(sb.toString());
		assertThat(seleniumConfiguration.getId(), is(notNullValue()));
		assertThat(seleniumConfiguration.getId().getId(), is(id));
		assertThat(seleniumConfiguration.getName(), is(notNullValue()));
		assertThat(seleniumConfiguration.getName(), is(name));
		assertThat(seleniumConfiguration.getActionConfigurations(), is(notNullValue()));
		assertThat(seleniumConfiguration.getActionConfigurations().isEmpty(), is(false));
		final SeleniumActionConfiguration seleniumActionConfiguration = seleniumConfiguration.getActionConfigurations().get(0);

		assertThat(seleniumActionConfiguration.getClass().getName(), is(SeleniumActionConfigurationSleep.class.getName()));
		final SeleniumActionConfigurationSleep configuration = (SeleniumActionConfigurationSleep) seleniumActionConfiguration;
		assertThat(configuration.getMessage(), is(notNullValue()));
		assertThat(configuration.getMessage(), is(message));
		assertThat(configuration.getDuration(), is(duration));

		EasyMock.verify(mocks);
	}

	@Test
	public void testParseExpectUrl() throws Exception {
		final String id = "test";
		final String name = "Test Configuration";
		final String message = "test message";
		final String url = "http://www.benjamin-borbe.de";
		final StringBuilder sb = new StringBuilder();
		sb.append("<config>");
		sb.append("  <id>" + id + "</id>");
		sb.append("  <name>" + name + "</name>");
		sb.append("  <actions>");
		sb.append("    <action name=\"ExpectUrl\">");
		sb.append("      <message>" + message + "</message>");
		sb.append("      <url>" + url + "</url>");
		sb.append("    </action>");
		sb.append("  </actions>");
		sb.append("</config>");

		final ParseUtil parseUtil = EasyMock.createMock(ParseUtil.class);
		final Logger logger = EasyMock.createNiceMock(Logger.class);

		EasyMock.expect(parseUtil.parseURL(url)).andReturn(new URL(url));

		final Object[] mocks = new Object[]{parseUtil, logger};
		EasyMock.replay(mocks);

		final SeleniumGuiConfigurationXmlParser seleniumGuiConfigurationXmlParser = new SeleniumGuiConfigurationXmlParserImpl(logger, parseUtil);
		final SeleniumConfiguration seleniumConfiguration = seleniumGuiConfigurationXmlParser.parse(sb.toString());
		assertThat(seleniumConfiguration.getId(), is(notNullValue()));
		assertThat(seleniumConfiguration.getId().getId(), is(id));
		assertThat(seleniumConfiguration.getName(), is(notNullValue()));
		assertThat(seleniumConfiguration.getName(), is(name));
		assertThat(seleniumConfiguration.getActionConfigurations(), is(notNullValue()));
		assertThat(seleniumConfiguration.getActionConfigurations().isEmpty(), is(false));
		final SeleniumActionConfiguration seleniumActionConfiguration = seleniumConfiguration.getActionConfigurations().get(0);

		assertThat(seleniumActionConfiguration.getClass().getName(), is(SeleniumActionConfigurationExpectUrl.class.getName()));
		final SeleniumActionConfigurationExpectUrl seleniumActionConfigurationGetUrl = (SeleniumActionConfigurationExpectUrl) seleniumActionConfiguration;
		assertThat(seleniumActionConfigurationGetUrl.getMessage(), is(notNullValue()));
		assertThat(seleniumActionConfigurationGetUrl.getMessage(), is(message));
		assertThat(seleniumActionConfigurationGetUrl.getUrl(), is(notNullValue()));
		assertThat(seleniumActionConfigurationGetUrl.getUrl().toExternalForm(), is(url));

		EasyMock.verify(mocks);
	}

	@Test
	public void testParseSendKeys() throws Exception {
		final String id = "test";
		final String name = "Test Configuration";
		final String message = "test message";
		final String xpath = "myxpath";
		final String keys = "abc";
		final StringBuilder sb = new StringBuilder();
		sb.append("<config>");
		sb.append("  <id>" + id + "</id>");
		sb.append("  <name>" + name + "</name>");
		sb.append("  <actions>");
		sb.append("    <action name=\"SendKeys\">");
		sb.append("      <message>" + message + "</message>");
		sb.append("      <xpath>" + xpath + "</xpath>");
		sb.append("      <keys>" + keys + "</keys>");
		sb.append("    </action>");
		sb.append("  </actions>");
		sb.append("</config>");

		final ParseUtil parseUtil = EasyMock.createMock(ParseUtil.class);
		final Logger logger = EasyMock.createNiceMock(Logger.class);

		final Object[] mocks = new Object[]{parseUtil, logger};
		EasyMock.replay(mocks);

		final SeleniumGuiConfigurationXmlParser seleniumGuiConfigurationXmlParser = new SeleniumGuiConfigurationXmlParserImpl(logger, parseUtil);
		final SeleniumConfiguration seleniumConfiguration = seleniumGuiConfigurationXmlParser.parse(sb.toString());
		assertThat(seleniumConfiguration.getId(), is(notNullValue()));
		assertThat(seleniumConfiguration.getId().getId(), is(id));
		assertThat(seleniumConfiguration.getName(), is(notNullValue()));
		assertThat(seleniumConfiguration.getName(), is(name));
		assertThat(seleniumConfiguration.getActionConfigurations(), is(notNullValue()));
		assertThat(seleniumConfiguration.getActionConfigurations().isEmpty(), is(false));
		final SeleniumActionConfiguration seleniumActionConfiguration = seleniumConfiguration.getActionConfigurations().get(0);

		assertThat(seleniumActionConfiguration.getClass().getName(), is(SeleniumActionConfigurationSendKeys.class.getName()));
		final SeleniumActionConfigurationSendKeys configuration = (SeleniumActionConfigurationSendKeys) seleniumActionConfiguration;
		assertThat(configuration.getMessage(), is(notNullValue()));
		assertThat(configuration.getMessage(), is(message));
		assertThat(configuration.getXpath(), is(notNullValue()));
		assertThat(configuration.getXpath(), is(xpath));
		assertThat(configuration.getKeys(), is(notNullValue()));
		assertThat(configuration.getKeys(), is(keys));

		EasyMock.verify(mocks);
	}
}
