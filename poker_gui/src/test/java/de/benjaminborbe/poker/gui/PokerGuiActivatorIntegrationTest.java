package de.benjaminborbe.poker.gui;

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

import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.navigation.api.NavigationEntry;
import de.benjaminborbe.poker.gui.guice.PokerGuiModulesMock;
import de.benjaminborbe.tools.osgi.test.BundleActivatorTestUtil;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.tools.osgi.BaseGuiceFilter;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.mock.ExtHttpServiceMock;

public class PokerGuiActivatorIntegrationTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new PokerGuiModulesMock());
		final PokerGuiActivator activator = injector.getInstance(PokerGuiActivator.class);
		assertNotNull(activator);
	}

	@Test
	public void testServlets() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new PokerGuiModulesMock());
		final PokerGuiActivator activator = new PokerGuiActivator() {

			@Override
			public Injector getInjector() {
				return injector;
			}

		};
		final BundleActivatorTestUtil bundleActivatorTestUtil = new BundleActivatorTestUtil();
		final ExtHttpServiceMock extHttpServiceMock = bundleActivatorTestUtil.startBundle(activator);
		final List<String> paths = new ArrayList<String>();
		paths.add("/" + PokerGuiConstants.NAME + PokerGuiConstants.URL_HOME);
		paths.add("/" + PokerGuiConstants.NAME + PokerGuiConstants.URL_API_HELP);
		paths.add("/" + PokerGuiConstants.NAME + PokerGuiConstants.URL_ACTION_CALL);
		paths.add("/" + PokerGuiConstants.NAME + PokerGuiConstants.URL_ACTION_FOLD);
		paths.add("/" + PokerGuiConstants.NAME + PokerGuiConstants.URL_ACTION_RAISE);
		paths.add("/" + PokerGuiConstants.NAME + PokerGuiConstants.URL_GAME_STOP);
		paths.add("/" + PokerGuiConstants.NAME + PokerGuiConstants.URL_GAME_JOIN);
		paths.add("/" + PokerGuiConstants.NAME + PokerGuiConstants.URL_GAME_LEAVE);
		paths.add("/" + PokerGuiConstants.NAME + PokerGuiConstants.URL_GAME_CREATE);
		paths.add("/" + PokerGuiConstants.NAME + PokerGuiConstants.URL_GAME_UPDATE);
		paths.add("/" + PokerGuiConstants.NAME + PokerGuiConstants.URL_GAME_LIST);
		paths.add("/" + PokerGuiConstants.NAME + PokerGuiConstants.URL_GAME_DELETE);
		paths.add("/" + PokerGuiConstants.NAME + PokerGuiConstants.URL_GAME_VIEW);
		paths.add("/" + PokerGuiConstants.NAME + PokerGuiConstants.URL_GAME_START);
		paths.add("/" + PokerGuiConstants.NAME + PokerGuiConstants.URL_PLAYER_CREATE);
		paths.add("/" + PokerGuiConstants.NAME + PokerGuiConstants.URL_PLAYER_UPDATE);
		paths.add("/" + PokerGuiConstants.NAME + PokerGuiConstants.URL_PLAYER_LIST);
		paths.add("/" + PokerGuiConstants.NAME + PokerGuiConstants.URL_PLAYER_VIEW);
		paths.add("/" + PokerGuiConstants.NAME + PokerGuiConstants.URL_PLAYER_DELETE);
		paths.add("/" + PokerGuiConstants.NAME + PokerGuiConstants.URL_ACTION_CALL_JSON);
		paths.add("/" + PokerGuiConstants.NAME + PokerGuiConstants.URL_ACTION_FOLD_JSON);
		paths.add("/" + PokerGuiConstants.NAME + PokerGuiConstants.URL_ACTION_RAISE_JSON);
		paths.add("/" + PokerGuiConstants.NAME + PokerGuiConstants.URL_PLAYER_STATUS_JSON);
		paths.add("/" + PokerGuiConstants.NAME + PokerGuiConstants.URL_GAME_STATUS_JSON);
		assertEquals(paths.size(), extHttpServiceMock.getRegisterServletCallCounter());
		for (final String path : paths) {
			assertTrue("no servlet for path " + path + " registered", extHttpServiceMock.hasServletPath(path));
		}
	}

	@Test
	public void testFilters() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new PokerGuiModulesMock());
		final PokerGuiActivator activator = new PokerGuiActivator() {

			@Override
			public Injector getInjector() {
				return injector;
			}
		};
		final BundleActivatorTestUtil bundleActivatorTestUtil = new BundleActivatorTestUtil();
		final ExtHttpServiceMock extHttpServiceMock = bundleActivatorTestUtil.startBundle(activator);
		final List<String> paths = Arrays.asList("/poker.*");
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
		final Injector injector = GuiceInjectorBuilder.getInjector(new PokerGuiModulesMock());
		final PokerGuiActivator activator = new PokerGuiActivator() {

			@Override
			public Injector getInjector() {
				return injector;
			}
		};
		final BundleActivatorTestUtil bundleActivatorTestUtil = new BundleActivatorTestUtil();
		final ExtHttpServiceMock extHttpServiceMock = bundleActivatorTestUtil.startBundle(activator);
		final List<String> paths = new ArrayList<String>();
		paths.add("/" + PokerGuiConstants.NAME + PokerGuiConstants.URL_CSS);
		assertEquals(paths.size(), extHttpServiceMock.getRegisterResourceCallCounter());
		for (final String path : paths) {
			assertTrue("no resource for path " + path + " registered", extHttpServiceMock.hasResource(path));
		}
	}

	@Test
	public void testServices() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new PokerGuiModulesMock());
		final PokerGuiActivator activator = new PokerGuiActivator() {

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
