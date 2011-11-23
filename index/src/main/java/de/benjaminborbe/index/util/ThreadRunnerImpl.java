package de.benjaminborbe.index.util;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ThreadRunnerImpl implements ThreadRunner {

	@Inject
	public ThreadRunnerImpl() {

	}

	@Override
	public Thread run(final String name, final Runnable runnable) {
		final Thread thread = new Thread(runnable, name);
		thread.start();
		return thread;
	}
}
