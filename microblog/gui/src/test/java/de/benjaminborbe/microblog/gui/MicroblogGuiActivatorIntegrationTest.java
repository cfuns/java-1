package de.benjaminborbe.microblog.gui;

import com.google.inject.Injector;
import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.dashboard.api.DashboardContentWidget;
import de.benjaminborbe.microblog.gui.guice.MicroblogGuiModulesMock;
import de.benjaminborbe.navigation.api.NavigationEntry;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.tools.osgi.BaseGuiceFilter;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.mock.ExtHttpServiceMock;
import de.benjaminborbe.tools.osgi.test.BundleActivatorTestUtil;
import org.junit.Test;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class MicroblogGuiActivatorIntegrationTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new MicroblogGuiModulesMock());
		final MicroblogGuiActivator activator = injector.getInstance(MicroblogGuiActivator.class);
		assertNotNull(activator);
	}

	@Test
	public void testServlets() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new MicroblogGuiModulesMock());
		final MicroblogGuiActivator activator = new MicroblogGuiActivator() {

			@Override
			public Injector getInjector() {
				return injector;
			}

		};
		final BundleActivatorTestUtil bundleActivatorTestUtil = new BundleActivatorTestUtil();
		final ExtHttpServiceMock extHttpServiceMock = bundleActivatorTestUtil.startBundle(activator);
		final List<String> paths = new ArrayList<>();
		paths.add("/" + MicroblogGuiConstants.NAME + MicroblogGuiConstants.URL_CONVERSATION_SEND);
		paths.add("/" + MicroblogGuiConstants.NAME + MicroblogGuiConstants.URL_POST_SEND);
		paths.add("/" + MicroblogGuiConstants.NAME + MicroblogGuiConstants.URL_POST_REFRESH);
		paths.add("/" + MicroblogGuiConstants.NAME + MicroblogGuiConstants.URL_POST_UPDATE);
		paths.add("/" + MicroblogGuiConstants.NAME + MicroblogGuiConstants.URL_SLASH);

		paths.add("/" + MicroblogGuiConstants.NAME + MicroblogGuiConstants.URL_NOTIFICATION_LIST);
		paths.add("/" + MicroblogGuiConstants.NAME + MicroblogGuiConstants.URL_NOTIFICATION_DEACTIVATE);
		paths.add("/" + MicroblogGuiConstants.NAME + MicroblogGuiConstants.URL_NOTIFICATION_ACTIVATE_JSON);
		paths.add("/" + MicroblogGuiConstants.NAME + MicroblogGuiConstants.URL_NOTIFICATION_DEACTIVATE_JSON);
		paths.add("/" + MicroblogGuiConstants.NAME + MicroblogGuiConstants.URL_NOTIFICATION_LIST_JSON);

		assertEquals(paths.size(), extHttpServiceMock.getRegisterServletCallCounter());
		for (final String path : paths) {
			assertTrue("no servlet for path " + path + " registered", extHttpServiceMock.hasServletPath(path));
		}
	}

	@Test
	public void testFilters() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new MicroblogGuiModulesMock());
		final MicroblogGuiActivator activator = new MicroblogGuiActivator() {

			@Override
			public Injector getInjector() {
				return injector;
			}

		};

		final BundleActivatorTestUtil bundleActivatorTestUtil = new BundleActivatorTestUtil();
		final ExtHttpServiceMock extHttpServiceMock = bundleActivatorTestUtil.startBundle(activator);
		final List<String> paths = Arrays.asList("/microblog.*");
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
		final Injector injector = GuiceInjectorBuilder.getInjector(new MicroblogGuiModulesMock());
		final MicroblogGuiActivator activator = new MicroblogGuiActivator() {

			@Override
			public Injector getInjector() {
				return injector;
			}

		};
		final BundleActivatorTestUtil bundleActivatorTestUtil = new BundleActivatorTestUtil();
		final ExtHttpServiceMock extHttpServiceMock = bundleActivatorTestUtil.startBundle(activator);
		final List<String> paths = new ArrayList<>();
		assertEquals(paths.size(), extHttpServiceMock.getRegisterResourceCallCounter());
		for (final String path : paths) {
			assertTrue("no resource for path " + path + " registered", extHttpServiceMock.hasResource(path));
		}
	}

	@Test
	public void testServices() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new MicroblogGuiModulesMock());
		final MicroblogGuiActivator activator = new MicroblogGuiActivator() {

			@Override
			public Injector getInjector() {
				return injector;
			}

		};

		final BundleActivatorTestUtil bundleActivatorTestUtil = new BundleActivatorTestUtil();
		bundleActivatorTestUtil.startBundle(activator);

		final Collection<ServiceInfo> serviceInfos = activator.getServiceInfos();
		final List<String> names = new ArrayList<>();
		names.add(DashboardContentWidget.class.getName());
		names.add(NavigationEntry.class.getName());
		names.add(ConfigurationDescription.class.getName());
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
