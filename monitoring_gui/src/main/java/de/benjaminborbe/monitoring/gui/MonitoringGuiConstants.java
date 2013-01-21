package de.benjaminborbe.monitoring.gui;

import de.benjaminborbe.monitoring.api.MonitoringCheckType;

public interface MonitoringGuiConstants {

	String NAME = "monitoring";

	String PARAMETER_NODE_CHECK_TYPE = "node_check_type";

	String PARAMETER_NODE_ID = "node_id";

	String PARAMETER_NODE_NAME = "node_name";

	String PARAMETER_REFERER = "referer";

	String URL_HOME = "/";

	String URL_NODE_CREATE = "/node/create";

	String URL_NODE_DELETE = "/node/delete";

	String URL_NODE_LIST = "/node";

	String URL_NODE_UPDATE = "/node/update";

	MonitoringCheckType DFEAULT_CHECK = MonitoringCheckType.NOP;

	String PARAMETER_PREFIX = "parameter_";

	String URL_VIEW = "/view";

}
