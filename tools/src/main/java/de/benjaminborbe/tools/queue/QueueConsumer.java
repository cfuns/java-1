package de.benjaminborbe.tools.queue;

public interface QueueConsumer<M> {

	void consume(M message);
}
