package de.benjaminborbe.worktime.util;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.worktime.guice.WorktimeModulesMock;

public class InOfficeCheckTcpConnectIntegrationTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new WorktimeModulesMock());
		assertNotNull(injector.getInstance(InOfficeCheckTcpConnect.class));
	}

	@Ignore
	@Test
	public void testCheck() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new WorktimeModulesMock());
		final InOfficeCheckTcpConnect inOfficeCheckTcpConnect = injector.getInstance(InOfficeCheckTcpConnect.class);
		assertTrue(inOfficeCheckTcpConnect.check());
	}

}
