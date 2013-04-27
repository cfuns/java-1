package de.benjaminborbe.tools.osgi;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;
import de.benjaminborbe.tools.util.ThreadRunner;
import de.benjaminborbe.tools.util.ThreadRunnerMock;
import org.easymock.EasyMock;
import org.junit.Test;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;

import javax.inject.Singleton;
import java.util.Collection;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class HttpBundleActivatorIntegrationTest {

	private final class TestService {

	}

	private final class HttpBundleActivatorModuleMock extends AbstractModule {

		@Override
		protected void configure() {
			bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);
			bind(ThreadRunner.class).to(ThreadRunnerMock.class).in(Singleton.class);
		}
	}

	private class HttpBundleActivatorMock extends HttpBundleActivator {

		private HttpBundleActivatorMock(final String name) {
			super(name);
		}

		@Override
		protected Modules getModules(final BundleContext context) {
			return new Modules() {

				@Override
				public Collection<Module> getModules() {
					final Set<Module> result = new HashSet<>();
					result.add(new HttpBundleActivatorModuleMock());
					return result;
				}
			};
		}
	}

	@Test
	public void testStartStop() throws Exception {
		final HttpBundleActivator httpBundleActivator = new HttpBundleActivatorMock("startStop");
		final Filter filter = EasyMock.createMock(Filter.class);
		EasyMock.replay(filter);
		final BundleContext context = EasyMock.createMock(BundleContext.class);
		EasyMock.expect(context.createFilter(EasyMock.anyObject(String.class))).andReturn(filter).times(2);
		context.addServiceListener(EasyMock.anyObject(ServiceListener.class), EasyMock.anyObject(String.class));
		final ServiceReference[] value = new ServiceReference[0];
		EasyMock.expect(context.getServiceReferences(EasyMock.anyObject(String.class), EasyMock.anyObject(String.class))).andReturn(value);
		EasyMock.replay(context);
		httpBundleActivator.start(context);
		httpBundleActivator.stop(context);
	}

	@Test
	public void testcleanupAlias() throws Exception {
		final HttpBundleActivator httpBundleActivator = new HttpBundleActivatorMock("cleanupAlias");
		final Filter filter = EasyMock.createMock(Filter.class);
		EasyMock.replay(filter);
		final BundleContext context = EasyMock.createMock(BundleContext.class);
		EasyMock.expect(context.createFilter(EasyMock.anyObject(String.class))).andReturn(filter).times(1);
		context.addServiceListener(EasyMock.anyObject(ServiceListener.class), EasyMock.anyObject(String.class));
		final ServiceReference[] value = new ServiceReference[0];
		EasyMock.expect(context.getServiceReferences(EasyMock.anyObject(String.class), EasyMock.anyObject(String.class))).andReturn(value);
		EasyMock.replay(context);
		httpBundleActivator.start(context);

		assertEquals("/", httpBundleActivator.cleanupAlias("/"));
		assertEquals("/", httpBundleActivator.cleanupAlias("//"));
		assertEquals("/", httpBundleActivator.cleanupAlias("///"));
		assertEquals("/", httpBundleActivator.cleanupAlias("////"));
		assertEquals("/robots.txt", httpBundleActivator.cleanupAlias("/robots.txt"));
		assertEquals("/robots.txt", httpBundleActivator.cleanupAlias("//robots.txt"));
		assertEquals("/robots.txt", httpBundleActivator.cleanupAlias("///robots.txt"));
		assertEquals("/robots.txt", httpBundleActivator.cleanupAlias("////robots.txt"));
		assertEquals("/search", httpBundleActivator.cleanupAlias("/search"));
		assertEquals("/search", httpBundleActivator.cleanupAlias("/search/"));
		assertEquals("/search", httpBundleActivator.cleanupAlias("/search//"));
		assertEquals("/search", httpBundleActivator.cleanupAlias("/search///"));
		assertEquals("/search", httpBundleActivator.cleanupAlias("/search/"));
		assertEquals("/search/suggest", httpBundleActivator.cleanupAlias("//search//suggest/"));
	}

	@Test
	public void testServiceAdded() throws Exception {
		final Class<?> name = TestService.class;
		final Object service = new TestService();
		final Properties properties = new Properties();
		final HttpBundleActivator httpBundleActivator = new HttpBundleActivatorMock("prefix") {

			@Override
			public Collection<ServiceInfo> getServiceInfos() {
				final Set<ServiceInfo> result = new HashSet<>(super.getServiceInfos());
				result.add(new ServiceInfo(name, service, properties));
				return result;
			}

		};
		final Filter filter = EasyMock.createMock(Filter.class);
		EasyMock.replay(filter);
		final BundleContext context = EasyMock.createMock(BundleContext.class);
		EasyMock.expect(context.createFilter(EasyMock.anyObject(String.class))).andReturn(filter).times(1);
		context.addServiceListener(EasyMock.anyObject(ServiceListener.class), EasyMock.anyObject(String.class));
		final ServiceReference[] value = new ServiceReference[0];
		EasyMock.expect(context.getServiceReferences(EasyMock.anyObject(String.class), EasyMock.anyObject(String.class))).andReturn(value);

		EasyMock.expect(context.registerService(TestService.class.getName(), service, properties)).andReturn(null);
		EasyMock.replay(context);
		httpBundleActivator.start(context);

	}
}
