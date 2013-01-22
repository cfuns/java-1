package de.benjaminborbe.monitoring.util;

import java.util.Map;

import com.google.inject.Inject;

import de.benjaminborbe.monitoring.api.MonitoringCheckType;
import de.benjaminborbe.monitoring.api.MonitoringNode;
import de.benjaminborbe.monitoring.api.MonitoringNodeIdentifier;
import de.benjaminborbe.monitoring.check.MonitoringCheck;
import de.benjaminborbe.monitoring.check.MonitoringCheckFactory;
import de.benjaminborbe.monitoring.dao.MonitoringNodeBean;

public class MonitoringNodeBuilder {

	private final MonitoringCheckFactory monitoringCheckFactory;

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
			final MonitoringCheck check = monitoringCheckFactory.get(node.getCheckType());
			return check.getDescription(node.getParameter());
		}

		@Override
		public MonitoringCheckType getCheckType() {
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
	}

	@Inject
	public MonitoringNodeBuilder(final MonitoringCheckFactory monitoringCheckFactory) {
		this.monitoringCheckFactory = monitoringCheckFactory;
	}

	public MonitoringNode build(final MonitoringNodeBean bean) {
		return new MonitoringNodeDescription(bean);
	}

}
