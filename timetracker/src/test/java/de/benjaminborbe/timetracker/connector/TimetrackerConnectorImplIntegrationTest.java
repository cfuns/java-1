package de.benjaminborbe.timetracker.connector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.timetracker.connector.TimetrackerConnector;
import de.benjaminborbe.timetracker.connector.TimetrackerConnectorImpl;
import de.benjaminborbe.timetracker.guice.TimetrackerModulesMock;
import de.benjaminborbe.tools.date.DateUtil;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class TimetrackerConnectorImplIntegrationTest {

	private final String username = "xxx";

	private final String password = "xxx";

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new TimetrackerModulesMock());
		final TimetrackerConnector connector = injector.getInstance(TimetrackerConnector.class);
		assertNotNull(connector);
		assertEquals(TimetrackerConnectorImpl.class, connector.getClass());
	}

	@Ignore
	@Test
	public void testGetSessionId() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new TimetrackerModulesMock());
		final TimetrackerConnectorImpl connector = injector.getInstance(TimetrackerConnectorImpl.class);
		final String sessionId = connector.getSessionId(username, password);
		assertNotNull(sessionId);
	}

	@Ignore
	@Test
	public void testIsCompleted() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new TimetrackerModulesMock());
		final TimetrackerConnectorImpl connector = injector.getInstance(TimetrackerConnectorImpl.class);
		final DateUtil dateUtil = injector.getInstance(DateUtil.class);
		final String sessionId = connector.getSessionId(username, password);
		assertNotNull(sessionId);
		assertFalse(connector.isCompleted(sessionId, dateUtil.parseDate("2012-07-28")));
		assertTrue(connector.isCompleted(sessionId, dateUtil.parseDate("2012-07-29")));
	}
}
