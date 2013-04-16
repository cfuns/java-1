package de.benjaminborbe.wow.core.xmpp.action;

import de.benjaminborbe.tools.util.ThreadResult;
import org.slf4j.Logger;

public class WowIncreaseCounterAction extends WowActionBase {

	private final ThreadResult<Integer> counter;

	private boolean executed = false;

	public WowIncreaseCounterAction(final Logger logger, final String name, final ThreadResult<Boolean> running, final ThreadResult<Integer> counter) {
		super(logger, name, running);
		this.counter = counter;
	}

	@Override
	public void executeOnce() {
		counter.set(counter.get() + 1);
		executed = true;
	}

	@Override
	public boolean validateExecuteResult() {
		return executed && super.validateExecuteResult();
	}

}
