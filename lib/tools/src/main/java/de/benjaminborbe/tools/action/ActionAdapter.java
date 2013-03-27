package de.benjaminborbe.tools.action;

public class ActionAdapter implements Action {

	private final Action action;

	public ActionAdapter(final Action action) {
		this.action = action;
	}

	@Override
	public void onSuccess() {
		action.onSuccess();
	}

	@Override
	public void onFailure() {
		action.onFailure();
	}

	@Override
	public boolean validateExecuteResult() {
		return action.validateExecuteResult();
	}

	@Override
	public long getWaitTimeout() {
		return action.getWaitTimeout();
	}

	@Override
	public long getRetryDelay() {
		return action.getRetryDelay();
	}

	@Override
	public void executeOnce() {
		action.executeOnce();
	}

	@Override
	public void executeRetry() {
		action.executeRetry();
	}

}
