package de.benjaminborbe.lunch.util;

import de.benjaminborbe.tools.html.HtmlTagParser;
import de.benjaminborbe.tools.html.HtmlUtil;
import de.benjaminborbe.tools.html.HtmlUtilImpl;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.util.ParseUtilImpl;
import de.benjaminborbe.tools.util.StringUtil;
import de.benjaminborbe.tools.util.StringUtilImpl;
import org.easymock.EasyMock;
import org.easymock.IMockBuilder;
import org.junit.Test;
import org.slf4j.Logger;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class LunchParseUtilUnitTest {

	@Test
	public void testExtractLunchName() throws Exception {
		final LunchParseUtil lunchParseUtil = getLunchParseUtil();
		final String title = "FooBar";
		{
			final String htmlContent = "<div class='panelMacro'><table class='tipMacro'><colgroup><col width='24'><col></colgroup><tr><td valign='top'><img src=\"/images/icons/emoticons/check.png\" width=\"16\" height=\"16\" align=\"absmiddle\" alt=\"\" border=\"0\"></td><td><p>"
				+ title + "<br class=\"atl-forced-newline\" /></p></td></tr></table></div>";
			assertEquals(title, lunchParseUtil.extractLunchNameFromContent(htmlContent));
		}
		{
			final String htmlContent = "<p><ac:macro ac:name=\"tip\"><ac:rich-text-body><p><ac:macro ac:name=\"excerpt\"><ac:parameter ac:name=\"atlassian-macro-output-type\">INLINE</ac:parameter><ac:rich-text-body><p>"
				+ title + "</p></ac:rich-text-body></ac:macro><br class=\"atl-forced-newline\" /></p></ac:rich-text-body></ac:macro></p>";
			assertEquals(title, lunchParseUtil.extractLunchNameFromContent(htmlContent));
		}
	}

	private LunchParseUtil getLunchParseUtil() {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final HtmlTagParser htmlTagParser = new HtmlTagParser(logger);
		final HtmlUtil htmlUtil = new HtmlUtilImpl(logger, htmlTagParser);
		final ParseUtil parseUtil = new ParseUtilImpl();
		final StringUtil stringUtil = new StringUtilImpl();
		return new LunchParseUtil(logger, htmlUtil, parseUtil, stringUtil);
	}

	@Test
	public void testExtractSubscribedUser() {
		final LunchParseUtil lunchParseUtil = getLunchParseUtil();

		final StringBuilder sb = new StringBuilder();
		sb.append("<table id='TBL1355500921669'  atlassian-macro-output-type=BLOCK class=\"confluenceTable\">");
		sb.append("<tbody>");
		sb.append("<tr>");
		sb.append("<th class=\"confluenceTh\">Teilnehmer</th>");
		sb.append("<th class=\"confluenceTh\">Teilgenommen?</th></tr>");
		sb.append("<tr>");
		sb.append("<td class=\"confluenceTd\">Vorname1 Nachname1</td>");
		sb.append("<td class=\"confluenceTd\">bla</td>");
		sb.append("</tr>");
		sb.append("<tr>");
		sb.append("<td class=\"confluenceTd\">Vorname2 Nachname2</td>");
		sb.append("<td class=\"confluenceTd\">bla</td>");
		sb.append("</tr>");
		sb.append("</tbody></table>");

		final Set<String> result = new HashSet<String>(lunchParseUtil.extractSubscribedUser(sb.toString()));
		assertThat(result, is(notNullValue()));
		assertThat(result.size(), is(2));
		assertThat(result.contains("Vorname1 Nachname1"), is(true));
		assertThat(result.contains("Vorname2 Nachname2"), is(true));
	}

	@Test
	public void testExtractLunchNameFromTitleOrContentFoundInContent() throws Exception {
		final String title = "title123";
		final String content = "content123";
		final String name = "name123";
		final LunchParseUtil lunchParseUtil = createLunchParseUtil("extractLunchNameFromContent");
		EasyMock.expect(lunchParseUtil.extractLunchNameFromContent(content)).andReturn(name);
		EasyMock.replay(lunchParseUtil);
		assertThat(lunchParseUtil.extractLunchNameFromTitleOrContent(title, content), is(name));
	}

	@Test
	public void testExtractLunchNameFromTitleOrContentNotFoundInContent() throws Exception {
		final String title = "title123";
		final String content = "content123";
		final String name = "name123";
		final LunchParseUtil lunchParseUtil = createLunchParseUtil("extractLunchNameFromContent", "extractLunchNameFromTitle");
		EasyMock.expect(lunchParseUtil.extractLunchNameFromContent(content)).andReturn(null);
		EasyMock.expect(lunchParseUtil.extractLunchNameFromTitle(title)).andReturn(name);
		EasyMock.replay(lunchParseUtil);
		assertThat(lunchParseUtil.extractLunchNameFromTitleOrContent(title, content), is(name));
	}

	@Test
	public void testExtractLunchNameFromTitleOrContentNotFound() throws Exception {
		final String title = "title123";
		final String content = "content123";
		final String name = "name123";
		final LunchParseUtil lunchParseUtil = createLunchParseUtil("extractLunchNameFromContent", "extractLunchNameFromTitle");
		EasyMock.expect(lunchParseUtil.extractLunchNameFromContent(content)).andReturn(" ");
		EasyMock.expect(lunchParseUtil.extractLunchNameFromTitle(title)).andReturn(" ");
		EasyMock.replay(lunchParseUtil);
		assertThat(lunchParseUtil.extractLunchNameFromTitleOrContent(title, content), is(nullValue()));
	}

	private LunchParseUtil createLunchParseUtil(final String... methods) {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);
		final HtmlTagParser htmlTagParser = new HtmlTagParser(logger);
		final HtmlUtil htmlUtil = new HtmlUtilImpl(logger, htmlTagParser);
		final ParseUtil parseUtil = new ParseUtilImpl();
		final StringUtil stringUtil = new StringUtilImpl();
		final IMockBuilder<LunchParseUtil> builder = EasyMock.createMockBuilder(LunchParseUtil.class);
		builder.withConstructor(logger, htmlUtil, parseUtil, stringUtil);
		builder.addMockedMethods(methods);
		return builder.createMock();
	}

	@Test
	public void testExtractLunchNameFromTitle() throws Exception {
		final LunchParseUtil lunchParseUtil = getLunchParseUtil();
		assertThat(lunchParseUtil.extractLunchNameFromTitle("2014-01-31 Bastians Mittagessen"), is(nullValue()));
		assertThat(lunchParseUtil.extractLunchNameFromTitle("2014-01-31 Foo Bar"), is("Foo Bar"));
		assertThat(lunchParseUtil.extractLunchNameFromTitle("2014-01-31 - Foo Bar"), is("Foo Bar"));
	}
}
