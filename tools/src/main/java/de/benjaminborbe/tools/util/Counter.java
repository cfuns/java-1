package de.benjaminborbe.tools.util;

import java.util.concurrent.atomic.AtomicLong;

public class Counter {

	private final AtomicLong counter = new AtomicLong();

	public long get() {
		return counter.get();
	}

	public void increase() {
		counter.incrementAndGet();
	}

	public void reset() {
		counter.set(0);
	}
}
