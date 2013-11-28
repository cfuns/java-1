package de.benjaminborbe.microblog.test;

import de.benjaminborbe.microblog.api.MicroblogPost;
import de.benjaminborbe.microblog.api.MicroblogPostIdentifier;
import de.benjaminborbe.microblog.api.MicroblogService;
import de.benjaminborbe.test.osgi.TestCaseOsgi;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.CalendarUtilImpl;
import de.benjaminborbe.tools.date.CurrentTime;
import de.benjaminborbe.tools.date.CurrentTimeImpl;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.date.TimeZoneUtilImpl;
import de.benjaminborbe.tools.osgi.mock.ExtHttpServiceMock;
import de.benjaminborbe.tools.stream.ChannelTools;
import de.benjaminborbe.tools.url.UrlUtilImpl;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.util.ParseUtilImpl;
import org.apache.felix.http.api.ExtHttpService;
import org.easymock.EasyMock;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

public class MicroblogIntegrationTest extends TestCaseOsgi {

	private static boolean notFound;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		try {
			URL website = new URL("https://micro.rp.seibert-media.net");
			ReadableByteChannel rbc = Channels.newChannel(website.openStream());
			OutputStream fos = new ByteArrayOutputStream();
			WritableByteChannel out = Channels.newChannel(fos);
			new ChannelTools().fastChannelCopy(rbc, out);
			notFound = false;
		} catch (final Exception e) {
			notFound = true;
		} finally {
		}
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetExtHttpService() {
		final BundleContext bundleContext = getContext();
		assertNotNull(bundleContext);
		final ExtHttpServiceMock extHttpService = new ExtHttpServiceMock(new UrlUtilImpl());
		assertNotNull(extHttpService);
		// zum start: keine Dienste registriert
		assertEquals(0, extHttpService.getRegisterFilterCallCounter());
		assertEquals(0, extHttpService.getRegisterServletCallCounter());
		assertEquals(0, extHttpService.getUnregisterFilterCallCounter());
		assertEquals(0, extHttpService.getUnregisterServletCallCounter());
		final ServiceRegistration serviceRegistration = bundleContext.registerService(ExtHttpService.class.getName(), extHttpService, null);
		assertNotNull(serviceRegistration);
		// nach start: Dienste vorhanden?
		assertTrue("no filters registered", extHttpService.getRegisterFilterCallCounter() > 0);
		assertTrue("no servlets registered.", extHttpService.getRegisterServletCallCounter() > 0);
		assertEquals(0, extHttpService.getUnregisterFilterCallCounter());
		assertEquals(0, extHttpService.getUnregisterServletCallCounter());

		// do unregister
		serviceRegistration.unregister();

		assertTrue("no servlets unregistered", extHttpService.getUnregisterServletCallCounter() > 0);
		assertEquals(extHttpService.getRegisterServletCallCounter(), extHttpService.getRegisterServletCallCounter());
		assertEquals(extHttpService.getRegisterFilterCallCounter(), extHttpService.getUnregisterFilterCallCounter());
	}

	public void testGetPost() throws Exception {
		if (notFound)
			return;
		final MicroblogService microblogService = getService(MicroblogService.class);
		final MicroblogPostIdentifier microblogPostIdentifier = new MicroblogPostIdentifier(31697l);
		final MicroblogPost post = microblogService.getPost(microblogPostIdentifier);
		assertNotNull(post);
		assertNotNull(post.getDate());

		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final TimeZoneUtil timeZoneUtil = new TimeZoneUtilImpl();
		final CurrentTime currentTime = new CurrentTimeImpl();
		final ParseUtil parseUtil = new ParseUtilImpl();
		final CalendarUtil calendarUtil = new CalendarUtilImpl(logger, currentTime, parseUtil, timeZoneUtil);

		assertEquals("2013-02-22 08:22:33", calendarUtil.toDateTimeString(post.getDate()));
	}
}
