package de.benjaminborbe.tools.util;

import com.google.inject.Singleton;

@Singleton
public class ThreadRunnerMock implements ThreadRunner {

	@Override
	public Thread run(final String threadName, final Runnable runnable) {
		try {
			final Thread thread = new Thread(runnable, threadName);
			thread.start();
			thread.join();
			return thread;
		}
		catch (final InterruptedException e) {
			throw new RuntimeException("InterruptedException", e);
		}
	}

	@Override
	public void runIn(final long time, final Runnable runnable) {
		run("runIn", runnable);
	}

}
