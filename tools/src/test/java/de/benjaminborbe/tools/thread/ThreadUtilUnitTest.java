package de.benjaminborbe.tools.thread;

import static org.junit.Assert.*;

import org.junit.Test;

import de.benjaminborbe.tools.util.Counter;
import de.benjaminborbe.tools.util.ThreadRunner;
import de.benjaminborbe.tools.util.ThreadRunnerImpl;

public class ThreadUtilUnitTest {

	@Test
	public void testRunIn() throws Exception {
		final ThreadRunner t = new ThreadRunnerImpl();
		final ThreadUtil threadUtil = new ThreadUtil();
		final Counter counter = new Counter();
		assertEquals(0l, counter.get());
		t.runIn(500, new Runnable() {

			@Override
			public void run() {
				counter.increase();
			}
		});

		assertEquals(0l, counter.get());

		threadUtil.wait(1000, new Assert() {

			@Override
			public boolean calc() {
				return counter.get() == 1l;
			}
		});

		assertEquals(1l, counter.get());
	}
}
