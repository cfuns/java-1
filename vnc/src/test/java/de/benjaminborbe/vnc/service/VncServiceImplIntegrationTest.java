package de.benjaminborbe.vnc.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.vnc.api.VncKey;
import de.benjaminborbe.vnc.api.VncScreenContent;
import de.benjaminborbe.vnc.api.VncService;
import de.benjaminborbe.vnc.guice.VncModulesMock;

public class VncServiceImplIntegrationTest {

	// @Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new VncModulesMock());
		final VncService vncService = injector.getInstance(VncService.class);
		assertNotNull(vncService);
		assertEquals(VncServiceImpl.class, vncService.getClass());
	}

	// @Test
	public void testConnect() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new VncModulesMock());
		final VncService vncService = injector.getInstance(VncService.class);
		try {
			vncService.connect();
		}
		finally {
			vncService.disconnect();
		}
	}

	// @Test
	public void testGetWidth() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new VncModulesMock());
		final VncService vncService = injector.getInstance(VncService.class);
		try {
			vncService.connect();

			final VncScreenContent vncScreenContent = vncService.getScreenContent();
			assertNotNull(vncScreenContent);
			assertTrue(vncScreenContent.getWidth() > 0);
		}
		finally {
			vncService.disconnect();
		}
	}

	// @Test
	public void testGetHeight() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new VncModulesMock());
		final VncService vncService = injector.getInstance(VncService.class);
		try {
			vncService.connect();

			final VncScreenContent vncScreenContent = vncService.getScreenContent();
			assertNotNull(vncScreenContent);
			assertTrue(vncScreenContent.getHeight() > 0);
		}
		finally {
			vncService.disconnect();
		}
	}

	@Test
	public void testKey() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new VncModulesMock());
		final VncService vncService = injector.getInstance(VncService.class);
		try {
			vncService.connect();
			for (final VncKey vncKey : VncKey.values()) {
				vncService.keyPress(vncKey);
				vncService.keyRelease(vncKey);
			}
		}
		finally {
			vncService.disconnect();
		}
	}
}
