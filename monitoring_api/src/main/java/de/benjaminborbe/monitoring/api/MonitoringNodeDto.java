package de.benjaminborbe.monitoring.api;

import java.util.Map;

public class MonitoringNodeDto implements MonitoringNode {

	private MonitoringCheckType checkType;

	private String name;

	private MonitoringNodeIdentifier id;

	private Map<String, String> parameter;

	private Boolean active;

	private Boolean silent;

	@Override
	public MonitoringNodeIdentifier getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setId(final MonitoringNodeIdentifier id) {
		this.id = id;
	}

	@Override
	public MonitoringCheckType getCheckType() {
		return checkType;
	}

	public void setCheckType(final MonitoringCheckType checkType) {
		this.checkType = checkType;
	}

	@Override
	public Map<String, String> getParameter() {
		return parameter;
	}

	public void setParameter(final Map<String, String> parameter) {
		this.parameter = parameter;
	}

	@Override
	public Boolean getSilent() {
		return silent;
	}

	@Override
	public Boolean getActive() {
		return active;
	}

	public void setActive(final Boolean active) {
		this.active = active;
	}

	public void setSilent(final Boolean silent) {
		this.silent = silent;
	}

}
