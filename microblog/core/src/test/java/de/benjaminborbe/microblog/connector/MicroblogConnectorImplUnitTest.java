package de.benjaminborbe.microblog.connector;

import de.benjaminborbe.httpdownloader.api.HttpRequest;
import de.benjaminborbe.httpdownloader.api.HttpResponse;
import de.benjaminborbe.httpdownloader.api.HttpdownloaderService;
import de.benjaminborbe.httpdownloader.tools.HttpUtil;
import de.benjaminborbe.mail.api.MailService;
import de.benjaminborbe.microblog.api.MicroblogPostIdentifier;
import de.benjaminborbe.microblog.config.MicroblogConfig;
import de.benjaminborbe.microblog.conversation.MicroblogConversationResult;
import de.benjaminborbe.microblog.revision.MicroblogRevisionStorage;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.CalendarUtilImpl;
import de.benjaminborbe.tools.date.CurrentTime;
import de.benjaminborbe.tools.date.CurrentTimeImpl;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.date.TimeZoneUtilImpl;
import de.benjaminborbe.tools.html.HtmlTagParser;
import de.benjaminborbe.tools.html.HtmlUtil;
import de.benjaminborbe.tools.html.HtmlUtilImpl;
import de.benjaminborbe.tools.stream.ChannelTools;
import de.benjaminborbe.tools.stream.StreamUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.util.ResourceUtil;
import de.benjaminborbe.tools.util.ResourceUtilImpl;
import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MicroblogConnectorImplUnitTest {

	@Test
	public void testParseLatestRevision() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		final ParseUtil parseUtil = EasyMock.createMock(ParseUtil.class);
		final HtmlUtil htmlUtil = EasyMock.createMock(HtmlUtil.class);
		final MicroblogConfig microblogConfig = EasyMock.createMock(MicroblogConfig.class);
		final HttpdownloaderService httpdownloaderService = EasyMock.createMock(HttpdownloaderService.class);
		final HttpUtil httpUtil = EasyMock.createMock(HttpUtil.class);
		final HttpResponse httpResponse = EasyMock.createMock(HttpResponse.class);

		final TimeZoneUtil timeZoneUtil = new TimeZoneUtilImpl();
		final CurrentTime currentTime = new CurrentTimeImpl();
		final CalendarUtil calendarUtil = new CalendarUtilImpl(logger, currentTime, parseUtil, timeZoneUtil);
		final MicroblogPostIdentifier rev = new MicroblogPostIdentifier(1337l);
		final String rssFeed = "https://micro.rp.seibert-media.net/api/statuses/friends_timeline/bborbe.rss";
		final String content = "bla <guid>https://micro.rp.seibert-media.net/notice/" + rev + "</guid> bla";
		final URL url = new URL(rssFeed);

		EasyMock.expect(parseUtil.parseLong(String.valueOf(rev))).andReturn(rev.getId());
		EasyMock.expect(microblogConfig.getMicroblogRssFeed()).andReturn(rssFeed).anyTimes();
		EasyMock.expect(microblogConfig.getMicroblogUrl()).andReturn("https://micro.rp.seibert-media.net").anyTimes();
		EasyMock.expect(parseUtil.parseURL(rssFeed)).andReturn(url);
		EasyMock.expect(httpdownloaderService.fetch(EasyMock.anyObject(HttpRequest.class))).andReturn(httpResponse);
		EasyMock.expect(httpUtil.getContent(httpResponse)).andReturn(content);

		final Object[] mocks = new Object[]{httpResponse, httpdownloaderService, microblogConfig, logger, parseUtil, httpUtil, htmlUtil};
		EasyMock.replay(mocks);

		final MicroblogConnector microblogConnector = new MicroblogConnectorImpl(logger, calendarUtil, timeZoneUtil, microblogConfig, parseUtil, htmlUtil, httpdownloaderService, httpUtil);
		assertEquals(rev, microblogConnector.getLatestRevision());

		EasyMock.verify(mocks);
	}

	@Test
	public void testExtractContentJoinGroup() throws Exception {
		final HttpdownloaderService httpdownloaderService = EasyMock.createMock(HttpdownloaderService.class);
		final HttpUtil httpUtil = EasyMock.createMock(HttpUtil.class);
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		final MicroblogConnector microblogConnector = EasyMock.createMock(MicroblogConnector.class);
		final MicroblogRevisionStorage microblogRevisionStorage = EasyMock.createMock(MicroblogRevisionStorage.class);
		final MailService mailService = EasyMock.createMock(MailService.class);
		final ParseUtil parseUtil = EasyMock.createMock(ParseUtil.class);
		final HtmlUtil htmlUtil = EasyMock.createMock(HtmlUtil.class);
		final MicroblogConfig microblogConfig = EasyMock.createMock(MicroblogConfig.class);

		final StringBuilder pageContent = new StringBuilder();
		pageContent.append("<div class=\"entry-title\">");
		pageContent
			.append("<div class=\"join-activity\"><a href=\"https://micro.rp.seibert-media.net/bgates\">Bill Gates</a> joined the group <a href=\"https://micro.rp.seibert-media.net/group/tech\">HELL</a>.</div>");
		pageContent.append("</div>");
		pageContent.append("<div class=\"entry-content\">");
		pageContent.append("<a rel=\"bookmark\" class=\"timestamp\" href=\"https://micro.rp.seibert-media.net/notice/27693\">");
		pageContent.append("<abbr class=\"published\" title=\"2012-11-05T10:30:32+01:00\">about 2 hours ago</abbr>");
		pageContent.append("</a>");
		pageContent.append("<span class=\"source\">from <span class=\"device\">activity</span>");
		pageContent.append("</span>");
		pageContent.append("</div>");
		final String result = "Bill Gates joined the group HELL";
		final String htmlContent = "<a href=\"https://micro.rp.seibert-media.net/bgates\">Bill Gates</a> joined the group <a href=\"https://micro.rp.seibert-media.net/group/tech\">HELL</a>.";
		final TimeZoneUtil timeZoneUtil = new TimeZoneUtilImpl();
		final CurrentTime currentTime = new CurrentTimeImpl();
		final CalendarUtil calendarUtil = new CalendarUtilImpl(logger, currentTime, parseUtil, timeZoneUtil);

		EasyMock.expect(htmlUtil.filterHtmlTages(htmlContent)).andReturn(result);
		EasyMock.expect(htmlUtil.unescapeHtml(result)).andReturn(result);

		final Object[] mocks = new Object[]{httpdownloaderService, microblogConfig, htmlUtil, mailService, parseUtil, microblogRevisionStorage, microblogConnector, logger, httpUtil};
		EasyMock.replay(mocks);

		final MicroblogConnectorImpl microblogConnectorImpl = new MicroblogConnectorImpl(logger, calendarUtil, timeZoneUtil, microblogConfig, parseUtil, htmlUtil, httpdownloaderService, httpUtil);
		assertEquals(result, microblogConnectorImpl.extractContent(pageContent.toString()));

		EasyMock.verify(mocks);
	}

	@Test
	public void testExtractContent() throws Exception {
		final StringBuilder pageContent = new StringBuilder();
		pageContent.append("<h1>Bill Gates (bgates)'s status on Thursday, 19-Jan-12 12:39:37 CET</h1>");
		pageContent.append("<div id=\"content_inner\">");
		pageContent.append("<ol class=\"notices xoxo\">");
		pageContent.append("<li class=\"hentry notice notice-source-web\" id=\"notice-1337\">");
		pageContent.append("<div class=\"entry-title\">");
		pageContent.append("<div class=\"author\">");
		pageContent.append("<span class=\"vcard author\">");
		pageContent.append("<a href=\"https://micro.rp.seibert-media.net/bgates\" class=\"url\" title=\"bgates\">");
		pageContent.append("<img src=\"https://micro.rp.seibert-media.net/avatar/24-48-1231231238.jpeg\" class=\"avatar photo\" alt=\"Bill Gates\" height=\"48\" width=\"48\">");
		pageContent.append("<span class=\"fn\">Foo Bar</span>");
		pageContent.append("</a>");
		pageContent.append("</span>");
		pageContent.append("<span class=\"addressees\">");
		pageContent.append("<a href=\"https://micro.rp.seibert-media.net/group/design\" title=\"design\" class=\"addressee group\">DESIGN</a>");
		pageContent.append("</span>");
		pageContent.append("</div>");
		pageContent.append("<p class=\"entry-content\">Foo<br/><b>&quote;Bar&quote;</b></p>");
		pageContent.append("</div>");
		pageContent.append("<div class=\"entry-content thumbnails\"></div>");
		pageContent.append("<div class=\"entry-content\">");
		pageContent.append("<a rel=\"bookmark\" class=\"timestamp\" href=\"https://micro.rp.seibert-media.net/notice/1337\">");
		pageContent.append("<abbr class=\"published\" title=\"2012-01-19T12:39:37+01:00\">about a day ago</abbr>");
		pageContent.append("</a>");
		pageContent.append("<span class=\"source\">from<span class=\"device\">web</span>");
		pageContent.append("</span>");
		pageContent.append("</div>");
		pageContent.append("</li>");
		pageContent.append("</ol>");
		pageContent.append("</div>");

		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final MicroblogConnector microblogConnector = EasyMock.createMock(MicroblogConnector.class);
		EasyMock.replay(microblogConnector);

		final MicroblogRevisionStorage microblogRevisionStorage = EasyMock.createMock(MicroblogRevisionStorage.class);
		EasyMock.replay(microblogRevisionStorage);

		final MailService mailService = EasyMock.createMock(MailService.class);
		EasyMock.replay(mailService);

		final ParseUtil parseUtil = EasyMock.createMock(ParseUtil.class);
		EasyMock.replay(parseUtil);

		final String result = "Foo \"Bar\"";

		final HtmlUtil htmlUtil = EasyMock.createMock(HtmlUtil.class);
		EasyMock.expect(htmlUtil.filterHtmlTages("Foo<br/><b>&quote;Bar&quote;</b>")).andReturn("Foo &quote;Bar&quote;");
		EasyMock.expect(htmlUtil.unescapeHtml("Foo &quote;Bar&quote;")).andReturn(result);
		EasyMock.replay(htmlUtil);

		final MicroblogConfig microblogConfig = EasyMock.createMock(MicroblogConfig.class);
		EasyMock.replay(microblogConfig);

		final TimeZoneUtil timeZoneUtil = new TimeZoneUtilImpl();
		final CurrentTime currentTime = new CurrentTimeImpl();
		final CalendarUtil calendarUtil = new CalendarUtilImpl(logger, currentTime, parseUtil, timeZoneUtil);

		final HttpdownloaderService httpdownloaderService = EasyMock.createMock(HttpdownloaderService.class);
		final HttpUtil httpUtil = EasyMock.createMock(HttpUtil.class);

		final Object[] mocks = new Object[]{httpdownloaderService, httpUtil};
		EasyMock.replay(mocks);

		final MicroblogConnectorImpl microblogConnectorImpl = new MicroblogConnectorImpl(logger, calendarUtil, timeZoneUtil, microblogConfig, parseUtil, htmlUtil, httpdownloaderService, httpUtil);

		assertEquals(result, microblogConnectorImpl.extractContent(pageContent.toString()));

		EasyMock.verify(mocks);
	}

	@Test
	public void testExtractAuthor() {
		final StringBuilder pageContent = new StringBuilder();
		pageContent.append("<span class=\"vcard author\">");
		pageContent.append("<a href=\"https://micro.rp.seibert-media.net/bgates\" class=\"url\" title=\"bgates\">");
		pageContent.append("<img src=\"https://micro.rp.seibert-media.net/avatar/76-48-20110505084348.jpeg\" class=\"avatar photo\" alt=\"Bill Gates\" height=\"48\" width=\"48\">");
		pageContent.append("<span class=\"fn\">Bill Gates</span>");
		pageContent.append("</a>");
		pageContent.append("</span>");

		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final MicroblogConnector microblogConnector = EasyMock.createMock(MicroblogConnector.class);
		EasyMock.replay(microblogConnector);

		final MicroblogRevisionStorage microblogRevisionStorage = EasyMock.createMock(MicroblogRevisionStorage.class);
		EasyMock.replay(microblogRevisionStorage);

		final MailService mailService = EasyMock.createMock(MailService.class);
		EasyMock.replay(mailService);

		final ParseUtil parseUtil = EasyMock.createMock(ParseUtil.class);
		EasyMock.replay(parseUtil);

		final HtmlUtil htmlUtil = EasyMock.createMock(HtmlUtil.class);
		EasyMock.replay(htmlUtil);

		final MicroblogConfig microblogConfig = EasyMock.createMock(MicroblogConfig.class);
		EasyMock.expect(microblogConfig.getMicroblogUrl()).andReturn("https://micro.rp.seibert-media.net").anyTimes();
		EasyMock.replay(microblogConfig);

		final TimeZoneUtil timeZoneUtil = new TimeZoneUtilImpl();
		final CurrentTime currentTime = new CurrentTimeImpl();
		final CalendarUtil calendarUtil = new CalendarUtilImpl(logger, currentTime, parseUtil, timeZoneUtil);

		final HttpdownloaderService httpdownloaderService = EasyMock.createMock(HttpdownloaderService.class);
		final HttpUtil httpUtil = EasyMock.createMock(HttpUtil.class);

		final Object[] mocks = new Object[]{httpdownloaderService, httpUtil};
		EasyMock.replay(mocks);

		final MicroblogConnectorImpl microblogConnectorImpl = new MicroblogConnectorImpl(logger, calendarUtil, timeZoneUtil, microblogConfig, parseUtil, htmlUtil, httpdownloaderService, httpUtil);
		assertEquals("bgates", microblogConnectorImpl.extractAuthor(pageContent.toString()));

		EasyMock.verify(mocks);
	}

	@Test
	public void testExtractConversationUrl() {
		final StringBuilder pageContent = new StringBuilder();

		pageContent.append("<div class=\"entry-content\">");
		pageContent.append("<a href=\"https://micro.rp.seibert-media.net/notice/1337\" class=\"timestamp\" rel=\"bookmark\">");
		pageContent.append("<abbr title=\"2012-01-23T08:27:01+01:00\" class=\"published\">about 36 minutes ago</abbr>");
		pageContent.append("</a>");
		pageContent.append("<span class=\"source\">from<span class=\"device\">web</span>");
		pageContent.append("</span>");
		pageContent.append("<a class=\"response\" href=\"https://micro.rp.seibert-media.net/conversation/42#notice-1337\">in context</a>");
		pageContent.append("</div>");

		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final MicroblogConnector microblogConnector = EasyMock.createMock(MicroblogConnector.class);
		EasyMock.replay(microblogConnector);

		final MicroblogRevisionStorage microblogRevisionStorage = EasyMock.createMock(MicroblogRevisionStorage.class);
		EasyMock.replay(microblogRevisionStorage);

		final MailService mailService = EasyMock.createMock(MailService.class);
		EasyMock.replay(mailService);

		final ParseUtil parseUtil = EasyMock.createMock(ParseUtil.class);
		EasyMock.replay(parseUtil);

		final HtmlUtil htmlUtil = EasyMock.createMock(HtmlUtil.class);
		EasyMock.replay(htmlUtil);

		final MicroblogConfig microblogConfig = EasyMock.createMock(MicroblogConfig.class);
		EasyMock.replay(microblogConfig);

		final TimeZoneUtil timeZoneUtil = new TimeZoneUtilImpl();
		final CurrentTime currentTime = new CurrentTimeImpl();
		final CalendarUtil calendarUtil = new CalendarUtilImpl(logger, currentTime, parseUtil, timeZoneUtil);
		final HttpdownloaderService httpdownloaderService = EasyMock.createMock(HttpdownloaderService.class);
		final HttpUtil httpUtil = EasyMock.createMock(HttpUtil.class);

		final Object[] mocks = new Object[]{httpdownloaderService, httpUtil};
		EasyMock.replay(mocks);

		final MicroblogConnectorImpl microblogConnectorImpl = new MicroblogConnectorImpl(logger, calendarUtil, timeZoneUtil, microblogConfig, parseUtil, htmlUtil, httpdownloaderService, httpUtil);
		assertEquals("https://micro.rp.seibert-media.net/conversation/42#notice-1337", microblogConnectorImpl.extractConversationUrl(pageContent.toString()));

		EasyMock.verify(mocks);
	}

	@Test
	public void testBuildMicroblogConversationResult() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final MicroblogRevisionStorage microblogRevisionStorage = EasyMock.createMock(MicroblogRevisionStorage.class);
		EasyMock.replay(microblogRevisionStorage);

		final MailService mailService = EasyMock.createMock(MailService.class);
		EasyMock.replay(mailService);

		final ParseUtil parseUtil = EasyMock.createMock(ParseUtil.class);
		EasyMock.expect(parseUtil.parseLong("13")).andReturn(13l);
		EasyMock.expect(parseUtil.parseLong("14")).andReturn(14l);
		EasyMock.replay(parseUtil);

		final HtmlTagParser htmlTagParser = new HtmlTagParser(logger);
		final HtmlUtil htmlUtil = new HtmlUtilImpl(logger, htmlTagParser);
		final StreamUtil streamUtil = new StreamUtil(new ChannelTools());
		final ResourceUtil resourceUtil = new ResourceUtilImpl(streamUtil);

		final MicroblogConfig microblogConfig = EasyMock.createMock(MicroblogConfig.class);
		EasyMock.replay(microblogConfig);

		final TimeZoneUtil timeZoneUtil = new TimeZoneUtilImpl();
		final CurrentTime currentTime = new CurrentTimeImpl();
		final CalendarUtil calendarUtil = new CalendarUtilImpl(logger, currentTime, parseUtil, timeZoneUtil);

		final HttpdownloaderService httpdownloaderService = EasyMock.createMock(HttpdownloaderService.class);
		final HttpUtil httpUtil = EasyMock.createMock(HttpUtil.class);

		final Object[] mocks = new Object[]{httpdownloaderService, httpUtil};
		EasyMock.replay(mocks);

		final MicroblogConnectorImpl microblogConnectorImpl = new MicroblogConnectorImpl(logger, calendarUtil, timeZoneUtil, microblogConfig, parseUtil, htmlUtil, httpdownloaderService, httpUtil);

		final String pageContent = resourceUtil.getResourceContentAsString("sample_conversation.txt");
		assertNotNull(pageContent);
		final String conversationUrl = "http://testd.de";
		final MicroblogConversationResult result = microblogConnectorImpl.buildMicroblogConversationResult(conversationUrl, pageContent);
		assertNotNull(result);
		assertNotNull(result.getConversationUrl());
		assertEquals(conversationUrl, result.getConversationUrl());
		assertNotNull(result.getPosts());
		assertEquals(2, result.getPosts().size());

		assertNotNull(result.getPosts().get(1));
		assertEquals("text1", result.getPosts().get(1).getContent());
		assertEquals("user1", result.getPosts().get(1).getAuthor());
		assertEquals("https://micro.rp.seibert-media.net/notice/13", result.getPosts().get(1).getPostUrl());
		assertEquals(conversationUrl, result.getPosts().get(1).getConversationUrl());

		assertNotNull(result.getPosts().get(0));
		assertEquals("text2", result.getPosts().get(0).getContent());
		assertEquals("user2", result.getPosts().get(0).getAuthor());
		assertEquals("https://micro.rp.seibert-media.net/notice/14", result.getPosts().get(0).getPostUrl());
		assertEquals(conversationUrl, result.getPosts().get(0).getConversationUrl());

		EasyMock.verify(mocks);
	}
}
