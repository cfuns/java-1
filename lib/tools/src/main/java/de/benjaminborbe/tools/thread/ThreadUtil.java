package de.benjaminborbe.tools.thread;

import javax.inject.Inject;

public class ThreadUtil {

	private final long WAIT_INTERVAL = 100;

	@Inject
	public ThreadUtil() {
	}

	public void wait(final long millis, final Assert a) throws InterruptedException {
		long d = 0;
		while (!a.calc() && d <= millis) {
			Thread.sleep(WAIT_INTERVAL);
			d += WAIT_INTERVAL;
		}
	}

}
