package de.benjaminborbe.tools.action;

public interface Action {

	void execute();

	void onSuccess();

	void onFailure();

	boolean validateExecuteResult();

	long getWaitTimeout();

	long getRetryDelay();

}
