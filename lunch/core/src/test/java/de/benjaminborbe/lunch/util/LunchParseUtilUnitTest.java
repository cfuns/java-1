package de.benjaminborbe.lunch.util;

import de.benjaminborbe.tools.html.HtmlTagParser;
import de.benjaminborbe.tools.html.HtmlUtil;
import de.benjaminborbe.tools.html.HtmlUtilImpl;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.util.ParseUtilImpl;
import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
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
			assertEquals(title, lunchParseUtil.extractLunchName(htmlContent));
		}
		{
			final String htmlContent = "<p><ac:macro ac:name=\"tip\"><ac:rich-text-body><p><ac:macro ac:name=\"excerpt\"><ac:parameter ac:name=\"atlassian-macro-output-type\">INLINE</ac:parameter><ac:rich-text-body><p>"
				+ title + "</p></ac:rich-text-body></ac:macro><br class=\"atl-forced-newline\" /></p></ac:rich-text-body></ac:macro></p>";
			assertEquals(title, lunchParseUtil.extractLunchName(htmlContent));
		}
	}

	private LunchParseUtil getLunchParseUtil() {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final HtmlTagParser htmlTagParser = new HtmlTagParser(logger);
		final HtmlUtil htmlUtil = new HtmlUtilImpl(logger, htmlTagParser);
		final ParseUtil parseUtil = new ParseUtilImpl();
		final LunchParseUtil lunchParseUtil = new LunchParseUtil(logger, htmlUtil, parseUtil);
		return lunchParseUtil;
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
}
