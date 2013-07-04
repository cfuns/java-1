package de.benjaminborbe.poker.table.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import de.benjaminborbe.poker.table.client.gin.PokerTableClientGinjector;

public class Home implements EntryPoint {

	private final PokerTableClientGinjector injector = GWT.create(PokerTableClientGinjector.class);

	@Override
	public void onModuleLoad() {
		final Widget panel = injector.getMainPanel();
		RootPanel.get().add(panel);
	}
}
