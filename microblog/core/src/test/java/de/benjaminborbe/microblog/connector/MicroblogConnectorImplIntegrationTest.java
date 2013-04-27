package de.benjaminborbe.microblog.connector;

import com.google.inject.Injector;
import de.benjaminborbe.microblog.api.MicroblogPostIdentifier;
import de.benjaminborbe.microblog.guice.MicroblogModulesMock;
import de.benjaminborbe.microblog.post.MicroblogPostResult;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MicroblogConnectorImplIntegrationTest {

	private static boolean notFound;

	@BeforeClass
	public static void setUp() {
		final Socket socket = new Socket();
		final SocketAddress endpoint = new InetSocketAddress("micro.rp.seibert-media.net", 443);
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
	public void testGetPost() throws Exception {
		if (notFound)
			return;
		final Injector injector = GuiceInjectorBuilder.getInjector(new MicroblogModulesMock());
		final MicroblogConnector microblogConnector = injector.getInstance(MicroblogConnector.class);
		final CalendarUtil calendarUtil = injector.getInstance(CalendarUtil.class);

		final MicroblogPostIdentifier microblogPostIdentifier = new MicroblogPostIdentifier(31697l);
		final MicroblogPostResult post = microblogConnector.getPost(microblogPostIdentifier);
		assertNotNull(post);
		assertNotNull(post.getDate());
		assertEquals("2013-02-22 08:22:33", calendarUtil.toDateTimeString(post.getDate()));
	}
}
