package de.benjaminborbe.vnc.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.tools.thread.Assert;
import de.benjaminborbe.tools.thread.ThreadUtil;
import de.benjaminborbe.vnc.api.VncKey;
import de.benjaminborbe.vnc.api.VncScreenContent;
import de.benjaminborbe.vnc.api.VncService;
import de.benjaminborbe.vnc.api.VncServiceException;
import de.benjaminborbe.vnc.guice.VncModulesMock;

public class VncServiceImplIntegrationTest {

	@Test
	public void testInject() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new VncModulesMock());
		final VncService vncService = injector.getInstance(VncService.class);
		assertNotNull(vncService);
		assertEquals(VncServiceImpl.class, vncService.getClass());
	}

	@Test
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

	@Test
	public void testGetWidth() throws Exception {
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

	@Test
	public void testGetHeight() throws Exception {
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

	@Test
	public void testMouse() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new VncModulesMock());
		final VncService vncService = injector.getInstance(VncService.class);
		final ThreadUtil threadUtil = injector.getInstance(ThreadUtil.class);
		try {
			vncService.connect();

			final int x = 20;
			final int y = 10;
			vncService.mouseMouse(x, y);
			threadUtil.wait(1000, new Assert() {

				@Override
				public boolean calc() {
					try {
						return x == vncService.getScreenContent().getPointerLocation().getX() && y == vncService.getScreenContent().getPointerLocation().getY();
					}
					catch (final VncServiceException e) {
						return false;
					}
				}
			});
			assertEquals(x, vncService.getScreenContent().getPointerLocation().getX());
			assertEquals(y, vncService.getScreenContent().getPointerLocation().getY());
		}
		finally {
			vncService.disconnect();
		}
	}
}
