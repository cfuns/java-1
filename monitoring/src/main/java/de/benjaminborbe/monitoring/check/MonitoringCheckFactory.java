package de.benjaminborbe.monitoring.check;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.monitoring.api.MonitoringCheckType;

@Singleton
public class MonitoringCheckFactory {

	private final Map<MonitoringCheckType, MonitoringCheck> checks = new HashMap<MonitoringCheckType, MonitoringCheck>();

	@Inject
	public MonitoringCheckFactory(final MonitoringCheckHttp monitoringCheckHttp, final MonitoringCheckNop monitoringCheckNop, final MonitoringCheckTcp monitoringCheckTcp) {
		add(monitoringCheckHttp);
		add(monitoringCheckNop);
		add(monitoringCheckTcp);
	}

	private void add(final MonitoringCheck monitoringCheck) {
		checks.put(monitoringCheck.getType(), monitoringCheck);
	}

	public MonitoringCheck get(final MonitoringCheckType type) {
		return checks.get(type);
	}
}
