package de.benjaminborbe.tools.util;

import java.util.concurrent.TimeUnit;

public interface ThreadPoolExecuter {

	void execute(Runnable runnable);

	void shutDown();

	void awaitTermination(long timeout, TimeUnit unit) throws InterruptedException;
}
