package de.benjaminborbe.gwt.gui.client.gin;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import de.benjaminborbe.gwt.gui.client.MainPanel;

@GinModules(GwtClientModule.class)
public interface GwtClientGinjector extends Ginjector {

	MainPanel getMainPanel();

}
