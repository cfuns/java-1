package de.benjaminborbe.tools.http;

import com.google.inject.Injector;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.tools.guice.ToolModules;
import de.benjaminborbe.tools.stream.ChannelTools;
import de.benjaminborbe.tools.stream.StreamUtil;
import de.benjaminborbe.tools.util.Base64Util;
import de.benjaminborbe.tools.util.Base64UtilImpl;
import de.benjaminborbe.tools.util.Duration;
import de.benjaminborbe.tools.util.DurationUtil;
import org.easymock.EasyMock;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class HttpDownloaderImplIntegrationTest {

	private static final int TIMEOUT = 3000;

	private static boolean notFound = true;

	@BeforeClass
	public static void setUp() {
		final Socket socket = new Socket();
		final SocketAddress endpoint = new InetSocketAddress("www.google.de", 80);
		try {
			socket.connect(endpoint, 500);
			notFound = !socket.isConnected();
			notFound = false;
		} catch (final IOException e) {
			notFound = true;
		} finally {
			try {
				socket.close();
			} catch (final IOException e) {
			}
		}
	}

	@Test
	public void testdownloadUrl() throws Exception {
		if (notFound)
			return;

		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final StreamUtil streamUtil = new StreamUtil(new ChannelTools());

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
		if (notFound)
			return;

		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final StreamUtil streamUtil = new StreamUtil(new ChannelTools());

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
			} catch (final HttpDownloaderException e) {
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
			} catch (final HttpDownloaderException e) {
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
		if (notFound)
			return;

		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final StreamUtil streamUtil = new StreamUtil(new ChannelTools());

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
		if (notFound)
			return;

		final Injector injector = GuiceInjectorBuilder.getInjector(new ToolModules());
		final HttpDownloader httpDownloader = injector.getInstance(HttpDownloader.class);
		final HttpDownloadUtil httpDownloadUtil = injector.getInstance(HttpDownloadUtil.class);
		final HttpDownloadResult result = httpDownloader.getUrl(new URL("http://www.spiegel.de/netzwelt/web/29c3-was-hacker-auf-einem-kongress-alles-machen-a-875161.html"), 5000);
		assertNotNull(result);

		assertThat(result.getContentEncoding().getEncoding(), is("ISO-8859-1"));
		assertThat(result.getResponseCode(), is(200));

		final String content = httpDownloadUtil.getContent(result);
		assertThat(content, is(notNullValue()));
		assertThat(content.isEmpty(), is(false));
		assertThat(content.contains("für"), is(true));
	}

	@Test
	public void testEncodingMeta() throws Exception {
		if (notFound)
			return;

		final Injector injector = GuiceInjectorBuilder.getInjector(new ToolModules());
		final HttpDownloader httpDownloader = injector.getInstance(HttpDownloader.class);
		final HttpDownloadUtil httpDownloadUtil = injector.getInstance(HttpDownloadUtil.class);
		final HttpDownloadResult result = httpDownloader.getUrl(new URL("http://www.seibert-media.net/unternehmen/index.shtml"), 5000);
		assertThat(result, is(notNullValue()));
		assertThat(result.getResponseCode(), is(200));

		// assertThat(result.getContentEncoding(), is(notNullValue()));
		// assertThat(result.getContentEncoding().getEncoding(), is("ISO"));
		final String content = httpDownloadUtil.getContent(result);
		assertThat(content, is(notNullValue()));
		assertThat(content.isEmpty(), is(false));
		assertTrue(content.contains("für"));
	}

	@Test
	public void testNotFoundException() throws Exception {
		if (notFound)
			return;

		final Injector injector = GuiceInjectorBuilder.getInjector(new ToolModules());
		final HttpDownloader httpDownloader = injector.getInstance(HttpDownloader.class);
		final HttpDownloadResult result = httpDownloader.getUrl(new URL("http://www.google.de/gibtsned"), 5000);
		assertNotNull(result);
		assertThat(result.getResponseCode(), is(404));
	}
}
