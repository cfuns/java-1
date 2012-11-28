package de.benjaminborbe.dashboard.api;

import de.benjaminborbe.html.api.Widget;

public interface DashboardContentWidget extends Widget {

	String getTitle();

	long getPriority();

	boolean isAdminRequired();

}
