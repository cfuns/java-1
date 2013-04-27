package de.benjaminborbe.monitoring.check;

import de.benjaminborbe.monitoring.api.MonitoringCheck;
import de.benjaminborbe.monitoring.api.MonitoringCheckIdentifier;
import de.benjaminborbe.tools.registry.RegistryBase;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class MonitoringCheckRegistry extends RegistryBase<MonitoringCheck> {

	private final Map<MonitoringCheckIdentifier, MonitoringCheck> checks = new HashMap<>();

	@Inject
	public MonitoringCheckRegistry(
		final MonitoringCheckHttp monitoringCheckHttp,
		final MonitoringCheckNop monitoringCheckNop,
		final MonitoringCheckTcp monitoringCheckTcp,
		final MonitoringCheckRemote monitoringCheckRemote
	) {
		add(monitoringCheckHttp);
		add(monitoringCheckNop);
		add(monitoringCheckTcp);
		add(monitoringCheckRemote);
	}

	public MonitoringCheck get(final MonitoringCheckIdentifier type) {
		return checks.get(type);
	}

	@Override
	protected void onElementAdded(final MonitoringCheck object) {
		checks.put(object.getId(), object);
	}

	@Override
	protected void onElementRemoved(final MonitoringCheck object) {
		checks.remove(object.getId());
	}

}
