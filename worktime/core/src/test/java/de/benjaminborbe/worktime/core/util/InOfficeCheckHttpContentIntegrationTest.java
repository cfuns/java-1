package de.benjaminborbe.worktime.core.util;

import com.google.inject.Injector;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.worktime.core.guice.WorktimeModulesMock;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class InOfficeCheckHttpContentIntegrationTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new WorktimeModulesMock());
		assertNotNull(injector.getInstance(InOfficeCheckHttpContent.class));
	}

	@Ignore
	@Test
	public void testCheck() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new WorktimeModulesMock());
		final InOfficeCheckHttpContent inOfficeCheckTcpConnect = injector.getInstance(InOfficeCheckHttpContent.class);
		assertTrue(inOfficeCheckTcpConnect.check());
	}
}
