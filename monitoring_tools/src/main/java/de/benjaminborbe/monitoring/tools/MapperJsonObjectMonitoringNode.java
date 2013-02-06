package de.benjaminborbe.monitoring.tools;

import com.google.inject.Inject;

import de.benjaminborbe.monitoring.api.MonitoringNode;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.json.JSONObject;
import de.benjaminborbe.tools.json.JSONObjectSimple;

public class MapperJsonObjectMonitoringNode {

	public static final String MESSAGE = "message";

	public static final String RESULT = "result";

	private final CalendarUtil calendarUtil;

	@Inject
	public MapperJsonObjectMonitoringNode(final CalendarUtil calendarUtil) {
		this.calendarUtil = calendarUtil;
	}

	public JSONObject map(final MonitoringNode node) {
		final JSONObject nodeResult = new JSONObjectSimple();
		nodeResult.put("id", node.getId());
		nodeResult.put(MESSAGE, node.getMessage());
		nodeResult.put("name", node.getName());
		nodeResult.put("description", node.getDescription());
		nodeResult.put("lastCheck", calendarUtil.toDateTimeString(node.getLastCheck()));
		nodeResult.put("failureCounter", node.getFailureCounter());
		nodeResult.put(RESULT, node.getResult());
		nodeResult.put("active", node.getActive());
		nodeResult.put("silent", node.getSilent());
		nodeResult.put("checkType", node.getCheckType());
		return nodeResult;
	}
}
