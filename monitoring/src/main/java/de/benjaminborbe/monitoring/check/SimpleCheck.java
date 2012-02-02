package de.benjaminborbe.monitoring.check;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.monitoring.api.Check;
import de.benjaminborbe.monitoring.api.CheckResult;

@Singleton
public class SimpleCheck implements Check {

	@Inject
	public SimpleCheck() {
	}

	@Override
	public CheckResult check() {
		return new CheckResultImpl(this, true, null, null);
	}

	@Override
	public String getDescription() {
		return "Simple Check. Return allways true";
	}

	@Override
	public String getName() {
		return "SimpleCheck";
	}
}
