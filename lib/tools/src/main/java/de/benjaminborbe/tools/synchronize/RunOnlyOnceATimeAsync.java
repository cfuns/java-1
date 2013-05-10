package de.benjaminborbe.tools.synchronize;

import de.benjaminborbe.tools.util.ThreadResult;
import de.benjaminborbe.tools.util.ThreadRunner;
import org.slf4j.Logger;

import javax.inject.Inject;

public class RunOnlyOnceATimeAsync {

	private final class Runner implements Runnable {

		private final Runnable runnable;

		public Runner(final Runnable runnable) {
			this.runnable = runnable;
		}

		@Override
		public void run() {
			try {
				runnable.run();
			} finally {
				running.set(false);
				logger.trace("thread completed, set running false");
			}
		}
	}

	private final Logger logger;

	private final ThreadRunner threadRunner;

	private final ThreadResult<Boolean> running = new ThreadResult<>(false);

	@Inject
	public RunOnlyOnceATimeAsync(final Logger logger, final ThreadRunner threadRunner) {
		this.logger = logger;
		this.threadRunner = threadRunner;
	}

	public boolean run(final Runnable runnable) {
		synchronized (running) {
			if (running.get()) {
				logger.trace("already running => skip");
				return false;
			} else {
				logger.trace("start thread, set running true");
				running.set(true);
				threadRunner.run("runOnlyOnceATimeAsync", new Runner(runnable));
				return true;
			}
		}
	}
}
