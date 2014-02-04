package de.benjaminborbe.monitoring.tools;

import de.benjaminborbe.monitoring.api.MonitoringNode;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.json.JSONObject;
import de.benjaminborbe.tools.json.JSONObjectSimple;

import javax.inject.Inject;

public class MapperJsonObjectMonitoringNode {

	public static final String MESSAGE = "message";

	public static final String RESULT = "result";

	public static final String ID = "id";

	public static final String NAME = "name";

	public static final String DESCRIPTION = "description";

	public static final String LAST_CHECK = "lastCheck";

	public static final String FAILURE_COUNTER = "failureCounter";

	public static final String ACTIVE = "active";

	public static final String SILENT = "silent";

	public static final String CHECK_TYPE = "checkType";

	private final CalendarUtil calendarUtil;

	@Inject
	public MapperJsonObjectMonitoringNode(final CalendarUtil calendarUtil) {
		this.calendarUtil = calendarUtil;
	}

	public JSONObject map(final MonitoringNode node) {
		final JSONObject nodeResult = new JSONObjectSimple();
		nodeResult.put(ID, node.getId());
		nodeResult.put(MESSAGE, node.getMessage());
		nodeResult.put(NAME, node.getName());
		nodeResult.put(DESCRIPTION, node.getDescription());
		nodeResult.put(LAST_CHECK, calendarUtil.toDateTimeString(node.getLastCheck()));
		nodeResult.put(FAILURE_COUNTER, node.getFailureCounter());
		nodeResult.put(RESULT, node.getResult());
		nodeResult.put(ACTIVE, node.getActive());
		nodeResult.put(SILENT, node.getSilent());
		nodeResult.put(CHECK_TYPE, node.getCheckType());
		return nodeResult;
	}
}
