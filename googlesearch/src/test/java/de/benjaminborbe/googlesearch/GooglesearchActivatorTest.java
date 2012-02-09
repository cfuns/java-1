package de.benjaminborbe.googlesearch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.googlesearch.GooglesearchActivator;
import de.benjaminborbe.googlesearch.guice.GooglesearchModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.mock.ExtHttpServiceMock;
import de.benjaminborbe.tools.osgi.test.BundleActivatorTestUtil;

public class GooglesearchActivatorTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new GooglesearchModulesMock());
		final GooglesearchActivator o = injector.getInstance(GooglesearchActivator.class);
		assertNotNull(o);
	}

	@Test
	public void testResources() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new GooglesearchModulesMock());
		final GooglesearchActivator o = new GooglesearchActivator() {

			@Override
			public Injector getInjector() {
				return injector;
			}

		};
		final BundleActivatorTestUtil bundleActivatorTestUtil = new BundleActivatorTestUtil();
		final ExtHttpServiceMock extHttpServiceMock = bundleActivatorTestUtil.startBundle(o);
		final List<String> paths = Arrays.asList();
		assertEquals(paths.size(), extHttpServiceMock.getRegisterResourceCallCounter());
		for (final String path : paths) {
			assertTrue("no resource for path " + path + " registered", extHttpServiceMock.hasResource(path));
		}

	}

	public void testServices() {
		final GooglesearchActivator googlesearchActivator = new GooglesearchActivator();
		final Collection<ServiceInfo> serviceInfos = googlesearchActivator.getServiceInfos();
		assertEquals(0, serviceInfos.size());
	}

}
