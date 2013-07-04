package de.benjaminborbe.monitoring.dao;

import de.benjaminborbe.monitoring.api.MonitoringCheckIdentifier;
import de.benjaminborbe.monitoring.api.MonitoringHasParentId;
import de.benjaminborbe.monitoring.api.MonitoringNodeIdentifier;
import de.benjaminborbe.storage.tools.EntityBase;
import de.benjaminborbe.storage.tools.HasCreated;
import de.benjaminborbe.storage.tools.HasModified;

import java.util.Calendar;
import java.util.Map;

public class MonitoringNodeBean extends EntityBase<MonitoringNodeIdentifier> implements HasCreated, HasModified, MonitoringHasParentId {

	private static final long serialVersionUID = -8803301003126328406L;

	private MonitoringNodeIdentifier id;

	private String name;

	private Calendar created;

	private Calendar modified;

	private MonitoringCheckIdentifier checkType;

	private Map<String, String> parameter;

	private Boolean active;

	private Boolean silent;

	private String message;

	private String exception;

	private Boolean result;

	private MonitoringNodeIdentifier parentId;

	private Integer failureCounter;

	private Calendar lastCheck;

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public Calendar getCreated() {
		return created;
	}

	@Override
	public void setCreated(final Calendar created) {
		this.created = created;
	}

	@Override
	public Calendar getModified() {
		return modified;
	}

	@Override
	public void setModified(final Calendar modified) {
		this.modified = modified;
	}

	@Override
	public MonitoringNodeIdentifier getId() {
		return id;
	}

	@Override
	public void setId(final MonitoringNodeIdentifier id) {
		this.id = id;
	}

	public MonitoringCheckIdentifier getCheckType() {
		return checkType;
	}

	public void setCheckType(final MonitoringCheckIdentifier checkType) {
		this.checkType = checkType;
	}

	public Map<String, String> getParameter() {
		return parameter;
	}

	public void setParameter(final Map<String, String> parameter) {
		this.parameter = parameter;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(final Boolean active) {
		this.active = active;
	}

	public Boolean getSilent() {
		return silent;
	}

	public void setSilent(final Boolean silent) {
		this.silent = silent;
	}

	public String getMessage() {
		return message;
	}

	public Boolean getResult() {
		return result;
	}

	public void setMessage(final String message) {
		this.message = message;
	}

	public void setResult(final Boolean result) {
		this.result = result;
	}

	@Override
	public MonitoringNodeIdentifier getParentId() {
		return parentId;
	}

	public void setParentId(final MonitoringNodeIdentifier parentId) {
		this.parentId = parentId;
	}

	public Integer getFailureCounter() {
		return failureCounter;
	}

	public void setFailureCounter(final Integer failureCounter) {
		this.failureCounter = failureCounter;
	}

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
