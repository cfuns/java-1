package de.benjaminborbe.tools.util;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ThreadRunnerImpl implements ThreadRunner {

	private final class RunInRunnable implements Runnable {

		private final Runnable runnable;

		private final long time;

		private RunInRunnable(final Runnable runnable, final long time) {
			this.runnable = runnable;
			this.time = time;
		}

		@Override
		public void run() {
			try {
				Thread.sleep(time);
				runInternal("delayedExecute", runnable);
			}
			catch (final InterruptedException e) {
			}
		}
	}

	@Inject
	public ThreadRunnerImpl() {

	}

	@Override
	public Thread run(final String name, final Runnable runnable) {
		return runInternal(name, runnable);
	}

	private Thread runInternal(final String name, final Runnable runnable) {
		final Thread thread = new Thread(runnable, name);
		thread.start();
		return thread;
	}

	@Override
	public void runIn(final long time, final Runnable runnable) {
		runInternal("runin", new RunInRunnable(runnable, time));
	}

}
