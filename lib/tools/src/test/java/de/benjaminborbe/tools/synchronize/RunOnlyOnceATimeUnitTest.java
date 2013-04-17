package de.benjaminborbe.tools.synchronize;

import de.benjaminborbe.tools.util.ThreadResult;
import de.benjaminborbe.tools.util.ThreadRunner;
import de.benjaminborbe.tools.util.ThreadRunnerImpl;
import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import static org.junit.Assert.assertEquals;

public class RunOnlyOnceATimeUnitTest {

	private final class MyRunnable implements Runnable {

		private final long timeout;

		private final ThreadResult<Long> threadResult;

		private MyRunnable(final long timeout, final ThreadResult<Long> threadResult) {
			this.timeout = timeout;
			this.threadResult = threadResult;
		}

		@Override
		public void run() {
			try {
				Thread.sleep(timeout);
				synchronized (threadResult) {
					threadResult.set(threadResult.get() + 1);
				}
			} catch (final Exception e) {
			}
		}
	}

	@Test
	public void testNotThreaded() {
		final long timeout = 1l;
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final RunOnlyOnceATime runOnlyOnceATime = new RunOnlyOnceATime(logger);
		final ThreadResult<Long> threadResult = new ThreadResult<>();
		threadResult.set(0L);
		final Runnable runnable = new MyRunnable(timeout, threadResult);

		// not parallel
		runOnlyOnceATime.run(runnable);
		runOnlyOnceATime.run(runnable);
		assertEquals(new Long(2), threadResult.get());
	}

	@Test
	public void testThreaded() throws Exception {
		final ThreadRunner threadRunner = new ThreadRunnerImpl();
		final long timeout = 100l;
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final RunOnlyOnceATime runOnlyOnceATime = new RunOnlyOnceATime(logger);
		final ThreadResult<Long> threadResult = new ThreadResult<>();
		threadResult.set(0L);
		final Runnable runnable = new MyRunnable(timeout, threadResult);

		final Thread t1 = threadRunner.run("t1", new Runnable() {

			@Override
			public void run() {
				runOnlyOnceATime.run(runnable);
			}
		});
		final Thread t2 = threadRunner.run("t2", new Runnable() {

			@Override
			public void run() {
				runOnlyOnceATime.run(runnable);
			}
		});
		t1.join();
		t2.join();
		assertEquals(new Long(1), threadResult.get());
	}
}
