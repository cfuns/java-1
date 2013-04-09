package de.benjaminborbe.poker.table.client.gin;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

import de.benjaminborbe.poker.table.client.MainPanel;

@GinModules(PokerTableClientModule.class)
public interface PokerTableClientGinjector extends Ginjector {

	MainPanel getMainPanel();

}
