package de.benjaminborbe.tools.http;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.URL;

import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import com.google.inject.Injector;

import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.tools.guice.ToolModules;
import de.benjaminborbe.tools.util.Base64Util;
import de.benjaminborbe.tools.util.Base64UtilImpl;
import de.benjaminborbe.tools.util.Duration;
import de.benjaminborbe.tools.util.DurationUtil;
import de.benjaminborbe.tools.util.StreamUtil;

public class HttpDownloaderImplIntegrationTest {

	private static final int TIMEOUT = 3000;

	@Test
	public void testdownloadUrl() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final StreamUtil streamUtil = new StreamUtil();

		final Duration duration = EasyMock.createMock(Duration.class);
		EasyMock.expect(duration.getTime()).andReturn(1337l).anyTimes();
		EasyMock.replay(duration);

		final DurationUtil durationUtil = EasyMock.createMock(DurationUtil.class);
		EasyMock.expect(durationUtil.getDuration()).andReturn(duration).anyTimes();
		EasyMock.replay(durationUtil);

		final Base64Util base64Util = new Base64UtilImpl();

		final HttpDownloader httpDownloader = new HttpDownloaderImpl(logger, streamUtil, durationUtil, base64Util);
		final URL url = new URL("http://www.google.de");
		{
			final HttpDownloadResult result = httpDownloader.getUrl(url, TIMEOUT);
			assertTrue(result.getDuration() > 0);
			assertNotNull(result.getContent());
		}
		{
			final HttpDownloadResult result = httpDownloader.getUrlUnsecure(url, TIMEOUT);
			assertTrue(result.getDuration() > 0);
			assertNotNull(result.getContent());
		}
	}

	@Test
	public void testdownloadUrlAuth() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final StreamUtil streamUtil = new StreamUtil();

		final Duration duration = EasyMock.createMock(Duration.class);
		EasyMock.expect(duration.getTime()).andReturn(1337l).anyTimes();
		EasyMock.replay(duration);

		final DurationUtil durationUtil = EasyMock.createMock(DurationUtil.class);
		EasyMock.expect(durationUtil.getDuration()).andReturn(duration).anyTimes();
		EasyMock.replay(durationUtil);

		final Base64Util base64Util = new Base64UtilImpl();
		final HttpDownloader httpDownloader = new HttpDownloaderImpl(logger, streamUtil, durationUtil, base64Util);
		final URL url = new URL("http://htaccesstest.benjamin-borbe.de/index.html");

		{
			try {
				httpDownloader.getUrl(url, TIMEOUT);
				fail("exception expected");
			}
			catch (final HttpDownloaderException e) {
				assertNotNull(e);
			}

			final String username = "test";
			final String password = "test";
			final HttpDownloadResult result = httpDownloader.getUrl(url, TIMEOUT, username, password);
			assertTrue(result.getDuration() > 0);
			assertNotNull(result.getContent());
		}

		{
			try {
				httpDownloader.getUrlUnsecure(url, TIMEOUT);
				fail("exception expected");
			}
			catch (final HttpDownloaderException e) {
				assertNotNull(e);
			}

			final String username = "test";
			final String password = "test";
			final HttpDownloadResult result = httpDownloader.getUrlUnsecure(url, TIMEOUT, username, password);
			assertTrue(result.getDuration() > 0);
			assertNotNull(result.getContent());
		}
	}

	@Test
	public void testdownloadUrlAuthUrl() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final StreamUtil streamUtil = new StreamUtil();

		final Duration duration = EasyMock.createMock(Duration.class);
		EasyMock.expect(duration.getTime()).andReturn(1337l).anyTimes();
		EasyMock.replay(duration);

		final DurationUtil durationUtil = EasyMock.createMock(DurationUtil.class);
		EasyMock.expect(durationUtil.getDuration()).andReturn(duration).anyTimes();
		EasyMock.replay(durationUtil);

		final Base64Util base64Util = new Base64UtilImpl();
		final HttpDownloader httpDownloader = new HttpDownloaderImpl(logger, streamUtil, durationUtil, base64Util);
		final URL url = new URL("http://test:test@htaccesstest.benjamin-borbe.de/index.html");

		{
			final HttpDownloadResult result = httpDownloader.getUrl(url, TIMEOUT);
			assertTrue(result.getDuration() > 0);
			assertNotNull(result.getContent());
		}

		{
			final HttpDownloadResult result = httpDownloader.getUrlUnsecure(url, TIMEOUT);
			assertTrue(result.getDuration() > 0);
			assertNotNull(result.getContent());
		}
	}

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new ToolModules());
		final HttpDownloader httpDownloader = injector.getInstance(HttpDownloader.class);
		assertNotNull(httpDownloader);
		assertEquals(HttpDownloaderImpl.class, httpDownloader.getClass());
	}

	@Test
	public void testEncodingContentType() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new ToolModules());
		final HttpDownloader httpDownloader = injector.getInstance(HttpDownloader.class);
		final HttpDownloadUtil httpDownloadUtil = injector.getInstance(HttpDownloadUtil.class);
		final HttpDownloadResult result = httpDownloader.getUrl(new URL("http://www.spiegel.de/netzwelt/web/29c3-was-hacker-auf-einem-kongress-alles-machen-a-875161.html"), 5000);
		assertNotNull(result);
		final String content = httpDownloadUtil.getContent(result);
		assertNotNull(content);
		assertTrue(content.contains("für"));
	}

	@Test
	public void testEncodingMeta() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new ToolModules());
		final HttpDownloader httpDownloader = injector.getInstance(HttpDownloader.class);
		final HttpDownloadUtil httpDownloadUtil = injector.getInstance(HttpDownloadUtil.class);
		final HttpDownloadResult result = httpDownloader.getUrl(new URL("http://www.seibert-media.net/unternehmen/index.shtml"), 5000);
		assertNotNull(result);
		final String content = httpDownloadUtil.getContent(result);
		assertNotNull(content);
		assertTrue(content.contains("für"));
	}
}
