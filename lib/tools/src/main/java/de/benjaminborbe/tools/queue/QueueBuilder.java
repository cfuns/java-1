package de.benjaminborbe.tools.queue;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.tools.util.ThreadRunner;

public class QueueBuilder {

	private final Logger logger;

	private final ThreadRunner threadRunner;

	@Inject
	public QueueBuilder(final Logger logger, final ThreadRunner threadRunner) {
		this.logger = logger;
		this.threadRunner = threadRunner;
	}

	public <M> Queue<M> buildQueue(final QueueConsumer<M> queueConsumer) {
		return new Queue<M>(logger, threadRunner, queueConsumer);
	}

}
