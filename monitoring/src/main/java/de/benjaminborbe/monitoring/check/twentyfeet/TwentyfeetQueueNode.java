package de.benjaminborbe.monitoring.check.twentyfeet;

import com.google.inject.Inject;

import de.benjaminborbe.monitoring.api.Check;
import de.benjaminborbe.monitoring.check.HasCheckNode;

public class TwentyfeetQueueNode implements HasCheckNode {

	private final TwentyfeetQueueCheck twentyfeetQueueCheck;

	@Inject
	public TwentyfeetQueueNode(final TwentyfeetQueueCheck twentyfeetQueueCheck) {
		this.twentyfeetQueueCheck = twentyfeetQueueCheck;
	}

	@Override
	public Check getCheck() {
		return twentyfeetQueueCheck;
	}

}
