package de.benjaminborbe.monitoring.api;

public class MonitoringNodeDto implements MonitoringNode {

	private MonitoringCheckType checkType;

	private String name;

	private MonitoringNodeIdentifier id;

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

	public MonitoringCheckType getCheckType() {
		return checkType;
	}

	public void setCheckType(MonitoringCheckType checkType) {
		this.checkType = checkType;
	}

}
