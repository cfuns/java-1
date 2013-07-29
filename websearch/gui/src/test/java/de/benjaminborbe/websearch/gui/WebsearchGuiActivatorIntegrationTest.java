package de.benjaminborbe.websearch.gui;

import com.google.inject.Injector;
import de.benjaminborbe.navigation.api.NavigationEntry;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.tools.osgi.BaseGuiceFilter;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.mock.ExtHttpServiceMock;
import de.benjaminborbe.tools.osgi.test.BundleActivatorTestUtil;
import de.benjaminborbe.websearch.gui.guice.WebsearchGuiModulesMock;
import org.junit.Test;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class WebsearchGuiActivatorIntegrationTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new WebsearchGuiModulesMock());
		final WebsearchGuiActivator activator = injector.getInstance(WebsearchGuiActivator.class);
		assertNotNull(activator);
	}

	@Test
	public void testServlets() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new WebsearchGuiModulesMock());
		final WebsearchGuiActivator activator = new WebsearchGuiActivator() {

			@Override
			public Injector getInjector() {
				return injector;
			}

		};
		final BundleActivatorTestUtil bundleActivatorTestUtil = new BundleActivatorTestUtil();
		final ExtHttpServiceMock extHttpServiceMock = bundleActivatorTestUtil.startBundle(activator);
		final List<String> paths = new ArrayList<String>();
		paths.add("/" + WebsearchGuiConstants.NAME + WebsearchGuiConstants.URL_CLEAR_INDEX);
		paths.add("/" + WebsearchGuiConstants.NAME + WebsearchGuiConstants.URL_EXPIRE);
		paths.add("/" + WebsearchGuiConstants.NAME + WebsearchGuiConstants.URL_EXPIRE_ALL);
		paths.add("/" + WebsearchGuiConstants.NAME + WebsearchGuiConstants.URL_HOME);
		paths.add("/" + WebsearchGuiConstants.NAME + WebsearchGuiConstants.URL_LIST);
		paths.add("/" + WebsearchGuiConstants.NAME + WebsearchGuiConstants.URL_REFRESH);
		paths.add("/" + WebsearchGuiConstants.NAME + WebsearchGuiConstants.URL_REFRESH_PAGE);
		paths.add("/" + WebsearchGuiConstants.NAME + WebsearchGuiConstants.URL_CONFIGURATION_CREATE);
		paths.add("/" + WebsearchGuiConstants.NAME + WebsearchGuiConstants.URL_CONFIGURATION_DELETE);
		paths.add("/" + WebsearchGuiConstants.NAME + WebsearchGuiConstants.URL_CONFIGURATION_UPDATE);
		paths.add("/" + WebsearchGuiConstants.NAME + WebsearchGuiConstants.URL_PAGE_SHOW);
		paths.add("/" + WebsearchGuiConstants.NAME + WebsearchGuiConstants.URL_PAGE_CONTENT);
		paths.add("/" + WebsearchGuiConstants.NAME + WebsearchGuiConstants.URL_SAVE_ALL_IMAGES);

		assertEquals(paths.size(), extHttpServiceMock.getRegisterServletCallCounter());
		for (final String path : paths) {
			assertTrue("no servlet for path " + path + " registered", extHttpServiceMock.hasServletPath(path));
		}
	}

	@Test
	public void testFilters() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new WebsearchGuiModulesMock());
		final WebsearchGuiActivator activator = new WebsearchGuiActivator() {

			@Override
			public Injector getInjector() {
				return injector;
			}

		};

		final BundleActivatorTestUtil bundleActivatorTestUtil = new BundleActivatorTestUtil();
		final ExtHttpServiceMock extHttpServiceMock = bundleActivatorTestUtil.startBundle(activator);
		final List<String> paths = Arrays.asList("/websearch.*");
		assertEquals(paths.size(), extHttpServiceMock.getRegisterFilterCallCounter());

		for (final String path : paths) {
			assertTrue("no filter for path " + path + " registered", extHttpServiceMock.hasFilterPath(path));
		}

		final BaseGuiceFilter guiceFilter = injector.getInstance(BaseGuiceFilter.class);
		for (final Filter filter : Arrays.asList(guiceFilter)) {
			assertTrue("no filter " + filter + " registered", extHttpServiceMock.hasFilter(filter));
		}
	}

	@Test
	public void testResources() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new WebsearchGuiModulesMock());
		final WebsearchGuiActivator activator = new WebsearchGuiActivator() {

			@Override
			public Injector getInjector() {
				return injector;
			}

		};
		final BundleActivatorTestUtil bundleActivatorTestUtil = new BundleActivatorTestUtil();
		final ExtHttpServiceMock extHttpServiceMock = bundleActivatorTestUtil.startBundle(activator);
		final List<String> paths = new ArrayList<String>();
		assertEquals(paths.size(), extHttpServiceMock.getRegisterResourceCallCounter());
		for (final String path : paths) {
			assertTrue("no resource for path " + path + " registered", extHttpServiceMock.hasResource(path));
		}
	}

	@Test
	public void testServices() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new WebsearchGuiModulesMock());
		final WebsearchGuiActivator activator = new WebsearchGuiActivator() {

			@Override
			public Injector getInjector() {
				return injector;
			}

		};

		final BundleActivatorTestUtil bundleActivatorTestUtil = new BundleActivatorTestUtil();
		bundleActivatorTestUtil.startBundle(activator);

		final Collection<ServiceInfo> serviceInfos = activator.getServiceInfos();
		final List<String> names = new ArrayList<String>();
		names.add(NavigationEntry.class.getName());
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
