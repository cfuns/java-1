package de.benjaminborbe.lunch.util;

import static org.junit.Assert.assertEquals;

import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import de.benjaminborbe.tools.html.HtmlUtil;
import de.benjaminborbe.tools.html.HtmlUtilImpl;

public class LunchParseUtilUnitTest {

	@Test
	public void testname() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final HtmlUtil htmlUtil = new HtmlUtilImpl(logger);
		final LunchParseUtil lunchParseUtil = new LunchParseUtil(logger, htmlUtil);
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

}
