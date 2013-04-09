package de.benjaminborbe.monitoring.util;

import java.util.Calendar;
import java.util.Map;

import com.google.inject.Inject;

import de.benjaminborbe.monitoring.api.MonitoringCheck;
import de.benjaminborbe.monitoring.api.MonitoringCheckIdentifier;
import de.benjaminborbe.monitoring.api.MonitoringNode;
import de.benjaminborbe.monitoring.api.MonitoringNodeIdentifier;
import de.benjaminborbe.monitoring.check.MonitoringCheckRegistry;
import de.benjaminborbe.monitoring.dao.MonitoringNodeBean;

public class MonitoringNodeBuilder {

	private final MonitoringCheckRegistry monitoringCheckRegistry;

	private final class MonitoringNodeDescription implements MonitoringNode {

		private final MonitoringNodeBean node;

		private MonitoringNodeDescription(final MonitoringNodeBean node) {
			this.node = node;
		}

		@Override
		public Boolean getResult() {
			return node.getResult();
		}

		@Override
		public String getMessage() {
			return node.getMessage();
		}

		@Override
		public String getName() {
			return node.getName();
		}

		@Override
		public MonitoringNodeIdentifier getId() {
			return node.getId();
		}

		@Override
		public String getDescription() {
			final MonitoringCheck check = monitoringCheckRegistry.get(node.getCheckType());
			if (check != null) {
				return check.getDescription(node.getParameter());
			}
			else {
				return null;
			}
		}

		@Override
		public MonitoringCheckIdentifier getCheckType() {
			return node.getCheckType();
		}

		@Override
		public Map<String, String> getParameter() {
			return node.getParameter();
		}

		@Override
		public Boolean getSilent() {
			return node.getSilent();
		}

		@Override
		public Boolean getActive() {
			return node.getActive();
		}

		@Override
		public MonitoringNodeIdentifier getParentId() {
			return node.getParentId();
		}

		@Override
		public Integer getFailureCounter() {
			return node.getFailureCounter();
		}

		@Override
		public Calendar getLastCheck() {
			return node.getLastCheck();
		}

		@Override
		public String getException() {
			return node.getException();
		}
	}

	@Inject
	public MonitoringNodeBuilder(final MonitoringCheckRegistry monitoringCheckRegistry) {
		this.monitoringCheckRegistry = monitoringCheckRegistry;
	}

	public MonitoringNode build(final MonitoringNodeBean bean) {
		return new MonitoringNodeDescription(bean);
	}

}
