package de.benjaminborbe.tools.thread;

import com.google.inject.Inject;

import de.benjaminborbe.tools.util.ThreadRunner;

public class ThreadUtil {

	private final class RunIn implements Runnable {

		private final long time;

		private final Runnable runnable;

		private RunIn(final long time, final Runnable runnable) {
			this.time = time;
			this.runnable = runnable;
		}

		@Override
		public void run() {
			try {
				Thread.sleep(time);
				threadRunner.run("delayedExecute", runnable);
			}
			catch (final InterruptedException e) {
			}
		}
	}

	private final long WAIT_INTERVAL = 100;

	private final ThreadRunner threadRunner;

	@Inject
	public ThreadUtil(final ThreadRunner threadRunner) {
		this.threadRunner = threadRunner;
	}

	public void wait(final long millis, final Assert a) throws InterruptedException {
		long d = 0;
		while (!a.calc() && d <= millis) {
			Thread.sleep(WAIT_INTERVAL);
			d += WAIT_INTERVAL;
		}
	}

	public void runIn(final long time, final Runnable runnable) {
		threadRunner.run("runin", new RunIn(time, runnable));
	}
}
