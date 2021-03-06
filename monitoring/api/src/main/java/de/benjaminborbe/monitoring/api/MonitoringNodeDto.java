package de.benjaminborbe.monitoring.api;

import java.util.Calendar;
import java.util.Map;

public class MonitoringNodeDto implements MonitoringNode {

	private MonitoringCheckIdentifier checkType;

	private String name;

	private MonitoringNodeIdentifier id;

	private Map<String, String> parameter;

	private Boolean active;

	private Boolean silent;

	private Boolean result;

	private String message;

	private String description;

	private MonitoringNodeIdentifier parentId;

	private Integer failureCounter;

	private Calendar lastCheck;

	private String exception;

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
	public MonitoringCheckIdentifier getCheckType() {
		return checkType;
	}

	public void setCheckType(final MonitoringCheckIdentifier checkType) {
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

	public void setParentId(final MonitoringNodeIdentifier parentId) {
		this.parentId = parentId;
	}

	@Override
	public MonitoringNodeIdentifier getParentId() {
		return parentId;
	}

	@Override
	public Integer getFailureCounter() {
		return failureCounter;
	}

	public void setFailureCounter(final Integer failureCounter) {
		this.failureCounter = failureCounter;
	}

	@Override
	public Calendar getLastCheck() {
		return lastCheck;
	}

	public void setLastCheck(final Calendar lastCheck) {
		this.lastCheck = lastCheck;
	}

	public String getException() {
		return exception;
	}

	public void setException(final String exception) {
		this.exception = exception;
	}

}
