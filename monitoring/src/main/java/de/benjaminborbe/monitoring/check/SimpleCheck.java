package de.benjaminborbe.monitoring.check;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class SimpleCheck implements Check {

	@Inject
	public SimpleCheck() {
	}

	@Override
	public CheckResult check() {
		return new CheckResultImpl(this, true, null);
	}

	@Override
	public String getDescription() {
		return "Simple Check. Return allways true";
	}
}
