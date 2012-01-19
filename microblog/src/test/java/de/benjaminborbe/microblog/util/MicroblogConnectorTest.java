package de.benjaminborbe.microblog.util;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URL;

import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import de.benjaminborbe.tools.http.HttpDownloadResult;
import de.benjaminborbe.tools.http.HttpDownloadUtil;
import de.benjaminborbe.tools.http.HttpDownloader;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;

public class MicroblogConnectorTest {

	@Test
	public void testParseLatestRevision() throws Exception, IOException, ParseException {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final HttpDownloadResult httpDownloadResult = EasyMock.createMock(HttpDownloadResult.class);
		EasyMock.replay(httpDownloadResult);

		final HttpDownloader httpDownloader = EasyMock.createMock(HttpDownloader.class);
		EasyMock.expect(httpDownloader.downloadUrlUnsecure(new URL("https://micro.rp.seibert-media.net/api/statuses/friends_timeline/bborbe.rss"), 5000)).andReturn(httpDownloadResult);
		EasyMock.replay(httpDownloader);

		final long rev = 1337l;
		final String content = "bla <guid>https://micro.rp.seibert-media.net/notice/" + rev + "</guid> bla";

		final HttpDownloadUtil httpDownloadUtil = EasyMock.createMock(HttpDownloadUtil.class);
		EasyMock.expect(httpDownloadUtil.getContent(httpDownloadResult)).andReturn(content);
		EasyMock.replay(httpDownloadUtil);

		final ParseUtil parseUtil = EasyMock.createMock(ParseUtil.class);
		EasyMock.expect(parseUtil.parseLong(String.valueOf(rev))).andReturn(rev);
		EasyMock.replay(parseUtil);

		final MicroblogConnector microblogConnector = new MicroblogConnectorImpl(logger, httpDownloader, httpDownloadUtil, parseUtil);
		assertEquals(rev, microblogConnector.getLatestRevision());
	}
}
