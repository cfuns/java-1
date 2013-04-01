package de.benjaminborbe.wow.xmpp.action;

import org.slf4j.Logger;

import de.benjaminborbe.tools.util.ThreadResult;

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
