package de.benjaminborbe.googlesearch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;

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
		assertEquals(0, extHttpServiceMock.getRegisterResourceCallCounter());
		// for (final String path : Arrays.asList("/search/css", "/search/js")) {
		// assertTrue("no resource for path " + path + " registered",
		// extHttpServiceMock.hasResource(path));
		// }
	}

	public void testServices() {
		final GooglesearchActivator googlesearchActivator = new GooglesearchActivator();
		final Collection<ServiceInfo> serviceInfos = googlesearchActivator.getServiceInfos();
		assertEquals(0, serviceInfos.size());
	}

}
