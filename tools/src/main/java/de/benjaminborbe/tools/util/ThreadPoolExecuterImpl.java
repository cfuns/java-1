package de.benjaminborbe.tools.util;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;

import com.google.inject.Inject;

public class ThreadPoolExecuterImpl implements ThreadPoolExecuter {

	// Parallel running Threads(Executor) on System
	private final int corePoolSize = 2;

	// Maximum Threads allowed in Pool
	private final int maxPoolSize = 4;

	// Keep alive time for waiting threads for jobs(Runnable)
	private final long keepAliveTime = 10;

	// This is the one who manages and start the work
	private final ThreadPoolExecutor threadPool;

	// Working queue for jobs (Runnable). We add them finally here
	private final ArrayBlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(5);

	@Inject
	public ThreadPoolExecuterImpl(final Logger logger) {
		threadPool = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);
	}

	@Override
	public void execute(final Runnable command) {
		threadPool.execute(command);
	}

	@Override
	public void shutDown() {
		threadPool.shutdown();
	}
}
