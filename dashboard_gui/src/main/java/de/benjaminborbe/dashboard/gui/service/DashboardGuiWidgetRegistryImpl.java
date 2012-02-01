package de.benjaminborbe.dashboard.gui.service;

import com.google.inject.Singleton;

import de.benjaminborbe.dashboard.api.DashboardContentWidget;
import de.benjaminborbe.tools.util.RegistryImpl;

@Singleton
public class DashboardGuiWidgetRegistryImpl extends RegistryImpl<DashboardContentWidget> implements DashboardGuiWidgetRegistry {

}
