package de.benjaminborbe.selenium.parser;

import de.benjaminborbe.selenium.api.SeleniumConfiguration;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;
import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class SeleniumGuiConfigurationXmlParserImplUnitTest {

	@Test(expected = ParseException.class)
	public void testParseNull() throws Exception {
		final ParseUtil parseUtil = EasyMock.createMock(ParseUtil.class);
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		final SeleniumGuiActionXmlParser seleniumGuiActionXmlParser = EasyMock.createMock(SeleniumGuiActionXmlParser.class);

		final Object[] mocks = new Object[]{parseUtil, logger, seleniumGuiActionXmlParser};
		EasyMock.replay(mocks);

		final SeleniumGuiConfigurationXmlParser seleniumGuiConfigurationXmlParser = new SeleniumGuiConfigurationXmlParserImpl(logger, parseUtil, seleniumGuiActionXmlParser);
		seleniumGuiConfigurationXmlParser.parse((String) null);

		EasyMock.verify(mocks);
	}

	@Test(expected = ParseException.class)
	public void testParseEmpty() throws Exception {
		final ParseUtil parseUtil = EasyMock.createMock(ParseUtil.class);
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		final SeleniumGuiActionXmlParser seleniumGuiActionXmlParser = EasyMock.createMock(SeleniumGuiActionXmlParser.class);

		final Object[] mocks = new Object[]{parseUtil, logger, seleniumGuiActionXmlParser};
		EasyMock.replay(mocks);

		final SeleniumGuiConfigurationXmlParser seleniumGuiConfigurationXmlParser = new SeleniumGuiConfigurationXmlParserImpl(logger, parseUtil, seleniumGuiActionXmlParser);
		seleniumGuiConfigurationXmlParser.parse("");

		EasyMock.verify(mocks);
	}

	@Test(expected = ParseException.class)
	public void testParseIllegalXml() throws Exception {
		final ParseUtil parseUtil = EasyMock.createMock(ParseUtil.class);
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		final SeleniumGuiActionXmlParser seleniumGuiActionXmlParser = EasyMock.createMock(SeleniumGuiActionXmlParser.class);

		final Object[] mocks = new Object[]{parseUtil, logger, seleniumGuiActionXmlParser};
		EasyMock.replay(mocks);

		final SeleniumGuiConfigurationXmlParser seleniumGuiConfigurationXmlParser = new SeleniumGuiConfigurationXmlParserImpl(logger, parseUtil, seleniumGuiActionXmlParser);
		seleniumGuiConfigurationXmlParser.parse("<asdf>");

		EasyMock.verify(mocks);
	}

	@Test
	public void testParseConfig() throws Exception {
		final ParseUtil parseUtil = EasyMock.createMock(ParseUtil.class);
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		final SeleniumGuiActionXmlParser seleniumGuiActionXmlParser = EasyMock.createMock(SeleniumGuiActionXmlParser.class);

		final String id = "test";
		final String name = "Test Configuration";
		final StringBuilder sb = new StringBuilder();
		sb.append("<config>");
		sb.append("  <id>" + id + "</id>");
		sb.append("  <name>" + name + "</name>");
		sb.append("</config>");

		final Object[] mocks = new Object[]{parseUtil, logger};
		EasyMock.replay(mocks);

		final SeleniumGuiConfigurationXmlParser seleniumGuiConfigurationXmlParser = new SeleniumGuiConfigurationXmlParserImpl(logger, parseUtil, seleniumGuiActionXmlParser);
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
	public void testParseCloseWindowWithoutNode() throws Exception {
		final String id = "test";
		final String name = "Test Configuration";
		final StringBuilder sb = new StringBuilder();
		sb.append("<config>");
		sb.append("  <id>" + id + "</id>");
		sb.append("  <name>" + name + "</name>");
		sb.append("</config>");

		final ParseUtil parseUtil = EasyMock.createMock(ParseUtil.class);
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		final SeleniumGuiActionXmlParser seleniumGuiActionXmlParser = EasyMock.createMock(SeleniumGuiActionXmlParser.class);

		final Object[] mocks = new Object[]{parseUtil, logger, seleniumGuiActionXmlParser};
		EasyMock.replay(mocks);

		final SeleniumGuiConfigurationXmlParser seleniumGuiConfigurationXmlParser = new SeleniumGuiConfigurationXmlParserImpl(logger, parseUtil, seleniumGuiActionXmlParser);
		final SeleniumConfiguration seleniumConfiguration = seleniumGuiConfigurationXmlParser.parse(sb.toString());
		assertThat(seleniumConfiguration.getCloseWindow(), is(nullValue()));

		EasyMock.verify(mocks);
	}

	@Test(expected = ParseException.class)
	public void testParseCloseWindowIllegalContent() throws Exception {
		final String id = "test";
		final String name = "Test Configuration";
		final String close = "";
		final StringBuilder sb = new StringBuilder();
		sb.append("<config>");
		sb.append("  <id>" + id + "</id>");
		sb.append("  <name>" + name + "</name>");
		sb.append("  <close>" + close + "</close>");
		sb.append("</config>");

		final SeleniumGuiActionXmlParser seleniumGuiActionXmlParser = EasyMock.createMock(SeleniumGuiActionXmlParser.class);
		final ParseUtil parseUtil = EasyMock.createMock(ParseUtil.class);
		final Logger logger = EasyMock.createNiceMock(Logger.class);

		EasyMock.expect(parseUtil.parseBoolean(close)).andThrow(new ParseException("moep"));

		final Object[] mocks = new Object[]{parseUtil, logger, seleniumGuiActionXmlParser};
		EasyMock.replay(mocks);

		final SeleniumGuiConfigurationXmlParser seleniumGuiConfigurationXmlParser = new SeleniumGuiConfigurationXmlParserImpl(logger, parseUtil, seleniumGuiActionXmlParser);
		seleniumGuiConfigurationXmlParser.parse(sb.toString());

		EasyMock.verify(mocks);
	}

	@Test
	public void testParseCloseWindowTrue() throws Exception {
		final String id = "test";
		final String name = "Test Configuration";
		final String close = "true";
		final StringBuilder sb = new StringBuilder();
		sb.append("<config>");
		sb.append("  <id>" + id + "</id>");
		sb.append("  <name>" + name + "</name>");
		sb.append("  <close>" + close + "</close>");
		sb.append("</config>");

		final ParseUtil parseUtil = EasyMock.createMock(ParseUtil.class);
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		final SeleniumGuiActionXmlParser seleniumGuiActionXmlParser = EasyMock.createMock(SeleniumGuiActionXmlParser.class);

		EasyMock.expect(parseUtil.parseBoolean(close)).andReturn(Boolean.TRUE);

		final Object[] mocks = new Object[]{parseUtil, logger, seleniumGuiActionXmlParser};
		EasyMock.replay(mocks);

		final SeleniumGuiConfigurationXmlParser seleniumGuiConfigurationXmlParser = new SeleniumGuiConfigurationXmlParserImpl(logger, parseUtil, seleniumGuiActionXmlParser);
		final SeleniumConfiguration configuration = seleniumGuiConfigurationXmlParser.parse(sb.toString());
		assertThat(configuration.getCloseWindow(), is(Boolean.TRUE));

		EasyMock.verify(mocks);
	}

	@Test
	public void testParseCloseWindowFalse() throws Exception {
		final String id = "test";
		final String name = "Test Configuration";
		final String close = "false";
		final StringBuilder sb = new StringBuilder();
		sb.append("<config>");
		sb.append("  <id>" + id + "</id>");
		sb.append("  <name>" + name + "</name>");
		sb.append("  <close>" + close + "</close>");
		sb.append("</config>");

		final ParseUtil parseUtil = EasyMock.createMock(ParseUtil.class);
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		final SeleniumGuiActionXmlParser seleniumGuiActionXmlParser = EasyMock.createMock(SeleniumGuiActionXmlParser.class);

		EasyMock.expect(parseUtil.parseBoolean(close)).andReturn(Boolean.FALSE);

		final Object[] mocks = new Object[]{parseUtil, logger, seleniumGuiActionXmlParser};
		EasyMock.replay(mocks);

		final SeleniumGuiConfigurationXmlParser seleniumGuiConfigurationXmlParser = new SeleniumGuiConfigurationXmlParserImpl(logger, parseUtil, seleniumGuiActionXmlParser);
		final SeleniumConfiguration configuration = seleniumGuiConfigurationXmlParser.parse(sb.toString());
		assertThat(configuration.getCloseWindow(), is(Boolean.FALSE));

		EasyMock.verify(mocks);
	}

}
