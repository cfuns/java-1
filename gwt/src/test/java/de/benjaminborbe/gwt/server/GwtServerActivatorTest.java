package de.benjaminborbe.gwt.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.gwt.server.guice.GwtServerModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.tools.osgi.mock.ExtHttpServiceMock;
import de.benjaminborbe.tools.osgi.test.BundleActivatorTestUtil;

public class GwtServerActivatorTest {

	@Test
	public void testinject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new GwtServerModulesMock());
		final GwtServerActivator o = injector.getInstance(GwtServerActivator.class);
		assertNotNull(o);
	}

	@Test
	public void testResources() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new GwtServerModulesMock());
		final GwtServerActivator o = new GwtServerActivator() {

			@Override
			public Injector getInjector() {
				return injector;
			}

		};
		final BundleActivatorTestUtil bundleActivatorTestUtil = new BundleActivatorTestUtil();
		final ExtHttpServiceMock extHttpServiceMock = bundleActivatorTestUtil.startBundle(o);
		assertEquals(1, extHttpServiceMock.getRegisterResourceCallCounter());
		for (final String path : Arrays.asList("/gwt/Home")) {
			assertTrue("no resource for path " + path + " registered", extHttpServiceMock.hasResource(path));
		}
	}
}
