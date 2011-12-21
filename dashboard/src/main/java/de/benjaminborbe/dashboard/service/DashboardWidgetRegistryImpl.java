package de.benjaminborbe.dashboard.service;

import com.google.inject.Singleton;

import de.benjaminborbe.dashboard.api.DashboardWidget;
import de.benjaminborbe.tools.util.RegistryImpl;

@Singleton
public class DashboardWidgetRegistryImpl extends RegistryImpl<DashboardWidget> implements DashboardWidgetRegistry {

}
