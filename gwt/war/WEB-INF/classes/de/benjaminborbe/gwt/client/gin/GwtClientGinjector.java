package de.benjaminborbe.gwt.client.gin;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

import de.benjaminborbe.gwt.client.MainPanel;

@GinModules(GwtClientModule.class)
public interface GwtClientGinjector extends Ginjector {

	MainPanel getMainPanel();

}
