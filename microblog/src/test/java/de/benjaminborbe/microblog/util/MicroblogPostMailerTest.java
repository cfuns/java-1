package de.benjaminborbe.microblog.util;

import static org.junit.Assert.assertEquals;

import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import de.benjaminborbe.mail.api.MailService;
import de.benjaminborbe.microblog.util.MicroblogConnector;
import de.benjaminborbe.microblog.util.MicroblogPostMailerImpl;
import de.benjaminborbe.microblog.util.MicroblogRevisionStorage;
import de.benjaminborbe.tools.http.HttpDownloadUtil;
import de.benjaminborbe.tools.http.HttpDownloader;

public class MicroblogPostMailerTest {

	@Test
	public void testExtractContent() {
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

		final MicroblogPostMailerImpl microblogPostMailer = new MicroblogPostMailerImpl(logger, mailService, httpDownloader, httpDownloadUtil);

		final StringBuffer pageContent = new StringBuffer();
		pageContent.append("<h1>Bill Gates (bgates)'s status on Thursday, 19-Jan-12 12:39:37 CET</h1>");
		pageContent.append("<div id=\"content_inner\">");
		pageContent.append("<ol class=\"notices xoxo\">");
		pageContent.append("<li class=\"hentry notice notice-source-web\" id=\"notice-1337\">");
		pageContent.append("<div class=\"entry-title\">");
		pageContent.append("<div class=\"author\">");
		pageContent.append("<span class=\"vcard author\">");
		pageContent.append("<a href=\"https://micro.rp.seibert-media.net/kkutter\" class=\"url\" title=\"kkutter\">");
		pageContent.append("<img src=\"https://micro.rp.seibert-media.net/avatar/24-48-1231231238.jpeg\" class=\"avatar photo\" alt=\"Bill Gates\" height=\"48\" width=\"48\">");
		pageContent.append("<span class=\"fn\">Foo Bar</span>");
		pageContent.append("</a>");
		pageContent.append("</span>");
		pageContent.append("<span class=\"addressees\">");
		pageContent.append("<a href=\"https://micro.rp.seibert-media.net/group/design\" title=\"design\" class=\"addressee group\">DESIGN</a>");
		pageContent.append("</span>");
		pageContent.append("</div>");
		pageContent.append("<p class=\"entry-content\">Foo<br/><b>Bar</b></p>");
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

		assertEquals("Foo Bar", microblogPostMailer.extractContent(pageContent.toString()));

	}
}
