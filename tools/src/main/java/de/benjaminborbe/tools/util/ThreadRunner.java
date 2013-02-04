package de.benjaminborbe.tools.util;

public interface ThreadRunner {

	Thread run(String name, Runnable runnable);

	void runIn(long time, Runnable runnable);

}
