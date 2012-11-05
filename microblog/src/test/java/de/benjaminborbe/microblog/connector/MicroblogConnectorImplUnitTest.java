package de.benjaminborbe.microblog.connector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.URL;

import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import de.benjaminborbe.mail.api.MailService;
import de.benjaminborbe.microblog.api.MicroblogPostIdentifier;
import de.benjaminborbe.microblog.conversation.MicroblogConversationResult;
import de.benjaminborbe.microblog.revision.MicroblogRevisionStorage;
import de.benjaminborbe.tools.html.HtmlUtil;
import de.benjaminborbe.tools.http.HttpDownloadResult;
import de.benjaminborbe.tools.http.HttpDownloadUtil;
import de.benjaminborbe.tools.http.HttpDownloader;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.util.ResourceUtil;
import de.benjaminborbe.tools.util.ResourceUtilImpl;
import de.benjaminborbe.tools.util.StreamUtil;

public class MicroblogConnectorImplUnitTest {

	@Test
	public void testParseLatestRevision() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final HttpDownloadResult httpDownloadResult = EasyMock.createMock(HttpDownloadResult.class);
		EasyMock.replay(httpDownloadResult);

		final HttpDownloader httpDownloader = EasyMock.createMock(HttpDownloader.class);
		EasyMock.expect(httpDownloader.getUrlUnsecure(new URL("https://micro.rp.seibert-media.net/api/statuses/friends_timeline/bborbe.rss"), 5000)).andReturn(httpDownloadResult);
		EasyMock.replay(httpDownloader);

		final MicroblogPostIdentifier rev = new MicroblogPostIdentifier(1337l);
		final String content = "bla <guid>https://micro.rp.seibert-media.net/notice/" + rev + "</guid> bla";

		final HttpDownloadUtil httpDownloadUtil = EasyMock.createMock(HttpDownloadUtil.class);
		EasyMock.expect(httpDownloadUtil.getContent(httpDownloadResult)).andReturn(content);
		EasyMock.replay(httpDownloadUtil);

		final ParseUtil parseUtil = EasyMock.createMock(ParseUtil.class);
		EasyMock.expect(parseUtil.parseLong(String.valueOf(rev))).andReturn(rev.getId());
		EasyMock.replay(parseUtil);

		final HtmlUtil htmlUtil = EasyMock.createMock(HtmlUtil.class);
		EasyMock.replay(htmlUtil);

