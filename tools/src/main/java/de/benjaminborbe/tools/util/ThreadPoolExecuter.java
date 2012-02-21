package de.benjaminborbe.tools.util;

public interface ThreadPoolExecuter {

	void execute(Runnable runnable);

	void shutDown();
}
