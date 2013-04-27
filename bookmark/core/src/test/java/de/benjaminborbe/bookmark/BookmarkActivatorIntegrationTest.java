package de.benjaminborbe.bookmark;

import com.google.inject.Injector;
import de.benjaminborbe.bookmark.api.BookmarkService;
import de.benjaminborbe.bookmark.guice.BookmarkModulesMock;
import de.benjaminborbe.search.api.SearchServiceComponent;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.test.BundleActivatorTestUtil;
import org.apache.felix.http.api.ExtHttpService;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BookmarkActivatorIntegrationTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new BookmarkModulesMock());
		final BookmarkActivator activator = injector.getInstance(BookmarkActivator.class);
		assertNotNull(activator);
	}

	@Test
	public void testServices() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new BookmarkModulesMock());
		final BookmarkActivator activator = new BookmarkActivator() {

			@Override
			public Injector getInjector() {
				return injector;
			}

		};

		final BundleActivatorTestUtil bundleActivatorTestUtil = new BundleActivatorTestUtil();
		bundleActivatorTestUtil.startBundle(activator);

		final Collection<ServiceInfo> serviceInfos = activator.getServiceInfos();
		final List<String> names = Arrays.asList(ExtHttpService.class.getName(), SearchServiceComponent.class.getName(), SearchServiceComponent.class.getName(),
			BookmarkService.class.getName());
		assertEquals(names.size(), serviceInfos.size());
		for (final String name : names) {
			boolean match = false;
			for (final ServiceInfo serviceInfo : serviceInfos) {
				if (name.equals(serviceInfo.getName())) {
					match = true;
				}
			}
			assertTrue("no service with name: " + name + " found", match);
		}
	}
}
