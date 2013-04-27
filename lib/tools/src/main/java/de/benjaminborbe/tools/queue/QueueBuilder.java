package de.benjaminborbe.tools.queue;

import de.benjaminborbe.tools.util.ThreadRunner;
import org.slf4j.Logger;

import javax.inject.Inject;

public class QueueBuilder {

	private final Logger logger;

	private final ThreadRunner threadRunner;

	@Inject
	public QueueBuilder(final Logger logger, final ThreadRunner threadRunner) {
		this.logger = logger;
		this.threadRunner = threadRunner;
	}

	public <M> Queue<M> buildQueue(final QueueConsumer<M> queueConsumer) {
		return new Queue<>(logger, threadRunner, queueConsumer);
	}

}
