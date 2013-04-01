package de.benjaminborbe.loggly.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.loggly.guice.LogglyModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class LogglyConnectorImplIntegrationTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new LogglyModulesMock());
		final LogglyConnector logglyConnector = injector.getInstance(LogglyConnector.class);
		assertNotNull(logglyConnector);
		assertEquals(LogglyConnectorImpl.class, logglyConnector.getClass());
	}

	@Test
	public void testLog() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new LogglyModulesMock());
		final LogglyConnector logglyConnector = injector.getInstance(LogglyConnector.class);

		logglyConnector.debug("hello world - " + new Date().getTime());
	}
}