		final MicroblogConnector microblogConnector = new MicroblogConnectorImpl(logger, httpDownloader, httpDownloadUtil, parseUtil, htmlUtil);
		assertEquals(rev, microblogConnector.getLatestRevision());
	}

	@Test
	public void testExtractContentJoinGroup() {
		final StringBuffer pageContent = new StringBuffer();

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

		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final MicroblogConnector microblogConnector = EasyMock.createMock(MicroblogConnector.class);
		EasyMock.replay(microblogConnector);

		final MicroblogRevisionStorage microblogRevisionStorage = EasyMock.createMock(MicroblogRevisionStorage.class);
		EasyMock.replay(microblogRevisionStorage);

		final MailService mailService = EasyMock.createMock(MailService.class);
		EasyMock.replay(mailService);

		final HttpDownloader httpDownloader = EasyMock.createNiceMock(HttpDownloader.class);
		EasyMock.replay(httpDownloader);

		final HttpDownloadUtil httpDownloadUtil = EasyMock.createNiceMock(HttpDownloadUtil.class);
		EasyMock.replay(httpDownloadUtil);

		final ParseUtil parseUtil = EasyMock.createMock(ParseUtil.class);
		EasyMock.replay(parseUtil);

		final String result = "Bill Gates joined the group HELL";

		final HtmlUtil htmlUtil = EasyMock.createMock(HtmlUtil.class);
		EasyMock
				.expect(
						htmlUtil
								.filterHtmlTages("<a href=\"https://micro.rp.seibert-media.net/bgates\">Bill Gates</a> joined the group <a href=\"https://micro.rp.seibert-media.net/group/tech\">HELL</a>."))
				.andReturn(result);
		EasyMock.expect(htmlUtil.unescapeHtml(result)).andReturn(result);
		EasyMock.replay(htmlUtil);

		final MicroblogConnectorImpl microblogConnectorImpl = new MicroblogConnectorImpl(logger, httpDownloader, httpDownloadUtil, parseUtil, htmlUtil);

		assertEquals(result, microblogConnectorImpl.extractContent(pageContent.toString()));
	}

	@Test
	public void testExtractContent() {
		final StringBuffer pageContent = new StringBuffer();
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

		final HttpDownloader httpDownloader = EasyMock.createNiceMock(HttpDownloader.class);
		EasyMock.replay(httpDownloader);

		final HttpDownloadUtil httpDownloadUtil = EasyMock.createNiceMock(HttpDownloadUtil.class);
		EasyMock.replay(httpDownloadUtil);

		final ParseUtil parseUtil = EasyMock.createMock(ParseUtil.class);
		EasyMock.replay(parseUtil);

		final String result = "Foo \"Bar\"";

		final HtmlUtil htmlUtil = EasyMock.createMock(HtmlUtil.class);
		EasyMock.expect(htmlUtil.filterHtmlTages("Foo<br/><b>&quote;Bar&quote;</b>")).andReturn("Foo &quote;Bar&quote;");
		EasyMock.expect(htmlUtil.unescapeHtml("Foo &quote;Bar&quote;")).andReturn(result);
		EasyMock.replay(htmlUtil);

		final MicroblogConnectorImpl microblogConnectorImpl = new MicroblogConnectorImpl(logger, httpDownloader, httpDownloadUtil, parseUtil, htmlUtil);

		assertEquals(result, microblogConnectorImpl.extractContent(pageContent.toString()));
	}

	@Test
	public void testExtractAuhor() {
		final StringBuffer pageContent = new StringBuffer();
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

		final HttpDownloader httpDownloader = EasyMock.createNiceMock(HttpDownloader.class);
		EasyMock.replay(httpDownloader);

		final HttpDownloadUtil httpDownloadUtil = EasyMock.createNiceMock(HttpDownloadUtil.class);
		EasyMock.replay(httpDownloadUtil);

		final ParseUtil parseUtil = EasyMock.createMock(ParseUtil.class);
		EasyMock.replay(parseUtil);

		final HtmlUtil htmlUtil = EasyMock.createMock(HtmlUtil.class);
		EasyMock.replay(htmlUtil);

		final MicroblogConnectorImpl microblogConnectorImpl = new MicroblogConnectorImpl(logger, httpDownloader, httpDownloadUtil, parseUtil, htmlUtil);
		assertEquals("bgates", microblogConnectorImpl.extractAuhor(pageContent.toString()));
	}

	@Test
	public void testExtractConversationUrl() {
		final StringBuffer pageContent = new StringBuffer();

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

		final HttpDownloader httpDownloader = EasyMock.createNiceMock(HttpDownloader.class);
		EasyMock.replay(httpDownloader);

		final HttpDownloadUtil httpDownloadUtil = EasyMock.createNiceMock(HttpDownloadUtil.class);
		EasyMock.replay(httpDownloadUtil);

		final ParseUtil parseUtil = EasyMock.createMock(ParseUtil.class);
		EasyMock.replay(parseUtil);

		final HtmlUtil htmlUtil = EasyMock.createMock(HtmlUtil.class);
		EasyMock.replay(htmlUtil);

		final MicroblogConnectorImpl microblogConnectorImpl = new MicroblogConnectorImpl(logger, httpDownloader, httpDownloadUtil, parseUtil, htmlUtil);
		assertEquals("https://micro.rp.seibert-media.net/conversation/42#notice-1337", microblogConnectorImpl.extractConversationUrl(pageContent.toString()));
	}

	@Test
	public void testBuildMicroblogConversationResult() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final MicroblogRevisionStorage microblogRevisionStorage = EasyMock.createMock(MicroblogRevisionStorage.class);
		EasyMock.replay(microblogRevisionStorage);

		final MailService mailService = EasyMock.createMock(MailService.class);
		EasyMock.replay(mailService);

		final HttpDownloader httpDownloader = EasyMock.createNiceMock(HttpDownloader.class);
		EasyMock.replay(httpDownloader);

		final HttpDownloadUtil httpDownloadUtil = EasyMock.createNiceMock(HttpDownloadUtil.class);
		EasyMock.replay(httpDownloadUtil);

		final ParseUtil parseUtil = EasyMock.createMock(ParseUtil.class);
		EasyMock.replay(parseUtil);

		final HtmlUtil htmlUtil = EasyMock.createMock(HtmlUtil.class);
		EasyMock.expect(htmlUtil.filterHtmlTages("user1: text1")).andReturn("user1: text1").anyTimes();
		EasyMock.expect(htmlUtil.filterHtmlTages("user2: text2")).andReturn("user2: text2").anyTimes();
		EasyMock.expect(htmlUtil.unescapeHtml("user1: text1")).andReturn("user1: text1").anyTimes();
		EasyMock.expect(htmlUtil.unescapeHtml("user2: text2")).andReturn("user2: text2").anyTimes();
		EasyMock.replay(htmlUtil);

		final StreamUtil streamUtil = new StreamUtil();
		final ResourceUtil resourceUtil = new ResourceUtilImpl(streamUtil);

		final MicroblogConnectorImpl microblogConnectorImpl = new MicroblogConnectorImpl(logger, httpDownloader, httpDownloadUtil, parseUtil, htmlUtil);
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
		assertEquals("user1: text1", result.getPosts().get(1).getContent());
		assertEquals("user1", result.getPosts().get(1).getAuthor());
		assertEquals("https://micro.rp.seibert-media.net/notice/13", result.getPosts().get(1).getPostUrl());
		assertEquals(conversationUrl, result.getPosts().get(1).getConversationUrl());

		assertNotNull(result.getPosts().get(0));
		assertEquals("user2: text2", result.getPosts().get(0).getContent());
		assertEquals("user2", result.getPosts().get(0).getAuthor());
		assertEquals("https://micro.rp.seibert-media.net/notice/14", result.getPosts().get(0).getPostUrl());
		assertEquals(conversationUrl, result.getPosts().get(0).getConversationUrl());

	}
}
