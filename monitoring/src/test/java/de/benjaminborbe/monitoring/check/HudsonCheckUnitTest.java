package de.benjaminborbe.monitoring.check;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URL;

import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import de.benjaminborbe.monitoring.api.CheckResult;
import de.benjaminborbe.tools.http.HttpDownloadResult;
import de.benjaminborbe.tools.http.HttpDownloadUtil;
import de.benjaminborbe.tools.http.HttpDownloader;
import de.benjaminborbe.tools.util.Encoding;

public class HudsonCheckUnitTest {

	@Test
	public void testCheckStable() throws Exception, IOException {
		final String name = "Hudson-Check on TwentyFeet-UnitTests";
		final String url = "https://hudson.rp.seibert-media.net/";
		final String job = "TwentyFeet-UnitTests";
		final int timeOut = 3000;

		final String content = "bla <tr><td data=\"5\"><a>TwentyFeet-UnitTests</a></td><td data=\"5\"></td></tr> bla";
		final Encoding encoding = new Encoding("UTF8");

		final HttpDownloadResult httpDownloadResult = EasyMock.createMock(HttpDownloadResult.class);
		EasyMock.expect(httpDownloadResult.getContent()).andReturn(content.getBytes()).anyTimes();
		EasyMock.expect(httpDownloadResult.getContentEncoding()).andReturn(encoding).anyTimes();
		EasyMock.replay(httpDownloadResult);

		final HttpDownloader httpDownloader = EasyMock.createMock(HttpDownloader.class);
		EasyMock.expect(httpDownloader.downloadUrlUnsecure(new URL(url), timeOut, null, null)).andReturn(httpDownloadResult);
		EasyMock.replay(httpDownloader);

		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final HttpDownloadUtil httpDownloadUtil = EasyMock.createMock(HttpDownloadUtil.class);
		EasyMock.expect(httpDownloadUtil.getContent(httpDownloadResult)).andReturn(content);
		EasyMock.replay(httpDownloadUtil);

		final HudsonCheck check = new HudsonCheck(logger, httpDownloader, httpDownloadUtil, name, url, job);
		final CheckResult result = check.check();
		assertNotNull(result);
		assertNotNull(result.getCheck());
		assertTrue(result.isSuccess());
		assertEquals("is stable", result.getMessage());
		assertEquals("Hudson check on host https://hudson.rp.seibert-media.net/ for job TwentyFeet-UnitTests", result.getDescription());
		assertEquals("Hudson-Check on TwentyFeet-UnitTests", result.getName());
	}

	@Test
	public void testCheckUnstable() throws Exception, IOException {
		final String name = "Hudson-Check on TwentyFeet-UnitTests";
		final String url = "https://hudson.rp.seibert-media.net/";
		final String job = "TwentyFeet-UnitTests";
		final int timeOut = 3000;

		final String content = "bla <tr><td data=\"5\"><img alt=\"Unstable\" src=\"foo.jpg\">TwentyFeet-UnitTests</a></td><td data=\"5\"></td></tr> bla";
		final Encoding encoding = new Encoding("UTF8");

		final HttpDownloadResult httpDownloadResult = EasyMock.createMock(HttpDownloadResult.class);
		EasyMock.expect(httpDownloadResult.getContent()).andReturn(content.getBytes()).anyTimes();
		EasyMock.expect(httpDownloadResult.getContentEncoding()).andReturn(encoding).anyTimes();
		EasyMock.replay(httpDownloadResult);

		final HttpDownloader httpDownloader = EasyMock.createMock(HttpDownloader.class);
		EasyMock.expect(httpDownloader.downloadUrlUnsecure(new URL(url), timeOut, null, null)).andReturn(httpDownloadResult);
		EasyMock.replay(httpDownloader);

		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final HttpDownloadUtil httpDownloadUtil = EasyMock.createMock(HttpDownloadUtil.class);
		EasyMock.expect(httpDownloadUtil.getContent(httpDownloadResult)).andReturn(content);
		EasyMock.replay(httpDownloadUtil);

		final HudsonCheck check = new HudsonCheck(logger, httpDownloader, httpDownloadUtil, name, url, job);
		final CheckResult result = check.check();
		assertNotNull(result);
		assertNotNull(result.getCheck());
		assertFalse(result.isSuccess());
		assertEquals("is unstable", result.getMessage());
		assertEquals("Hudson check on host https://hudson.rp.seibert-media.net/ for job TwentyFeet-UnitTests", result.getDescription());
		assertEquals("Hudson-Check on TwentyFeet-UnitTests", result.getName());
	}
}
