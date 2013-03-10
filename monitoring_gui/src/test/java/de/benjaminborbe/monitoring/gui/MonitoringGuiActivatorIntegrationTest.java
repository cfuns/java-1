package de.benjaminborbe.monitoring.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.servlet.Filter;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.monitoring.gui.guice.MonitoringGuiModulesMock;
import de.benjaminborbe.navigation.api.NavigationEntry;
import de.benjaminborbe.tools.osgi.test.BundleActivatorTestUtil;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.tools.osgi.BaseGuiceFilter;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.mock.ExtHttpServiceMock;

public class MonitoringGuiActivatorIntegrationTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new MonitoringGuiModulesMock());
		final MonitoringGuiActivator activator = injector.getInstance(MonitoringGuiActivator.class);
		assertNotNull(activator);
	}

	@Test
	public void testServlets() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new MonitoringGuiModulesMock());
		final MonitoringGuiActivator activator = new MonitoringGuiActivator() {

			@Override
			public Injector getInjector() {
				return injector;
			}

		};
		final BundleActivatorTestUtil bundleActivatorTestUtil = new BundleActivatorTestUtil();
		final ExtHttpServiceMock extHttpServiceMock = bundleActivatorTestUtil.startBundle(activator);
		final List<String> paths = new ArrayList<String>();
		paths.add("/" + MonitoringGuiConstants.NAME + MonitoringGuiConstants.URL_HOME);
		paths.add("/" + MonitoringGuiConstants.NAME + MonitoringGuiConstants.URL_NODE_CHECK);
		paths.add("/" + MonitoringGuiConstants.NAME + MonitoringGuiConstants.URL_NODE_LIST);
		paths.add("/" + MonitoringGuiConstants.NAME + MonitoringGuiConstants.URL_NODE_LIST_JSON);
		paths.add("/" + MonitoringGuiConstants.NAME + MonitoringGuiConstants.URL_NODE_JSON);
		paths.add("/" + MonitoringGuiConstants.NAME + MonitoringGuiConstants.URL_NODE_SILENT);
		paths.add("/" + MonitoringGuiConstants.NAME + MonitoringGuiConstants.URL_NODE_VIEW);
		paths.add("/" + MonitoringGuiConstants.NAME + MonitoringGuiConstants.URL_NODE_UNSILENT);
		paths.add("/" + MonitoringGuiConstants.NAME + MonitoringGuiConstants.URL_NODE_CREATE);
		paths.add("/" + MonitoringGuiConstants.NAME + MonitoringGuiConstants.URL_NODE_DELETE);
		paths.add("/" + MonitoringGuiConstants.NAME + MonitoringGuiConstants.URL_NODE_UPDATE);
		paths.add("/" + MonitoringGuiConstants.NAME + MonitoringGuiConstants.URL_TRIGGER_CHECK);
		paths.add("/" + MonitoringGuiConstants.NAME + MonitoringGuiConstants.URL_TRIGGER_MAIL);
		assertEquals(paths.size(), extHttpServiceMock.getRegisterServletCallCounter());
		for (final String path : paths) {
			assertTrue("no servlet for path " + path + " registered", extHttpServiceMock.hasServletPath(path));
		}
	}

	@Test
	public void testFilters() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new MonitoringGuiModulesMock());
		final MonitoringGuiActivator activator = new MonitoringGuiActivator() {

			@Override
			public Injector getInjector() {
				return injector;
			}

		};

		final BundleActivatorTestUtil bundleActivatorTestUtil = new BundleActivatorTestUtil();
		final ExtHttpServiceMock extHttpServiceMock = bundleActivatorTestUtil.startBundle(activator);
		final List<String> paths = Arrays.asList("/monitoring.*");
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
		final Injector injector = GuiceInjectorBuilder.getInjector(new MonitoringGuiModulesMock());
		final MonitoringGuiActivator activator = new MonitoringGuiActivator() {

			@Override
			public Injector getInjector() {
				return injector;
			}

		};
		final BundleActivatorTestUtil bundleActivatorTestUtil = new BundleActivatorTestUtil();
		final ExtHttpServiceMock extHttpServiceMock = bundleActivatorTestUtil.startBundle(activator);
		final List<String> paths = Arrays.asList("/monitoring/css");
		assertEquals(paths.size(), extHttpServiceMock.getRegisterResourceCallCounter());
		for (final String path : paths) {
			assertTrue("no resource for path " + path + " registered", extHttpServiceMock.hasResource(path));
		}
	}

	@Test
	public void testServices() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new MonitoringGuiModulesMock());
		final MonitoringGuiActivator activator = new MonitoringGuiActivator() {

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
