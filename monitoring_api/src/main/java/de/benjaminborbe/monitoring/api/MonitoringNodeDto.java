package de.benjaminborbe.monitoring.api;

import java.util.Map;

public class MonitoringNodeDto implements MonitoringNode {

	private MonitoringCheckType checkType;

	private String name;

	private MonitoringNodeIdentifier id;

	private Map<String, String> parameter;

	private Boolean active;

	private Boolean silent;

	private Boolean result;

	private String message;

	private String description;

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

	@Override
	public Boolean getResult() {
		return result;
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public String getDescription() {
		return description;
	}

	public void setResult(final Boolean result) {
		this.result = result;
	}

	public void setMessage(final String message) {
		this.message = message;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

}
