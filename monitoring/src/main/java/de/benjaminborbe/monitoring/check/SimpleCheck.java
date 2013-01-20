package de.benjaminborbe.monitoring.check;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.monitoring.api.MonitoringCheck;
import de.benjaminborbe.monitoring.api.MonitoringCheckResult;

@Singleton
public class SimpleCheck implements MonitoringCheck {

	@Inject
	public SimpleCheck() {
	}

	@Override
	public MonitoringCheckResult check() {
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
