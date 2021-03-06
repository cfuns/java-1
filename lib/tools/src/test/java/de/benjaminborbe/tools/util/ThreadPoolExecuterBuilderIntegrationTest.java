package de.benjaminborbe.tools.util;

import com.google.inject.Injector;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.tools.guice.ToolModules;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ThreadPoolExecuterBuilderIntegrationTest {

	private final class RunnableImplementation implements Runnable {

		private final Counter counter;

		private RunnableImplementation(final Counter counter) {
			this.counter = counter;
		}

		@Override
		public void run() {
			try {
				// Thread.sleep(1);
				counter.increase();
			} catch (final Exception e) {
			}
		}
	}

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new ToolModules());
		assertNotNull(injector.getInstance(ThreadPoolExecuterBuilder.class));
	}

	@Test
	public void testExecute() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new ToolModules());
		final ThreadPoolExecuterBuilder threadPoolExecuterBuilder = injector.getInstance(ThreadPoolExecuterBuilder.class);
		final String threadName = "test";
		final Counter counter = new Counter();
		final int threadAmount = 10;
		final ThreadPoolExecuter threadPoolExecuter = threadPoolExecuterBuilder.build(threadName, threadAmount);
		final long limit = 100;
		for (long i = 0; i < limit; ++i) {
			threadPoolExecuter.execute(new RunnableImplementation(counter));
		}
		threadPoolExecuter.shutDown();
		Thread.sleep(100);
		assertEquals(limit, counter.get());
	}
}
