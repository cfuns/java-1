package de.benjaminborbe.tools.util;

public class Counter {

	private long counter = 0;

	public long get() {
		return counter;
	}

	public void increase() {
		counter++;
	}

	public void reset() {
		counter = 0;
	}
}
