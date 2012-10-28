package de.benjaminborbe.tools.action;

public interface Action {

	void executeOnce();

	void executeRetry();

	void onSuccess();

	void onFailure();

	boolean validateExecuteResult();

	long getWaitTimeout();

	long getRetryDelay();

}
