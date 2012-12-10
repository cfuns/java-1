package de.benjaminborbe.task.gui;

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

import de.benjaminborbe.dashboard.api.DashboardContentWidget;
import de.benjaminborbe.navigation.api.NavigationEntry;
import de.benjaminborbe.search.api.SearchSpecial;
import de.benjaminborbe.task.gui.guice.TaskGuiModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.tools.osgi.BaseGuiceFilter;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.mock.ExtHttpServiceMock;
import de.benjaminborbe.tools.osgi.test.BundleActivatorTestUtil;

public class TaskGuiActivatorIntegrationTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new TaskGuiModulesMock());
		final TaskGuiActivator activator = injector.getInstance(TaskGuiActivator.class);
		assertNotNull(activator);
	}

	@Test
	public void testServlets() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new TaskGuiModulesMock());
		final TaskGuiActivator activator = new TaskGuiActivator() {

			@Override
			public Injector getInjector() {
				return injector;
			}

		};
		final BundleActivatorTestUtil bundleActivatorTestUtil = new BundleActivatorTestUtil();
		final ExtHttpServiceMock extHttpServiceMock = bundleActivatorTestUtil.startBundle(activator);
		final List<String> paths = new ArrayList<String>();
		paths.add("/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_TASK_FIRST);
		paths.add("/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_TASK_LAST);
		paths.add("/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_TASK_START_TOMORROW);
		paths.add("/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_TASKS_NEXT);
		paths.add("/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_TASK_UPDATE);
		paths.add("/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_TASK_SWAP_PRIO);
		paths.add("/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_TASKS_COMPLETED);
		paths.add("/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_TASKS_UNCOMPLETED);
		paths.add("/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_TASK_CREATE);
		paths.add("/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_TASK_DELETE);
		paths.add("/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_TASK_COMPLETE);
		paths.add("/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_TASK_UNCOMPLETE);
		paths.add("/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_TASKCONTEXT_CREATE);
		paths.add("/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_TASKCONTEXT_DELETE);
		paths.add("/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_TASKCONTEXT_LIST);
		paths.add("/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_TASK_VIEW);
		paths.add("/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_TASKCONTEXT_UPDATE);

		assertEquals(paths.size(), extHttpServiceMock.getRegisterServletCallCounter());
		for (final String path : paths) {
			assertTrue("no servlet for path " + path + " registered", extHttpServiceMock.hasServletPath(path));
		}
	}

	@Test
	public void testFilters() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new TaskGuiModulesMock());
		final TaskGuiActivator activator = new TaskGuiActivator() {

			@Override
			public Injector getInjector() {
				return injector;
			}
		};
		final BundleActivatorTestUtil bundleActivatorTestUtil = new BundleActivatorTestUtil();
		final ExtHttpServiceMock extHttpServiceMock = bundleActivatorTestUtil.startBundle(activator);
		final List<String> paths = Arrays.asList("/task.*");
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
		final Injector injector = GuiceInjectorBuilder.getInjector(new TaskGuiModulesMock());
		final TaskGuiActivator activator = new TaskGuiActivator() {

			@Override
			public Injector getInjector() {
				return injector;
			}
		};
		final BundleActivatorTestUtil bundleActivatorTestUtil = new BundleActivatorTestUtil();
		final ExtHttpServiceMock extHttpServiceMock = bundleActivatorTestUtil.startBundle(activator);
		final List<String> paths = new ArrayList<String>();
		paths.add("/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_CSS);
		paths.add("/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_JS);
		paths.add("/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_IMAGES);

		assertEquals(paths.size(), extHttpServiceMock.getRegisterResourceCallCounter());
		for (final String path : paths) {
			assertTrue("no resource for path " + path + " registered", extHttpServiceMock.hasResource(path));
		}
	}

	@Test
	public void testServices() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new TaskGuiModulesMock());
		final TaskGuiActivator activator = new TaskGuiActivator() {

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
		names.add(DashboardContentWidget.class.getName());
		names.add(SearchSpecial.class.getName());

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
